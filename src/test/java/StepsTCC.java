import io.cucumber.java.After;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Entao;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;

import java.time.Duration;


public class StepsTCC {

    WebDriver driver;
    WebDriverWait wait;

    @Dado("que o usuário está na página inicial da Amazon")
    public void queOUsuarioEstaNaPaginaInicialDaAmazon() {
        System.setProperty("webdriver.chrome.driver", "C:\\temp\\Drivers\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://www.amazon.com.br");
    }

    @Quando("o usuário digita {string} na barra de busca")
    public void oUsuarioDigitaNaBarraDeBusca(String produto) {
        WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
        searchBox.sendKeys(produto);
    }

    @Quando("o usuário clica no botão de busca")
    public void oUsuarioClicaNoBotaoDeBusca() {
        WebElement searchButton = driver.findElement(By.id("nav-search-submit-button"));
        searchButton.click();
    }

    @Entao("os resultados da busca por {string} são exibidos")
    public void osResultadosDaBuscaPorSaoExibidos(String produto) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.s-main-slot")));
        boolean resultadosExibidos = driver.findElement(By.cssSelector("div.s-main-slot")).isDisplayed();
        if (!resultadosExibidos) {
            throw new AssertionError("Resultados da busca não foram exibidos");
        }
    }

    @Quando("o usuário aplica o filtro {string}")
    public void oUsuarioAplicaOFiltro(String filtro) {
        if (filtro.equalsIgnoreCase("Novo")) {
            WebElement primeFilter = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div/div[3]/span/div[1]/div/div/div[6]/ul[1]/span/span[1]/li/span/a/span")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", primeFilter);
        }
    }
   
    @Entao("os resultados da busca são filtrados para mostrar apenas produtos elegíveis para Novo")
    public void osResultadosDaBuscaSaoFiltradosParaMostrarApenasProdutosElegiveisParaNovo() {
        // Espera até que o contêiner principal dos resultados da busca esteja visível
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div/div[3]/span/div[1]/div/div/div[6]/ul[1]/span/span[1]/li/span/a/span")));
        
        // Encontra todos os elementos que têm a classe 'a-badge-text' e verifica se todos contêm o texto 'Prime'
        boolean filtroAplicado = driver.findElements(By.xpath("//*[@id=\"p_n_condition-type/13862762011\"]/span/a/span")).stream()
                                    .allMatch(element -> element.getText().contains("Novo"));
        
        // Se não houver filtro aplicado corretamente, lança uma exceção
        if (!filtroAplicado) {
            throw new AssertionError("Filtro Novo não foi aplicado corretamente");
        }
    }


    @Quando("o usuário seleciona um produto dos resultados da busca")
    public void oUsuarioSelecionaUmProdutoDosResultadosDaBusca() {
        WebElement primeiroProduto = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.s-main-slot div[data-component-type='s-search-result']")));
        primeiroProduto.click();
    }

    @Entao("a página de detalhes do produto é exibida")
    public void aPaginaDeDetalhesDoProdutoEExibida() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("productTitle")));
        boolean detalhesExibidos = driver.findElement(By.id("productTitle")).isDisplayed();
        if (!detalhesExibidos) {
            throw new AssertionError("Página de detalhes do produto não foi exibida");
        }
    }

    @Entao("o título, preço e descrição do produto são visíveis")
    public void oTituloPrecoEDescricaoDoProdutoSaoVisiveis() {
        WebElement tituloProduto = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("productTitle")));
        WebElement precoProduto = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"corePrice_feature_div\"]/div/div/span[1]/span[2]/span[2]")));
        WebElement descricaoProduto = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("feature-bullets")));

        boolean tituloVisivel = tituloProduto.isDisplayed();
        boolean precoVisivel = precoProduto.isDisplayed();
        boolean descricaoVisivel = descricaoProduto.isDisplayed();

        if (!tituloVisivel || !precoVisivel || !descricaoVisivel) {
            throw new AssertionError("Título, preço ou descrição do produto não estão visíveis");
        }
    }
    
    @Quando("o usuário clica no botão {string}")
    public void oUsuarioClicaNoBotao(String botao) {
        if (botao.equalsIgnoreCase("Adicionar ao Carrinho")) {
            WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-button")));
            addToCartButton.click();
            
            // Verificar se a página de garantia estendida aparece e aceitar a garantia
            try {
                WebElement garantiaButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"attachSiAddCoverage\"]/span/input")));
                garantiaButton.click();
            } catch (Exception e) {
                // Se a página de garantia estendida não aparecer, continuar normalmente
                System.out.println("Página de garantia estendida não apareceu.");
            }
        }
    }

    @Entao("o produto é adicionado ao carrinho")
    public void oProdutoEAdicionadoAoCarrinho() {
        // Esperar até que a mensagem de sucesso seja visível
        WebElement mensagemAdicionado = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div#huc-v2-order-row-confirm-text span")));
        
        // Verificar se a mensagem de sucesso contém o texto esperado
        boolean produtoAdicionado = mensagemAdicionado.getText().contains("Adicionado ao carrinho");
        if (!produtoAdicionado) {
            throw new AssertionError("Produto não foi adicionado ao carrinho");
        }
    }

    @Quando("o usuário navega para a página do carrinho")
    public void oUsuarioNavegaParaAPaginaDoCarrinho() {
        boolean success = false;
        int attempts = 0;

        while (attempts < 3) {
            try {
                WebElement carrinhoButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-cart")));
                carrinhoButton.click();
                success = true;
                break;
            } catch (StaleElementReferenceException e) {
                attempts++;
            }
        }

        if (!success) {
            throw new AssertionError("Não foi possível clicar no botão do carrinho após várias tentativas");
        }
    }

    @Quando("o usuário clica no botão {string} para o produto")
    public void oUsuarioClicaNoBotaoParaOProduto(String botao) {
        if (botao.equalsIgnoreCase("Excluir")) {
            // Continuar excluindo itens enquanto houver itens no carrinho
            while (true) {
                try {
                    // Obter o próximo botão "Excluir" disponível no carrinho
                    WebElement excluirButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[value='Excluir']")));
                    
                    // Usar JavascriptExecutor para clicar no botão "Excluir"
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", excluirButton);

                    // Esperar até que o botão "Excluir" não seja mais clicável (indicando que o item foi excluído)
                    wait.until(ExpectedConditions.stalenessOf(excluirButton));
                } catch (Exception e) {
                    // Se não houver mais botões "Excluir" disponíveis, sair do loop
                    break;
                }
            }
        }
    }


    @Entao("o carrinho é atualizado de acordo")
    public void oCarrinhoEAtualizadoDeAcordo() {
        // Aumentar o tempo de espera para garantir que o contador do carrinho seja atualizado
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        // Esperar até que o contador do carrinho seja atualizado para 0
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("nav-cart-count"), "0"));

        String carrinhoCount = driver.findElement(By.id("nav-cart-count")).getText();
        if (!carrinhoCount.equals("0")) {
            throw new AssertionError("Carrinho não foi atualizado de acordo");
        }
    }
    @Quando("o usuário busca por {string}")
    public void oUsuarioBuscaPor(String produto) {
        WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
        searchBox.sendKeys(produto);
        WebElement searchButton = driver.findElement(By.id("nav-search-submit-button"));
        searchButton.click();
    }
    
    @Dado("que o usuário está na página inicial")
    public void queOUsuarioEstaNaPaginaInicial() {
        System.setProperty("webdriver.chrome.driver", "C:\\temp\\Drivers\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://www.amazon.com.br");
    }
    
    @Quando("o usuário aplica o filtro do celular {string}")
    public void oUsuarioAplicaOFiltroDoCelular(String filtro) {
        try {
            // Primeiro clique para abrir a rolagem de filtros
            WebElement abrirFiltro = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div[1]/span[2]/div/h1/div/div[4]/div/div/form/span/span/span/span")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", abrirFiltro);

            // Segundo clique para selecionar o filtro desejado
            if (filtro.equalsIgnoreCase("Mais caros primeiro")) {
                WebElement filtroMaisCaros = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"s-result-sort-select_2\"]")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", filtroMaisCaros);
            }
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Elemento não encontrado dentro do tempo de espera: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocorreu um erro: " + e.getMessage());
        }
    }


    @Quando("o usuário seleciona o filtro {string}")
    public void oUsuarioSelecionaOFiltro(String filtro) {
        if (filtro.equalsIgnoreCase("Exibir itens em estoque")) {
            WebElement filtroEstoque = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"p_n_availability/16254085011\"]/span/a/div/label/i")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", filtroEstoque);
        }
    }

    @Entao("o produto é adicionado")
    public void oProdutoEAdicionado() {
        WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div[1]/div[1]/div[1]/div/span[1]/div[1]/div[4]/div/div/span/div/div/div[2]/div[7]/div/div/div[1]/span/div/span/span/button")));
        addToCartButton.click();

        // Verificar se a página de garantia estendida aparece e aceitar a garantia
        try {
            WebElement garantiaButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Adicionar Garantia')]")));
            garantiaButton.click();
        } catch (Exception e) {
            // Se a página de garantia estendida não aparecer, continuar normalmente
            System.out.println("Página de garantia estendida não apareceu.");
        }
    }

    @Entao("é possível visualizar o produto no carrinho")
    public void ePossivelVisualizarOProdutoNoCarrinho() {
        WebElement carrinhoButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-cart")));
        carrinhoButton.click();

        WebElement produtoNoCarrinho = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.sc-list-item")));
        if (!produtoNoCarrinho.isDisplayed()) {
            throw new AssertionError("Produto não está visível no carrinho");
        }
    }

    // Método para encerrar o WebDriver após cada teste
    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
