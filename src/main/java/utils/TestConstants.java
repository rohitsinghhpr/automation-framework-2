package utils;

public class TestConstants {
    private static final String filePath = System.getProperty("user.dir")+"/config/config.properties";
    public static final String BASE_URL = PropertyReader.getProperty(filePath,"BASE_URL");
    public static final String USERNAME = PropertyReader.getProperty(filePath,"USERNAME");
    public static final String PASSWORD = PropertyReader.getProperty(filePath,"PASSWORD");
    public static final String BROWSER = PropertyReader.getProperty(filePath,"BROWSER");
    public static final String HEADLESS = PropertyReader.getProperty(filePath,"HEADLESS");
}
