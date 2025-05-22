package utils;

public class TestConstants {
    private static final String filePath = System.getProperty("user.dir")+"/config/config.properties";
    public static final String BASE_URL = PropertyReader.getProperty(filePath,"BASE_URL");
    public static final String USERNAME = PropertyReader.getProperty(filePath,"USERNAME");
    public static final String PASSWORD = PropertyReader.getProperty(filePath,"PASSWORD");
    public static final String BROWSER = PropertyReader.getProperty(filePath,"BROWSER");
    public static final String HEADLESS = PropertyReader.getProperty(filePath,"HEADLESS");
    public static final String REPORT_THEME = PropertyReader.getProperty(filePath,"REPORT_THEME");
    public static final String REPORT_TITLE = PropertyReader.getProperty(filePath,"REPORT_TITLE");
    public static final String REPORT_NAME = PropertyReader.getProperty(filePath,"REPORT_NAME");
    public static final String HOST_NAME = PropertyReader.getProperty(filePath,"HOST_NAME");
    public static final String ENVIRONMENT = PropertyReader.getProperty(filePath,"ENVIRONMENT");
    public static final String USER = PropertyReader.getProperty(filePath,"USER");
    public static final String SCREENSHOT_FORMAT = PropertyReader.getProperty(filePath,"SCREENSHOT_FORMAT");
    public static final int TIMEOUT = Integer.parseInt(PropertyReader.getProperty(filePath,"TIMEOUT"));
}
