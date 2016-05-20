package cn.com.flaginfo.ware.config;

public class ApplicationConfig {

    private static String dynamicDomain;

    public static String getDynamicDomain() {
        return dynamicDomain;
    }

    public void setDynamicDomain(String dynamicDomain) {
        ApplicationConfig.dynamicDomain = dynamicDomain;
    }

}
