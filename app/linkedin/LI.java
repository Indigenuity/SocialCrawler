package linkedin;

import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import utils.ThrottledLimiter;

public class LI {
	private static final String CHROME_EXE = "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe";
	private static final String COOKIES = "--user-data-dir=C:\\Users\\JD\\AppData\\Local\\Google\\Chrome\\User Data";
	
	private static final ThrottledLimiter rateLimiter;
	private static WebDriver driver;
	private static Boolean initialized = false;
	
	static {
		
//		driver = null;
		
		rateLimiter = new ThrottledLimiter(.2, 60, TimeUnit.SECONDS); 
	}
	
	private static void initialize(){
		synchronized(initialized){
			
			if(!initialized) {
				System.out.println("Initializing");
				System.setProperty("webdriver.chrome.driver", "./chromedrivertoonew.exe"); 
				ChromeOptions opt = new ChromeOptions();
				opt.setBinary(CHROME_EXE);
				opt.addArguments(COOKIES);
				System.out.println("about to call new chromedriver");
				driver = new ChromeDriver(opt);
				System.out.println("finished initializing");
				initialized = true;
			}
		}
	}
	
	
	public static Document getMainDocument(String seed) {
		initialize();
		System.out.println("getting main document");
		rateLimiter.acquire();
		driver.get(seed);
		System.out.println("retrieved main, about to return DOM");
		return Jsoup.parse(driver.getPageSource());
	}
	
	public static Document getFullDocument(String seed) throws InterruptedException{
		initialize();
		rateLimiter.acquire();
		driver.get(seed);
		scrollToFull();
		return Jsoup.parse(driver.getPageSource());
	}
	
	private static void scrollToFull() {
		boolean moreToView = true;
		do{
			try{
				rateLimiter.acquire();
				WebElement viewMoreButton = driver.findElement(By.className("view-more"));
				viewMoreButton.click();
			} catch(NoSuchElementException e){
				System.out.println("no view-more button");
				break;
			} catch(ElementNotVisibleException e){
				System.out.println("no view-more button");
				break;
			}
		}
		while(moreToView);
	}
}
