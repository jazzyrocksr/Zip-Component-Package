import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

public class PackageInstall {
	
	private WebDriver driver;
	private static String packageLink;
	private StringBuffer verificationErrors = new StringBuffer();
	public static String userName="";
	public static String password="";
	public static String apiURL="";
	public static String pkgName="";
	public static double apiVersion;
	private static Properties properties=null;
	
	@Before
	public void setUp() throws Exception {
		
		//Initialize the Firefox Browser Driver
		driver = new FirefoxDriver();
		//Read config.properties and read various attributes
		try {
			if(properties == null){
				properties = new Properties();
			    properties.load(new FileInputStream("config.properties"));
			}
		} catch (IOException e) {
			System.err.println("Error while reading Connection Property file : " + e.getMessage());
			throw e;
		}
		packageLink = properties.getProperty("sf.packageLink");
		userName=properties.getProperty("sf.username");
		password=properties.getProperty("sf.password");
		apiURL=properties.getProperty("sf.APIURL");
		apiVersion=Double.parseDouble(properties.getProperty("sf.APIVersion"));
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	//This would install the package
	@Test
	public void testPackageInstall() throws Exception {
		
		driver.get(packageLink);
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys(userName);
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.id("Login")).click();
		driver.findElement(By.id("InstallPackagePage:InstallPackageForm:InstallBtn")).click();
		driver.findElement(By.name("goNext")).click();
		driver.findElement(By.id("p201FULL")).click();
		driver.findElement(By.name("goNext")).click();
		driver.findElement(By.name("save")).click();
		driver.findElement(By.name("viewComponents")).click();
		pkgName=driver.findElement(By.cssSelector("h2.pageDescription")).getText();
		System.out.println("Package Name Found :" + pkgName);
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
		//Once the package is installed successfully, retrieve the package components
		else{
			RetreivePackageComponent rpc = new RetreivePackageComponent();
	        rpc.run();
		}
		
	}

	
}
