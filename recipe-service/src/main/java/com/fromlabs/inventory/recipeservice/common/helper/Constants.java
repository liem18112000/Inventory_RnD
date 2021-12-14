package com.fromlabs.inventory.recipeservice.common.helper;

public class Constants {
    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static final String PHONE_PATTERN = "^\\(?\\+?\\d{1,4}?\\)?[-.\\s]?\\(?\\d{1,3}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}$";
    public static final String NUMBER_PATTERN = "^[0-9]+$";
    public static final String INFO_QR_CODE_PATTERN = "[a-z0-9]{8}";

    public static final String timezoneUTC = "UTC";
    public static final String timezoneDefault = "UTC";
    public static final String PATTERN_STANDARD_DATETIME_FORMAT = "yyyy/MM/dd HH:mm";
    public static final String PATTERN_FULL_DATETIME_FORMAT = "yyyy-MM-dd HH:mm";
}