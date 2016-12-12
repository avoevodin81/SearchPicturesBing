import com.google.common.base.Predicate;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class BingTest {

    public static void main(String[] args) {
        //create the new google driver (EventFiringWebDriver)
        final EventFiringWebDriver driver = new EventFiringWebDriver(new BingTest().getDriver());
        //register the WebDriverEventListener
        driver.register(new EventHandler());
        //create the JavascriptExecutor object
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        //create the wait element
        WebDriverWait wait = new WebDriverWait(driver, 10);
        //open the bing search page
        driver.get("https://www.bing.com/");

        //wait the next navigation element by id
        By images = By.id("scpl1");
        wait.until(ExpectedConditions.presenceOfElementLocated(images));
        //find the "Pictures" text link
        WebElement pictureTextLink = driver.findElement(images);
        //click the text link
        pictureTextLink.click();

        //wait the next navigation element by class name
        wait.until(ExpectedConditions.titleIs("Лента изображений Bing"));

        //scroll
        for (int i = 0; i < 3; i++) {
            final By imagesList = By.xpath("//div[@class='img_cont hoff']/img");
            //create the first list of the search results
            final List<WebElement> first = driver.findElements(imagesList);
            //scroll to the bottom of the page
            jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            //wait for loading new content
            wait.until(new Predicate<WebDriver>() {
                public boolean apply(WebDriver webDriver) {
                    return first.size() < driver.findElements(imagesList).size();
                }
            });
            //create the second list of the search results
            List<WebElement> second = driver.findElements(imagesList);
            //print the result with quantity of previous and last results
            System.out.println(String.format("The pictures are uploaded! The size before uploading is %d, the size after uploading is %d", first.size(), second.size()));
        }
        //scroll to the top of the page
        jse.executeScript("window.scrollTo(0, 0)");

        //find the input search field by class name
        WebElement input = driver.findElement(By.className("b_searchbox"));
        //fill the input field
        input.sendKeys("automatio");

        //wait for the "automation" text
        By nText = By.xpath("//strong[text()='n']");
        wait.until(ExpectedConditions.presenceOfElementLocated(nText));
        //find the "automation" text link by xpath
        WebElement automation = driver.findElement(nText);
        //click the text link
        automation.click();

        //wait the "Дата" text link by cssSelector
        final By dateSelector = By.cssSelector("#ftrB>ul>li:nth-child(6)>span>span");
        final By dateDropDownMenu = By.xpath("//div[@class='ftrDC ']");
        wait.until(ExpectedConditions.presenceOfElementLocated(dateSelector));
        //check that the "Дата" dropdown menu is shown
        wait.until(new Predicate<WebDriver>() {
            public boolean apply(WebDriver webDriver) {
                //find the "Дата" dropdown menu by cssSelector
                WebElement dateElement = driver.findElement(dateSelector);
                //click the "Дата" dropdown menu
                dateElement.click();
                //If the "Дата" dropdown menu is not shown< repeat
                return driver.findElement(dateDropDownMenu).isDisplayed();
            }
        });

        //find the "В прошлом месяце" text link by cssSelector
        WebElement lastMonthElement = driver.findElement(By.cssSelector("#ftrB>ul>li:nth-child(6)>div>div>a:nth-child(4)>span"));
        //click the "В прошлом месяце" text link
        lastMonthElement.click();

        //wait for upload pictures
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("dg_c")));

        //Find the pictures by xpath
        WebElement element = driver.findElement(By.xpath("//div[@id='dg_c']/div/div/div/div/a/img"));
        //click the first image
        element.click();

        //wait for the image frame
        By frame = By.id("OverlayIFrame");
        wait.until(ExpectedConditions.presenceOfElementLocated(frame));
        //switch to the frame
        driver.switchTo().frame(driver.findElement(frame));

        //wait for the main image
        By mainImage = By.xpath("//img[@class='mainImage accessible nofocus']");
        wait.until(ExpectedConditions.presenceOfElementLocated(mainImage));
        //find the next button
        WebElement nextImage = driver.findElement(By.xpath("//*[@filter='url(#ds_right)']/.."));
        //click the next button
        nextImage.click();
        //wait for the main image
        wait.until(ExpectedConditions.presenceOfElementLocated(mainImage));
        //find the previous button
        WebElement previousImage = driver.findElement(By.xpath("//*[@filter='url(#ds_left)']/.."));
        //click the previous button
        previousImage.click();
        //wait for the main image
        wait.until(ExpectedConditions.presenceOfElementLocated(mainImage));

        //find the main image
        WebElement mainImageEl = driver.findElement(mainImage);
        //click the main image
        mainImageEl.click();

        //find the url image
        String urlImage = mainImageEl.getAttribute("src");
        //for checking the new tab
        boolean isPresented = false;

        //check all tabs with the url image
        for (String s : driver.getWindowHandles()) {
            driver.switchTo().window(s);
            if (urlImage.equals(driver.getCurrentUrl())) {
                isPresented = true;
                break;
            }
        }

        //check and print the result
        if (isPresented) {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//body/img[@src='" + urlImage + "']")));
            System.out.println("The image is loaded on the new tab");
        } else System.out.println("The image is not loaded on the new tab");

        //quit the google driver
        driver.quit();
    }

    public WebDriver getDriver() {
        //get driver path property
        String driverPath = System.getProperty("user.dir") + "/driver/chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", driverPath);

        return new ChromeDriver();
    }
}
