package data.twitterScraper;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class twitterScrawl {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Webdriver\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();

        // 1 - Đi đến url đăng nhập
        driver.navigate().to("https://twitter.com/i/flow/login");
        Thread.sleep(2000);

        // 2 - Nhập username và password
        driver.findElement(By.xpath("//input[@autocomplete='username']")).sendKeys("BeXuanMike25");
        driver.findElement(By.xpath("//input[@autocomplete='username']/following::div[@role='button']")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys("quan252556915691");
        driver.findElement(By.xpath("//span[text()='Log in']")).click();

        // 3 - Tìm kiếm từ khóa
        Thread.sleep(5000);
        WebElement search = driver.findElement(By.xpath("//input[@data-testid='SearchBox_Search_Input']"));
        search.sendKeys("#NFT");
        search.sendKeys(Keys.ENTER);
        Thread.sleep(5000);
    }
}