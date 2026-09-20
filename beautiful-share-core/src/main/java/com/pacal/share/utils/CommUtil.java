package com.pacal.share.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pacal.share.common.Constants;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public final class CommUtil {
    private CommUtil() {}

    public static boolean isJson(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readTree( jsonString );
            return true;
        } catch ( Exception ex ) {
            return false;
        }
    }

    public static String generateCode() {
        Random random = new Random();
        int fourNum = random.nextInt(9000) + 1000;
        return String.valueOf( fourNum );
    }

    public static int generateNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public static String generateRandomString(int length) {
        String allCode = "ABCDEFGHIJKMNPQRSTUVWXYZ23456789abcdefghijkmnpqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = generateNumber(0, allCode.length() - 1);
            sb.append(allCode.charAt(index));
        }
        return sb.toString();
    }

    public static String generateTradeNo() {
        List<Character> letters = new ArrayList<>();
        for ( char ch = 'A'; ch <= 'Z'; ch++ ) {
            letters.add( ch );
        }
        Collections.shuffle( letters );
        String tradeNo = LocalDateTime.now().format( DateTimeFormatter.ofPattern( "yyyyMMddHHmmss" ) );

        tradeNo = tradeNo.concat( letters.get( 0 ).toString() ).concat( letters.get( 1 ).toString() ).concat( letters.get( 2 ).toString() );

        return tradeNo;
    }

    public static String generateGroupId() {
        return LocalDateTime.now().format( DateTimeFormatter.ofPattern( "yyyyMMddHHmmss" ) );
    }

    public static String getNameFromEmail(String email) {
        int index = email.indexOf( "@" );
        return email.substring( 0, index );
    }

    public static List<String> toStrList(String str) {
        if ( StringUtils.isEmpty( str ) ) {
            return new ArrayList<>();
        }
        return new ArrayList<>( Arrays.asList( str.split( "," ) ) );
    }

    public static List<Integer> toIntegerList(String str) {
        if ( StringUtils.isEmpty( str ) ) {
            return new ArrayList<>();
        }
        return Arrays.stream( str.split( "," ) ).map( Integer::parseInt ).collect( Collectors.toList() );
    }

    public static String intLisToStr(List<Integer> list) {
        if ( list == null || list.isEmpty() ) {
            return "";
        }
        return list.stream().map( String::valueOf ).collect( Collectors.joining( "," ) );
    }

    public static String strListToStr(List<String> list) {
        if ( list == null || list.isEmpty() ) {
            return "";
        }
        return String.join( ",", list );
    }

    public static boolean checkActiveStatus(String status) {
        return Constants.ACTIVE_STATUS.equals( status ) || Constants.INACTIVE_STATUS.equals( status );
    }

    public static String getTimeLabel(String showTime) {
        long seconds = DateUtil.getSecondsBetween( showTime, null );
        // 一个月之前的显示原本的
        if ( seconds > 2592000 ) {
            return showTime;
        } else if ( seconds > 86400 ) {
            return seconds / 86400 + " days ago";
        } else if ( seconds > 3600 ) {
            return seconds / 3600 + " hours ago";
        } else if ( seconds > 60 ) {
            return seconds / 60 + " minutes ago";
        } else {
            return seconds + " seconds ago";
        }
    }

    public static String truncateWithEllipsis(String str, int maxLength) {
        if (str == null || str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength) + "...";
    }

    public static BigDecimal toBigDecimal(Integer value) {
        if ( Objects.isNull( value ) ) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf( value ).divide( BigDecimal.valueOf( 100 ), 2, RoundingMode.HALF_UP );
    }
}