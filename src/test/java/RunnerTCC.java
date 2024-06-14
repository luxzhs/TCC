
import org.junit.runner.*;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = "src/test/resources/CenariosAmazon.feature",	
		plugin = {"pretty","html:target/relatorio.html"},
		tags = "not @ignorar",
		monochrome = true,
		snippets = SnippetType.CAMELCASE,
		dryRun = false
		
		)
public class RunnerTCC {

}
