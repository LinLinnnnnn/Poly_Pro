/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.polypro.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MY LINH
 */
public class ThongKeDAO {

    private List<Object[]> getListOfArray(String sql, String[] cols, Object... args) {
        try {
            List<Object[]> lst = new ArrayList<>();
            ResultSet rs = com.polypro.utils.XJdbc.query(sql, args);
            while(rs.next()){
                Object[] vals = new Object[cols.length];
                for(int i = 0; i< cols.length; i++){
                    vals[i] = rs.getObject(cols[i]);
                }
                lst.add(vals);
            }
            rs.getStatement().getConnection().close();
            return lst;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Object[]> getBangDiem(Integer maKH) {
        String sql = "{CALL SP_BANGDIEM(?)}";
        String[] cols = {"MaNH", "HoVaTen", "DiemTB"};
        return this.getListOfArray(sql, cols, maKH);

    }

    public List<Object[]> getLuongNguoiHoc() {
        String sql = "{CALL SP_LuongNguoiHoc}";
        String[] cols = {"Nam", "SoLuong", "DauTien", "CuoiCung"};
        return this.getListOfArray(sql, cols);
    }

    public List<Object[]> getDiemChuyenDe() {
        String sql = "{CALL SP_DiemChuyenDe}";
        String[] cols = {"ChuyenDe", "SoHV", "ThapNhat", "CaoNhat", "TrungBinh"};
        return this.getListOfArray(sql, cols);

    }

    public List<Object[]> getDoanhThu(int nam) {
        String sql = "{CALL SP_DoanhThu(?)}";
        String[] cols = {"ChuyenDe", "SoKH", "SoHV", "DoanhThu", "ThapNhat", "CaoNhat", "TrungBinh"};
        return this.getListOfArray(sql, cols, nam);
    }
}
