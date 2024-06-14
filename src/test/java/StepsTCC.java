import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Entao;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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

    @Quando("o usuário busca por {string}")
    public void oUsuarioBuscaPor(String produto) {
        oUsuarioDigitaNaBarraDeBusca(produto);
        oUsuarioClicaNoBotaoDeBusca();
    }

    @Quando("o usuário aplica o filtro {string}")
    public void oUsuarioAplicaOFiltro(String filtro) {
        if (filtro.equalsIgnoreCase("Prime")) {
            WebElement primeFilter = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("i.a-icon.a-icon-prime")));
            primeFilter.click();
        }
    }

    @Entao("os resultados da busca são filtrados para mostrar apenas produtos elegíveis para Prime")
    public void osResultadosDaBuscaSaoFiltradosParaMostrarApenasProdutosElegiveisParaPrime() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.a-badge-text")));
        boolean filtroAplicado = driver.findElement(By.cssSelector("span.a-badge-text")).getText().contains("Prime");
        if (!filtroAplicado) {
            throw new AssertionError("Filtro Prime não foi aplicado corretamente");
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
        boolean tituloVisivel = driver.findElement(By.id("productTitle")).isDisplayed();
        boolean precoVisivel = driver.findElement(By.id("priceblock_ourprice")).isDisplayed();
        boolean descricaoVisivel = driver.findElement(By.id("feature-bullets")).isDisplayed();
        if (!tituloVisivel || !precoVisivel || !descricaoVisivel) {
            throw new AssertionError("Título, preço ou descrição do produto não estão visíveis");
        }
    }

    @Quando("o usuário clica no botão {string}")
    public void oUsuarioClicaNoBotao(String botao) {
        if (botao.equalsIgnoreCase("Adicionar ao Carrinho")) {
            WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-button")));
            addToCartButton.click();
        }
    }

    @Entao("o produto é adicionado ao carrinho")
    public void oProdutoEAdicionadoAoCarrinho() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.a-size-medium.a-color-success")));
        boolean produtoAdicionado = driver.findElement(By.cssSelector("span.a-size-medium.a-color-success")).isDisplayed();
        if (!produtoAdicionado) {
            throw new AssertionError("Produto não foi adicionado ao carrinho");
        }
    }

    @Entao("o carrinho é atualizado com o novo produto")
    public void oCarrinhoEAtualizadoComONovoProduto() {
        String carrinhoCount = driver.findElement(By.id("nav-cart-count")).getText();
        if (!carrinhoCount.equals("1")) {
            throw new AssertionError("Carrinho não foi atualizado com o novo produto");
        }
    }

    @Dado("que o usuário tem um produto em seu carrinho")
    public void queOUsuarioTemUmProdutoEmSeuCarrinho() {
        queOUsuarioEstaNaPaginaInicialDaAmazon();
        oUsuarioBuscaPor("notebook");
        oUsuarioSelecionaUmProdutoDosResultadosDaBusca();
        oUsuarioClicaNoBotao("Adicionar ao Carrinho");
    }

    @Quando("o usuário navega para a página do carrinho")
    public void oUsuarioNavegaParaAPaginaDoCarrinho() {
        WebElement carrinhoButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-cart")));
        carrinhoButton.click();
    }

    @Quando("o usuário clica no botão {string} para o produto")
    public void oUsuarioClicaNoBotaoParaOProduto(String botao) {
        if (botao.equalsIgnoreCase("Excluir")) {
            WebElement excluirButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[value='Excluir']")));
            excluirButton.click();
        }
    }

    @Entao("o produto é removido do carrinho")
    public void oProdutoERemovidoDoCarrinho() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.sc-list-item")));
        boolean produtoRemovido = driver.findElements(By.cssSelector("div.sc-list-item")).isEmpty();
        if (!produtoRemovido) {
            throw new AssertionError("Produto não foi removido do carrinho");
        }
    }

    @Entao("o carrinho é atualizado de acordo")
    public void oCarrinhoEAtualizadoDeAcordo() {
        String carrinhoCount = driver.findElement(By.id("nav-cart-count")).getText();
        if (!carrinhoCount.equals("0")) {
            throw new AssertionError("Carrinho não foi atualizado de acordo");
        }
        driver.quit();
    }
    
}
