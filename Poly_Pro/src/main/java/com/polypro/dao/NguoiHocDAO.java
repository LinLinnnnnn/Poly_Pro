/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.polypro.dao;

import com.polypro.utils.XJdbc;
import com.polypro.entity.NguoiHoc;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MY LINH
 */
public class NguoiHocDAO extends PolyProDAO<NguoiHoc, String> {

    String INSERT_SQL = " INSERT INTO NGUOIHOC(MANH, HOVATEN, NGAYSINH, GIOITINH, SODIENTHOAI, EMAIL, GHICHU, MANV, NGAYDK) VALUES(?,?,?,?,?,?,?,?,?)";
    String UPDATE_SQL = "UPDATE NGUOIHOC SET HOVATEN = ?, NGAYSINH = ?, GIOITINH = ?, SODIENTHOAI = ?, EMAIL = ?, GHICHU = ?, MANV = ?, NGAYDK = ? WHERE MANH = ?";
    String DELETE_SQL = "DELETE FROM NGUOIHOC WHERE MANH = ?";
    String SELECT_ALL_SQL = "SELECT * FROM NGUOIHOC";
    String SELECT_BY_ID_SQL = "SELECT * FROM NGUOIHOC WHERE MANH = ?";
    String SELECT_BY_Key = "SELECT* FROM NGUOIHOC WHERE HOVATEN LIKE ?";
    String SELECT_NOT_IN_COURSE = "SELECT * FROM NGUOIHOC "
            + "WHERE HOVATEN LIKE ? AND"
            + " MANH NOT IN (SELECT MANH FROM HOCVIEN WHERE MAKH = ?)";

    @Override
    public void insert(NguoiHoc entity) {   
        XJdbc.update(INSERT_SQL,
                entity.getMaNH(),
                entity.getHoTen(),
                entity.getNgaySinh(),
                entity.isGioiTinh(),
                entity.getSoDienThoai(),
                entity.getEmail(),
                entity.getGhiChu(),
                entity.getMaNV(),
                entity.getNgayDK()
        );
    }

    @Override
    public void update(NguoiHoc entity) {
        XJdbc.update(UPDATE_SQL,
                entity.getHoTen(),
                entity.getNgaySinh(),
                entity.isGioiTinh(),
                entity.getSoDienThoai(),
                entity.getEmail(),
                entity.getGhiChu(),
                entity.getMaNV(),
                entity.getNgayDK(),
                entity.getMaNH()
        );

    }

    @Override
    public void delete(String id) {
        XJdbc.update(DELETE_SQL, id);
    }

    @Override
    public NguoiHoc selectByid(String id) {
        List<NguoiHoc> lst = this.selectBySql(SELECT_BY_ID_SQL, id);
        if (lst.isEmpty()) {
            System.out.println("rỗng");
            return null;
        }
        return lst.get(0);
    }

    @Override
    public List<NguoiHoc> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }

    @Override
    protected List<NguoiHoc> selectBySql(String sql, Object... args) {
        List<NguoiHoc> lst = new ArrayList<NguoiHoc>();
        try {
            ResultSet rs = XJdbc.query(sql, args);
            while (rs.next()) {
                NguoiHoc entity = new NguoiHoc();
                entity.setMaNH(rs.getString("MaNH"));
                entity.setHoTen(rs.getString("HoVaTen"));
                entity.setNgaySinh(rs.getDate("NgaySinh"));
                entity.setGioiTinh(rs.getBoolean("GioiTinh"));
                entity.setSoDienThoai(rs.getString("SoDienThoai"));
                entity.setEmail(rs.getString("Email"));
                entity.setGhiChu(rs.getString("GhiChu"));
                entity.setMaNV(rs.getString("MaNV"));
                entity.setNgayDK(rs.getDate("NgayDK"));
                lst.add(entity);
            }
            rs.getStatement().getConnection().close();
            return lst;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public List<NguoiHoc> SearchByKey(String key) {
        return this.selectBySql(SELECT_BY_Key, "%" + key + "%");
    }
    public List<NguoiHoc> selectNotInCourse(int makh, String keyword){
        return this.selectBySql(SELECT_NOT_IN_COURSE, "%"+keyword+"%" ,makh);
    }
}
