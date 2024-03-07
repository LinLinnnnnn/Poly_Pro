/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.polypro.entity;

import com.polypro.utils.XDate;
import java.util.Date;

/**
 *
 * @author MY LINH
 */
public class KhoaHoc {

    private int maKH;
    private String maCD;
    private String maNV;
    private double hocPhi;
    private int thoiLuong;
    private Date ngKhaiGiang;
    private String ghiChu;
    private Date ngayTao;

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public String getMaCD() {
        return maCD;
    }

    public void setMaCD(String maCD) {
        this.maCD = maCD;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public double getHocPhi() {
        return hocPhi;
    }

    public void setHocPhi(double hocPhi) {
        this.hocPhi = hocPhi;
    }

    public int getThoiLuong() {
        return thoiLuong;
    }

    public void setThoiLuong(int thoiLuong) {
        this.thoiLuong = thoiLuong;
    }

    public Date getNgKhaiGiang() {
        return ngKhaiGiang;
    }

    public void setNgKhaiGiang(Date ngKhaiGiang) {
        this.ngKhaiGiang = ngKhaiGiang;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public KhoaHoc() {
    }

    public KhoaHoc(int maKH, String maCD, String maNV, double hocPhi, int thoiLuong, Date ngKhaiGiang, String ghiChu, Date ngayTao) {
        this.maKH = maKH;
        this.maCD = maCD;
        this.maNV = maNV;
        this.hocPhi = hocPhi;
        this.thoiLuong = thoiLuong;
        this.ngKhaiGiang = ngKhaiGiang;
        this.ghiChu = ghiChu;
        this.ngayTao = ngayTao;
    }
     @Override
    public String toString() {
        return this.maCD + "("+ XDate.toString(this.ngKhaiGiang,"dd/MM/yyyy")+")";
    }

}
