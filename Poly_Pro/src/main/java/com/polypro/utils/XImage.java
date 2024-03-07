/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.polypro.utils;

import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.ImageIcon;

/**
 *
 * @author MY LINH
 */
public class XImage {

    public static ImageIcon getAppIcon() {
        URL url = XImage.class.getResource("/img/fpt.png");
        return new ImageIcon(url);
    }

    public static ImageIcon insertIcon(int width, int height, String link) {
        try {
            ImageIcon ico = new ImageIcon(link);
            Image img = ico.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(img);
            return icon;
        } catch (Exception e) {
            return null;
        }

    }

    public static boolean save(File src) {
        File dst = new File("image", src.getName());
        if (!dst.getParentFile().exists()) {
            dst.getParentFile().mkdirs();
        }
        try {
            Path from = Paths.get(src.getAbsolutePath());
            Path to = Paths.get(dst.getAbsolutePath());
            Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static ImageIcon read(String fileName) {
        File path = new File("image", fileName);
        return new ImageIcon(path.getAbsolutePath());

    }

    public static Image ConvertIcon(String link, Component cp) {
        ImageIcon ico = new ImageIcon(link);
        Image img = ico.getImage().getScaledInstance(cp.getWidth(), cp.getHeight(), Image.SCALE_SMOOTH);
        return img;
    }
        public static File saveExel(File src){
        File dst = new File("Excel", src.getName());
        if(!dst.getParentFile().exists()){
            dst.getParentFile().mkdirs();
        }
        try {
            Path from = Paths.get(src.getAbsolutePath());
            Path to = Paths.get(dst.getAbsolutePath());
            Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
            return dst;
        } 
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
        
         public static File saveQR(File src){
        File dst = new File("QR", src.getName());
        if(!dst.getParentFile().exists()){
            dst.getParentFile().mkdirs();
        }
        try {
            Path from = Paths.get(src.getAbsolutePath());
            Path to = Paths.get(dst.getAbsolutePath());
            Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
            return dst;
        } 
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
