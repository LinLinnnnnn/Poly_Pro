/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.polypro.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author MY LINH
 */
public class Validate {

    public static boolean isValidPhoneNumber(String phoneNumber) {
        // Biểu thức chính quy cho số điện thoại Việt Nam
        String regex = "^(\\+84|0)[1-9]\\d{8,9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean isValidEmail(String email) {
        // Biểu thức chính quy cho địa chỉ email hợp lệ với ít nhất một ký tự trước @
        String regex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+)\\w+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public static boolean isOver18(Date dob) {
        Date currentDate = new Date();

        // Sử dụng millisecond để tính tuổi
        long millisecondsPerYear = 1000L * 60 * 60 * 24 * 365;
        long ageInMilliseconds = currentDate.getTime() - dob.getTime();

        int age = (int) (ageInMilliseconds / millisecondsPerYear);

        return age >= 18;
    }

    public static boolean isValidID_7(String id) {
        return id.length() <= 7;
    }

    public static int calculateAge(Date dob) {
        Date currentDate = new Date();
        long millisecondsPerYear = 1000L * 60 * 60 * 24 * 365;
        long ageInMilliseconds = currentDate.getTime() - dob.getTime();
        int age = (int) (ageInMilliseconds / millisecondsPerYear);
        return age;
    }

    public static boolean isValidScore(double score) {
        return score >= 0 && score <= 10;
    }

    public static boolean isValidRangeNumber(double n, double min, double max) {
        return n >= min && n <= max;
    }

    public static boolean isTxtEmpty(String txt) {
        return txt.equals("");
    }

    public static boolean isValidPassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static String hashPassword(String password) {
        String salt = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        return salt;
    }

    public static boolean verifyPassword(String password, String saltedHashedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), saltedHashedPassword);
        return result.verified;
    }
}
