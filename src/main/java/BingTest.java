import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class BingTest {

    public static void main(String[] args) {
        //create the new google driver (EventFiringWebDriver)
        EventFiringWebDriver driver = new EventFiringWebDriver(new BingTest().getDriver());
        //register the WebDriverEventListener
        driver.register(new EventHandler());
        //create the JavascriptExecutor object
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        //create the wait element
        WebDriverWait wait = new WebDriverWait(driver, 10);
        //open the bing search page
        driver.get("https://www.bing.com/");
        //wait the next navigation element by class name
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@id='scpl1']")));

        //find the "Pictures" text link by xpath
        WebElement pictureTextLink = driver.findElement(By.xpath("//a[@id='scpl1']"));
        //click the text link
        pictureTextLink.click();

        //wait the next navigation element by class name
        wait.until(ExpectedConditions.titleIs("Лента изображений Bing"));

        //scroll
        for (int i = 0; i < 3; i++) {
            //create the first list of the search results by xpath
            List<WebElement> first = driver.findElements(By.xpath("//div[@class='img_cont hoff']/img"));
            //scroll to the bottom of the page
            jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            //wait for loading new content (while uploading the "expandButton active" class is becoming as "expandButton loading")
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='expandButton active']")));
            //create the second list of the search results by xpath
            List<WebElement> second = driver.findElements(By.xpath("//div[@class='img_cont hoff']/img"));
            //check that the pictures were uploaded
            if (first.size() < second.size()) {
                System.out.println(String.format("The pictures are uploaded! The size before uploading is %d, the size after uploading is %d", first.size(), second.size()));
            } else System.err.println("The pictures are not uploaded");
        }
        //scroll to the top of the page
        jse.executeScript("window.scrollTo(0, 0)");

        //find the input search field by class name
        WebElement input = driver.findElement(By.className("b_searchbox"));
        //fill the input field
        input.sendKeys("automatio");
        //wait for the "automation" text
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//strong[text()='n']")));

        //find the "automation" text link by xpath
        WebElement automation = driver.findElement(By.xpath("//strong[text()='n']"));
        //click the text link
        automation.click();

        //wait for uploading pictures
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='dg_u']/div/a/img")));

        //wait the "Дата" text link by cssSelector
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#ftrB>ul>li:nth-child(6)>span>span")));
        //find the "Дата" dropdown menu by cssSelector
        WebElement dateElement = driver.findElement(By.cssSelector("#ftrB>ul>li:nth-child(6)>span>span"));
        //click the "Дата" dropdown menu
        dateElement.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='ftrDC ']")));
        //find the "В прошлом месяце" text link by cssSelector
        WebElement lastMonthElement = driver.findElement(By.cssSelector("#ftrB>ul>li:nth-child(6)>div>div>a:nth-child(4)>span"));
        //click the "В прошлом месяце" text link
        lastMonthElement.click();

        //wait for upload pictures
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='dg_c']")));

        //create the list of the pictures by xpath
        List<WebElement> element = driver.findElements(By.xpath("//div[@id='dg_c']/div/div/div/div/a/img"));
        //click the first image
        element.get(0).click();

        //wait for the image frame
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//iframe[@id='OverlayIFrame']")));
        //switch to the frame
        driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@id='OverlayIFrame']")));

        //wait for the main image
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@class='mainImage accessible nofocus']")));
        //find the next button
        WebElement nextImage = driver.findElement(By.xpath("//*[@filter='url(#ds_right)']/.."));
        //click the next button
        nextImage.click();
        //wait for the main image
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@class='mainImage accessible nofocus']")));
        //find the previous button
        WebElement previousImage = driver.findElement(By.xpath("//*[@filter='url(#ds_left)']/.."));
        //click the previous button
        previousImage.click();
        //wait for the main image
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@class='mainImage accessible nofocus']")));

        //find the main image
        WebElement mainImage = driver.findElement(By.xpath("//img[@class='mainImage accessible nofocus']"));
        //click the main image
        mainImage.click();

        //find the url image
        String urlImage = mainImage.getAttribute("src");
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
