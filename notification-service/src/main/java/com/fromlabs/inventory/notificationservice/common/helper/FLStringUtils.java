package com.fromlabs.inventory.notificationservice.common.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FLStringUtils {
    private static final Logger logger = LoggerFactory.getLogger(FLStringUtils.class);

    public static String selfOrEmpty(String in){
        if(in == null) return "";
        return in;
    }

    public static String cutOffStringOverLimit(String s, int limit){
        if(s == null) return null;
        if(s.length() <= limit) return s;
        return s.substring(0, limit);
    }

    public static String generateGuestUserName(){
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        String year = df.format(now);

        StringBuilder sb = new StringBuilder();
        sb.append("Guest-").append(year).append("-").append(generateRandomNumericStringSizeOf(10));
        return sb.toString();
    }

    public static String generateRandomAlphaNumericCaseSensitiveStringSizeOf(int size){
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public static String generateRandomAlphaNumericCaseExcludeCharactersStringSizeOf(int size){
        char[] chars = "abcdefghjkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public static String generateRandomAlphaNumericNoCaseSensitiveStringSizeOf(int size){
        char[] chars = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public static String generateRandomNumericStringSizeOf(int size){
        char[] chars = "0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public static String generateRandomAlphaNumericCouponSizeOf(int size){
        char[] chars = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }


    public static String generateRandomColorSizeOf(int size){
        char[] chars = "abcdefABCDEF0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }


    public static String concat(String... strings){
        return Arrays.asList(strings).toString();
    }

    public static String convertYmcaDateToProgramDate(String input){
        SimpleDateFormat df = new SimpleDateFormat("mm/dd/yy");
        Date date = null;
        try {
            date = df.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        SimpleDateFormat ddf = new SimpleDateFormat("yyyy-MM-dd");
        return ddf.format(date);
    }

    public static String insertIntoString(String source, String needle, int ...index){
        StringBuffer sb = new StringBuffer(source);
        int needleSize = needle.length();
        int i = 0;
        for(int p : index){
            int pi = p + (i * needleSize) + 1;
            if( pi < sb.length())
                sb.insert(pi, needle);
            else
                break;
            i = 1;
        }
        return sb.toString();
    }

    public static boolean isNullOrEmpty(final String str) {
        if(str==null || str.trim().isEmpty())
            return true;
        return false;
    }

    public static boolean isEmailValid(final String email) {
        Pattern pattern = Pattern.compile(Constants.EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isStringValidPattern(final String strInput, final String strPattern){
        Pattern pattern = Pattern.compile(strPattern);
        Matcher matcher = pattern.matcher(strInput);
        return matcher.matches();
    }

    public static String ConcatNames(String... parts) {
        return ConcatStringsWithDelimiter(" ", parts);
    }

    public static String ConcatStringsWithDelimiter(String delimiter, String... parts) {
        StringJoiner joiner = new StringJoiner(delimiter);
        Arrays.stream(parts).filter(s -> s!=null&&!s.isEmpty()).forEach(joiner::add);
        return joiner.toString();
    }

    public static String ConcatPaths(String... paths){
        return ConcatStringsWithDelimiter(File.separator, paths);
    }
}
