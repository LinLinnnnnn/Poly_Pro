
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.polypro.dao;

import com.polypro.utils.XJdbc;
import com.polypro.entity.NhanVien;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MY LINH
 */
public class NhanVienDAO extends PolyProDAO<NhanVien, String> {

    String INSERT_SQL = " INSERT INTO NHANVIEN(MANV, MATKHAU, HOVATEN, VAITRO) VALUES(?,?,?,?)";
    String UPDATE_SQL = "UPDATE NHANVIEN SET MATKHAU = ?, HOVATEN =?, VAITRO = ? WHERE MANV = ?";
    String DELETE_SQL = "DELETE FROM NHANVIEN WHERE MANV = ?";
    String SELECT_ALL_SQL = "SELECT * FROM NHANVIEN";
    String SELECT_BY_ID_SQL = "SELECT* FROM NHANVIEN WHERE MANV= ?";

    @Override
    public void insert(NhanVien entity) {
        XJdbc.update(INSERT_SQL, entity.getMaNV(), entity.getMatKhau(), entity.getHoTen(), entity.isVaiTro());
    }

    @Override
    public void update(NhanVien entity) {
        XJdbc.update(UPDATE_SQL, entity.getMatKhau(), entity.getHoTen(), entity.isVaiTro(), entity.getMaNV());

    }

    @Override
    public void delete(String id) {
        XJdbc.update(DELETE_SQL, id);
    }

    @Override
    public NhanVien selectByid(String id) {
        List<NhanVien> lst = this.selectBySql(SELECT_BY_ID_SQL, id);
        if (lst.isEmpty()) {
            return null;
        }
        return lst.get(0);
    }

    @Override
    public List<NhanVien> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }

    @Override
    protected List<NhanVien> selectBySql(String sql, Object... args) {
        List<NhanVien> lst = new ArrayList<NhanVien>();
        try {
            ResultSet rs = XJdbc.query(sql, args);
            while (rs.next()) {
                NhanVien entity = new NhanVien();
                entity.setMaNV(rs.getString("MaNV"));
                entity.setMatKhau(rs.getString("MatKhau"));
                entity.setHoTen(rs.getString("HoVaTen"));
                entity.setVaiTro(rs.getBoolean("VaiTro"));
                lst.add(entity);
            }
            rs.getStatement().getConnection().close();
            return lst;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
