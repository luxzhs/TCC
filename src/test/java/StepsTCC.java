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
import org.openqa.selenium.TimeoutException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class StepsTCC {

	WebDriver driver;
	WebDriverWait wait;

	@Dado("que o usuário está na página inicial da Amazon")
	public void queOUsuarioEstaNaPaginaInicialDaAmazon() {
		System.setProperty("webdriver.chrome.driver", "C:\\temp\\Drivers\\chromedriver-win64\\chromedriver.exe");
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		driver.get("https://www.amazon.com.br/ref=nav_logo");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Dado("o usuário está logado")
	public void oUsuárioEstáLogado() {
		WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-link-accountList")));
		signInButton.click();

		WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ap_email")));
		emailField.sendKeys("luiz.felipea@sempreceub.com"); // Substitua com seu email

		WebElement continueButton = driver.findElement(By.id("continue"));
		continueButton.click();

		WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ap_password")));
		passwordField.sendKeys("2190@tcc"); // Substitua com sua senha

		WebElement signInSubmitButton = driver.findElement(By.id("signInSubmit"));
		signInSubmitButton.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-link-accountList"))); // Confirma que o
																									// login foi
																									// bem-sucedido
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
			WebElement primeFilter = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
					"/html/body/div[1]/div[1]/div[1]/div[2]/div/div[3]/span/div[1]/div/div/div[6]/ul[1]/span/span[1]/li/span/a/span")));
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", primeFilter);
		}
	}

	@Entao("os resultados da busca são filtrados para mostrar apenas produtos elegíveis para Novo")
	public void osResultadosDaBuscaSaoFiltradosParaMostrarApenasProdutosElegiveisParaNovo() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

		// Esperar pela visibilidade do elemento que indica que a página de resultados
		// está carregada
		try {
			WebElement filtroNovoElemento = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//*[@id=\"p_n_condition-type/13862762011\"]/span/a/span")));
		} catch (TimeoutException e) {
			throw new AssertionError("O elemento esperado não se tornou visível no tempo esperado.");
		}

		// Verificar se todos os produtos exibidos são "Novo"
		List<WebElement> elementosFiltrados = driver
				.findElements(By.xpath("//*[@id=\"p_n_condition-type/13862762011\"]/span/a/span"));

		boolean filtroAplicado = elementosFiltrados.stream().allMatch(element -> element.getText().contains("Novo"));

		if (!filtroAplicado) {
			throw new AssertionError("Filtro Novo não foi aplicado corretamente");
		}
	}

	@Quando("o usuário seleciona um produto dos resultados da busca")
	public void oUsuarioSelecionaUmProdutoDosResultadosDaBusca() {
		WebElement primeiroProduto = wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//*[@id=\"search\"]/div[1]/div[1]/div/span[1]/div[1]/div[4]/div")));
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
		WebElement precoProduto = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//*[@id=\"corePrice_feature_div\"]/div/div/span[1]/span[2]/span[2]")));
		WebElement descricaoProduto = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("feature-bullets")));

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
			WebElement addToCartButton = wait
					.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-button")));
			addToCartButton.click();
			try {
				WebElement garantiaButton = wait.until(ExpectedConditions
						.elementToBeClickable(By.xpath("//*[@id=\"attachSiAddCoverage\"]/span/input")));
				garantiaButton.click();
			} catch (Exception e) {
				System.out.println("Página de garantia estendida não apareceu.");
			}
		}
	}

	@Entao("o produto é adicionado ao carrinho")
	public void oProdutoEAdicionadoAoCarrinho() {
		WebElement mensagemAdicionado = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.cssSelector("div#huc-v2-order-row-confirm-text span")));
		boolean produtoAdicionado = mensagemAdicionado.getText().contains("Adicionado ao carrinho");
		if (!produtoAdicionado) {
			throw new AssertionError("Produto não foi adicionado ao carrinho");
		}
	}

	@Entao("o usuário navega para a página do carrinho")
	public void oUsuárioNavegaParaAPáginaDoCarrinho() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		// Esperar até que o elemento seja visível
		WebElement carrinhoElemento = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"sw-gtc\"]/span/a")));

		// Tentar clicar no elemento
		try {
			// Garantir que o elemento está na viewport
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", carrinhoElemento);
			wait.until(ExpectedConditions.elementToBeClickable(carrinhoElemento)).click();
		} catch (org.openqa.selenium.ElementClickInterceptedException e) {
			// Usar JavaScript para clicar no elemento se a interceptação persistir
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", carrinhoElemento);
		}
	}

	@Quando("o usuário clica no botão {string} para o produto")
	public void oUsuarioClicaNoBotaoParaOProduto(String botao) {
		if (botao.equalsIgnoreCase("Excluir")) {
			while (true) {
				try {
					WebElement excluirButton = wait
							.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[value='Excluir']")));
					((JavascriptExecutor) driver).executeScript("arguments[0].click();", excluirButton);
					wait.until(ExpectedConditions.stalenessOf(excluirButton));
				} catch (Exception e) {
					break;
				}
			}
		}
	}

	@Entao("o carrinho é atualizado de acordo")
	public void oCarrinhoEAtualizadoDeAcordo() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("nav-cart-count"), "0"));

		String carrinhoCount = driver.findElement(By.id("nav-cart-count")).getText();
		if (!carrinhoCount.equals("0")) {
			throw new AssertionError("Carrinho não foi atualizado de acordo");
		}

		// Fecha o driver após realizar o step
		driver.quit();
	}

	@Quando("o usuário busca por {string}")
	public void oUsuarioBuscaPor(String produto) {
		WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
		searchBox.sendKeys(produto);
		WebElement searchButton = driver.findElement(By.id("nav-search-submit-button"));
		searchButton.click();
	}


	@Quando("o usuário aplica o filtro do celular {string}")
	public void oUsuarioAplicaOFiltroDoCelular(String filtro) {
		try {
			WebElement abrirFiltro = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("/html/body/div[1]/div[1]/span[2]/div/h1/div/div[4]/div/div/form/span/span/span/span")));
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", abrirFiltro);

			if (filtro.equalsIgnoreCase("Mais caros primeiro")) {
				WebElement filtroMaisCaros = wait.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath("/html/body/div[4]/div/div/ul/li[2]/a")));
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
			WebElement filtroEstoque = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//*[@id=\"p_n_availability/16254085011\"]/span/a/div/label/i")));
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", filtroEstoque);
		}
	}

	@Entao("o produto é adicionado")
	public void oProdutoEAdicionado() {
		WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
				"/html/body/div[1]/div[1]/div[1]/div[1]/div/span[1]/div[1]/div[3]/div/div/span/div/div/div[3]/div[6]/div/div/div[1]/span/div/span/span/button")));
		addToCartButton.click();

		try {
			WebElement garantiaButton = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("//button[contains(text(), 'Adicionar Garantia')]")));
			garantiaButton.click();
		} catch (Exception e) {
			System.out.println("Página de garantia estendida não apareceu.");
		}
	}

	@Entao("é possível visualizar o produto no carrinho")
	public void ePossivelVisualizarOProdutoNoCarrinho() {
		WebElement carrinhoButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-cart")));
		carrinhoButton.click();

		WebElement produtoNoCarrinho = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//*[@id=\"sc-active-cart\"]/div/div/div[1]/h1")));
		if (!produtoNoCarrinho.isDisplayed()) {
			throw new AssertionError("Produto não está visível no carrinho");
		}
		// Fecha o driver após realizar o step
		driver.quit();
	}

	@Dado("que o usuário está na página da Amazon")
	public void queOUsuarioEstáNaPáginaDaAmazon() {
		System.setProperty("webdriver.chrome.driver", "C:\\temp\\Drivers\\chromedriver-win64\\chromedriver.exe");
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		driver.get("https://www.amazon.com.br/ref=nav_logo");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Quando("ele clica em {string}")
	public void eleClicaEm(String linkTexto) {
		try {
			WebElement linkAnuncieSeusProdutos = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(linkTexto)));
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", linkAnuncieSeusProdutos);
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Elemento não encontrado dentro do tempo de espera: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Ocorreu um erro: " + e.getMessage());
		}
	}

	@Entao("outra aba abre para o usuário proceder com seus produtos")
	public void outraAbaAbreParaOUsuarioProcederComSeusProdutos() {
		try {
			Thread.sleep(500);
			ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
			driver.switchTo().window(tabs.get(1));
		} catch (InterruptedException e) {
			System.out.println("Thread interrupted: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Ocorreu um erro: " + e.getMessage());
		}
		// Fecha o driver após realizar o step
		driver.quit();
	}
	
}	  

	

/*
 * @After public void tearDown() { if (driver != null) { driver.quit(); } } }
 */
