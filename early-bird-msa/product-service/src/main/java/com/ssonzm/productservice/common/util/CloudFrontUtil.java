package com.ssonzm.productservice.common.util;

public class CloudFrontUtil {
    public static String getCloudFrontImageUrl(String domain, String storeFileName) {
        return "https://" + domain + "/" + storeFileName;
    }
}
