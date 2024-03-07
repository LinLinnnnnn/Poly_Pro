/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.polypro.dao;

import com.polypro.utils.XJdbc;
import com.polypro.entity.KhoaHoc;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MY LINH
 */
public class KhoaHocDAO extends PolyProDAO<KhoaHoc, Integer> {

    String INSERT_SQL = " INSERT INTO KHOAHOC (MACD, MANV, HOCPHI, THOILUONG, NGKHAIGIANG, GHICHU, NGAYTAO) VALUES(?,?,?,?,?,?,?)";
    String UPDATE_SQL = "UPDATE KHOAHOC SET MACD = ?, MANV =?, HOCPHI = ?, THOILUONG = ?, NGKHAIGIANG = ?, GHICHU = ?, NGAYTAO = ? WHERE MAKH = ?";
    String DELETE_SQL = "DELETE FROM KHOAHOC WHERE MAKH = ?";
    String SELECT_ALL_SQL = "SELECT * FROM KHOAHOC";
    String SELECT_BY_ID_SQL = "SELECT* FROM KHOAHOC WHERE MAKH= ?";
    String SELECT_BY_CHUYENDE = "SELECT* FROM KHOAHOC WHERE MACD= ?";
    String SELECT_YEARS = "SELECT DISTINCT YEAR(NGKHAIGIANG)  AS YEARS FROM KHOAHOC ORDER BY YEARS DESC";

    @Override
    public void insert(KhoaHoc entity) {
        XJdbc.update(INSERT_SQL,
                entity.getMaCD(),
                entity.getMaNV(),
                entity.getHocPhi(),
                entity.getThoiLuong(),
                entity.getNgKhaiGiang(),
                entity.getGhiChu(),
                entity.getNgayTao()
        );
    }

    @Override
    public void update(KhoaHoc entity) {
        XJdbc.update(UPDATE_SQL,
                entity.getMaCD(),
                entity.getMaNV(),
                entity.getHocPhi(),
                entity.getThoiLuong(),
                entity.getNgKhaiGiang(),
                entity.getGhiChu(),
                entity.getNgayTao(),
                entity.getMaKH());

    }

    @Override
    public void delete(Integer id) {
        XJdbc.update(DELETE_SQL, id);
    }

    @Override
    public KhoaHoc selectByid(Integer id) {
        List<KhoaHoc> lst = this.selectBySql(SELECT_BY_ID_SQL, id);
        if (lst.isEmpty()) {
            return null;
        }
        return lst.get(0);
    }

    @Override
    public List<KhoaHoc> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }

    @Override
    protected List<KhoaHoc> selectBySql(String sql, Object... args) {
        List<KhoaHoc> lst = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.query(sql, args);
            while (rs.next()) {
                KhoaHoc entity = new KhoaHoc();
                entity.setMaKH(rs.getInt("MaKH"));
                entity.setMaCD(rs.getString("MaCD"));
                entity.setMaNV(rs.getString("MaNV"));
                entity.setHocPhi(rs.getDouble("HocPhi"));
                entity.setThoiLuong(rs.getInt("ThoiLuong"));
                entity.setNgKhaiGiang(rs.getDate("NgKhaiGiang"));
                entity.setGhiChu(rs.getString("GhiChu"));
                entity.setNgayTao(rs.getDate("NgayTao"));
                lst.add(entity);
            }
            rs.getStatement().getConnection().close();
            return lst;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public List<KhoaHoc> selectByChuyenDe(String macd) {
        return this.selectBySql(SELECT_BY_CHUYENDE, macd);
    }

    public List<Integer> selectYears() {
        List<Integer> lst = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.query(SELECT_YEARS);
            while (rs.next()) {
                lst.add(rs.getInt(1));
            }
            rs.getStatement().getConnection().close();
            return lst;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);

        }
    }

}
