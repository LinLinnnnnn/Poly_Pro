/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.polypro.dao;

import com.polypro.utils.XJdbc;
import com.polypro.entity.HocVien;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MY LINH
 */
public class HocVienDAO extends PolyProDAO<HocVien, Integer> {

    String INSERT_SQL = "INSERT INTO HOCVIEN(MAKH, MANH, DIEMTB) VALUES(?,?,?)";
    String UPDATE_SQL = "UPDATE HOCVIEN SET MAKH = ?, MANH =?, DIEMTB = ? WHERE MAHV = ?";
    String DELETE_SQL = "DELETE FROM HOCVIEN WHERE MAHV = ?";
    String SELECT_ALL_SQL = "SELECT * FROM HOCVIEN";
    String SELECT_BY_ID_SQL = "SELECT* FROM HOCVIEN WHERE MAHV= ?";
    String SELECT_BY_KHOA_HOC = "SELECT * FROM HOCVIEN WHERE MAKH = ?";

    @Override
    public void insert(HocVien entity) {
        XJdbc.update(INSERT_SQL, entity.getMaKH(), entity.getMaNH(), entity.getDiemTB());
    }

    @Override
    public void update(HocVien entity) {
        XJdbc.update(UPDATE_SQL, entity.getMaKH(), entity.getMaNH(), entity.getDiemTB(), entity.getMaHV());

    }

    @Override
    public List<HocVien> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }

    @Override
    protected List<HocVien> selectBySql(String sql, Object... args) {
        List<HocVien> lst = new ArrayList<HocVien>();
        try {
            ResultSet rs = XJdbc.query(sql, args);
            while (rs.next()) {
                HocVien entity = new HocVien();
                entity.setMaHV(rs.getInt("MaHV"));
                entity.setMaKH(rs.getInt("MaKH"));
                entity.setMaNH(rs.getString("MaNH"));
                entity.setDiemTB(rs.getDouble("DiemTB"));
                lst.add(entity);
            }
            rs.getStatement().getConnection().close();
            return lst;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public List<HocVien> selectByKhoaHoc(int makh) {
        return this.selectBySql(SELECT_BY_KHOA_HOC, makh);
    }

    @Override
    public void delete(Integer id) {
        XJdbc.update(DELETE_SQL, id);

    }

    @Override
    public HocVien selectByid(Integer id) {
        List<HocVien> lst = this.selectBySql(SELECT_BY_ID_SQL, id);
        if (lst.isEmpty()) {
            return null;
        }
        return lst.get(0);
    }

}
