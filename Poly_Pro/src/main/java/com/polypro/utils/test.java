/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.polypro.utils;

import java.security.SecureRandom;

/**
 *
 * @author MY LINH
 */
public class test {

    public static void main(String[] args) {
        String generatedPassword = generateRandomPassword();
        System.out.println("Generated Password: " + generatedPassword);
    }

    public static String generateRandomPassword() {
        // Định dạng mật khẩu: ít nhất một chữ cái thường, một chữ cái hoa, và một chữ số
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$";

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        while (true) {
            password.setLength(0); // Xóa mật khẩu trước đó

            // Tạo mật khẩu ngẫu nhiên với độ dài mong muốn
            for (int i = 0; i < 8; i++) {
                int randomChar = random.nextInt(62); // Sử dụng 62 ký tự in ASCII
                char baseChar = (randomChar < 26) ? 'A' : (randomChar < 52) ? 'a' : '0';
                password.append((char) (baseChar + randomChar % 26));
            }

            // Kiểm tra xem mật khẩu có đúng định dạng hay không
            if (password.toString().matches(passwordRegex)) {
                break; // Nếu đúng định dạng, thoát khỏi vòng lặp
            }
        }

        return password.toString();
    }

}
