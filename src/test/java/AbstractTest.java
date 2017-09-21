import enums.Platform;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.net.URL;
import static java.util.concurrent.TimeUnit.SECONDS;

import static enums.Platform.ANDROID;
import static enums.Platform.IOS;
import static pageobjects.BasePage.platform;

public class AbstractTest {

    static {
        setPlatform();
    }

    static AppiumDriver<MobileElement> driver;

    private static void setPlatform() {
        String platformVal = System.getProperty("platform");

        if (platformVal != null) {
            platform = platformVal.contains("ios") ? IOS : ANDROID;
        } else {
            platform = IOS;
        }
    }

    @BeforeTest
    public void setUp() throws Exception {
        URL remoteAddress = new URL("http://127.0.0.1:4723/wd/hub");

        if (platform.equals(Platform.IOS)) {
            File classPathRoot = new File(System.getProperty("user.dir"));
            File appDir = new File(classPathRoot, "app/iOS");
            File app = new File(appDir, "ContactsSimulator.app");

            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
            desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.IOS);
            desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 6 Simulator");
            desiredCapabilities.setCapability(MobileCapabilityType.UDID, "F678F40D-B14D-4252-B1AD-1997CBB7DF11");
            desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "10.3");
            desiredCapabilities.setCapability(MobileCapabilityType.APP, app);
            desiredCapabilities.setCapability(MobileCapabilityType.NO_RESET, true);
            desiredCapabilities.setCapability("newCommandTimeout", 300);

            driver = new IOSDriver<>(remoteAddress, desiredCapabilities);
        } else if (platform.equals(Platform.ANDROID)) {
            File classPathRoot = new File(System.getProperty("user.dir"));
            File appDir = new File(classPathRoot, "app/Android");
            File app = new File(appDir, "Contacts.apk");

            DesiredCapabilities desiredCapabilitiee = new DesiredCapabilities();
            desiredCapabilitiee.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
            desiredCapabilitiee.setCapability(MobileCapabilityType.DEVICE_NAME, "Google Nexus");
            desiredCapabilitiee.setCapability(MobileCapabilityType.APP, app);
            desiredCapabilitiee.setCapability("appPackage", "com.jayway.contacts");
            desiredCapabilitiee.setCapability("appActivity", "com.jayway.contacts.MainActivity");
            desiredCapabilitiee.setCapability("newCommandTimeout", 300);

            driver = new AndroidDriver<>(remoteAddress, desiredCapabilitiee);
        }
        driver.manage().timeouts().implicitlyWait(5, SECONDS);
    }

    @AfterTest
    public void tearDown() throws Exception {
        driver.quit();
    }

}
