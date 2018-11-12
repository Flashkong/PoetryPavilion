package com.poetrypavilion.poetrypavilion.Http;

public class HttpConfig {
    private static String getPoemUrl;
    private static String IPAddress = "http://192.168.43.243:8888/";
//    private static String IPAddress = "http://127.0.0.1:8888/";

    public static String getGetPoemUrl() {
        return getPoemUrl;
    }

    public static void setGetPoemUrl(String getPoemUrl) {
        HttpConfig.getPoemUrl = getPoemUrl;
    }

    public static String getIPAddress() {
        return IPAddress;
    }

    public static void setIPAddress(String IPAddress) {
        HttpConfig.IPAddress = IPAddress;
    }
}
