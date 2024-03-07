/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.polypro.entity;

import java.util.Date;

/**
 *
 * @author MY LINH
 */
public class NguoiHoc {

    private String maNH;
    private String hoTen;
    private Date ngaySinh;
    private boolean gioiTinh;
    private String soDienThoai;
    private String email;
    private String ghiChu;
    private String maNV;
    private Date NgayDK;

    public String getMaNH() {
        return maNH;
    }

    public void setMaNH(String maNH) {
        this.maNH = maNH;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public Date getNgayDK() {
        return NgayDK;
    }

    public void setNgayDK(Date NgayDK) {
        this.NgayDK = NgayDK;
    }

    public NguoiHoc(String maNH, String hoTen, Date ngaySinh, boolean gioiTinh, String soDienThoai, String email, String ghiChu, String maNV, Date NgayDK) {
        this.maNH = maNH;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.ghiChu = ghiChu;
        this.maNV = maNV;
        this.NgayDK = NgayDK;
    }

    public NguoiHoc() {
    }

}
