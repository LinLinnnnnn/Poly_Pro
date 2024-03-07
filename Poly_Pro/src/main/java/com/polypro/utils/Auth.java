/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.polypro.utils;

import com.polypro.entity.NhanVien;
import java.awt.Component;

/**
 *
 * @author MY LINH
 */
public class Auth {
    public static NhanVien user = null;
    public static void clear(){
        Auth.user =  null;
    }
    public static boolean isLogin(){
        return Auth.user != null;
    }
    public static boolean isManager(){
        return Auth.isLogin() && user.isVaiTro();
    }
      public static void checkLogin(Component btnXoa){
        if(!Auth.isLogin()){
            System.exit(0);
        }
        else if(btnXoa == null){
            
        } else if(!isManager()){
            btnXoa.setEnabled(false);
        }
    }
}
