package com.example.time2meet.data;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

public class Helper {
    private static Helper instance;
    private Helper(){}

    public static Helper getInstance() {
        if (instance == null) {
            instance = new Helper();
        }
        return instance;
    }

    public Boolean isValidDate(String value) {
        LocalDateTime ldt = null;
        String format = "dd/MM/yyyy";
        Locale locale = Locale.ENGLISH;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, locale);

        try {
            ldt = LocalDateTime.parse(value, formatter);
            String result = ldt.format(formatter);
            return result.equals(value);
        } catch (DateTimeParseException e) {
            try {
                LocalDate ld = LocalDate.parse(value, formatter);
                String result = ld.format(formatter);
                return result.equals(value);
            } catch (DateTimeParseException exp) {
                try {
                    LocalTime lt = LocalTime.parse(value, formatter);
                    String result = lt.format(formatter);
                    return result.equals(value);
                } catch (DateTimeParseException e2) {
                    e2.printStackTrace();
                }
            }
        }

        return false;
    }

    public static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String hashPassword(String password) {
        String algorithm = new String("SHA-256");

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        final byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedHash);
    }

    public Date stringToDate(String s) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date;
        try {
            date = format.parse(s);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer compareDate(String start, String end) {
        Date startDate = stringToDate(start);
        Date endDate = stringToDate(end);
        return startDate.compareTo(endDate);
    }
}