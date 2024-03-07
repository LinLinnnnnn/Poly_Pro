/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.polypro.entity;

/**
 *
 * @author MY LINH
 */
public class ChuyenDe {

    private String maCD;
    private String tenCD;
    private Double hocPhi;
    private int thoiLuong;
    private String hinhLoGo;
    private String moTa;

    public String getMaCD() {
        return maCD;
    }

    public void setMaCD(String maCD) {
        this.maCD = maCD;
    }

    public String getTenCD() {
        return tenCD;
    }

    public void setTenCD(String tenCD) {
        this.tenCD = tenCD;
    }

    public Double getHocPhi() {
        return hocPhi;
    }

    public void setHocPhi(Double hocPhi) {
        this.hocPhi = hocPhi;
    }

    public int getThoiLuong() {
        return thoiLuong;
    }

    public void setThoiLuong(int thoiLuong) {
        this.thoiLuong = thoiLuong;
    }

    public String getHinhLoGo() {
        return hinhLoGo;
    }

    public void setHinhLoGo(String hinhLoGo) {
        this.hinhLoGo = hinhLoGo;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public ChuyenDe() {
    }

    public ChuyenDe(String maCD, String tenCD, Double hocPhi, int thoiLuong, String hinhLoGo, String moTa) {
        this.maCD = maCD;
        this.tenCD = tenCD;
        this.hocPhi = hocPhi;
        this.thoiLuong = thoiLuong;
        this.hinhLoGo = hinhLoGo;
        this.moTa = moTa;
    }

    @Override
    public String toString() {
        return this.tenCD;
    }

    @Override
    public boolean equals(Object obj) {
        ChuyenDe other = (ChuyenDe) obj;
        return other.getMaCD().equals(this.maCD);
    }

}
