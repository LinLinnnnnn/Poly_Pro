/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.polypro.dao;

import com.polypro.utils.XJdbc;
import com.polypro.entity.ChuyenDe;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MY LINH
 */
public class ChuyenDeDAO extends PolyProDAO<ChuyenDe, String> {

    String INSERT_SQL = " INSERT INTO CHUYENDE(MACD, TENCD, HOCPHI, THOILUONG, HINHLOGO, MOTA) VALUES(?,?,?,?,?,?)";
    String UPDATE_SQL = "UPDATE CHUYENDE SET TENCD = ?, HOCPHI =?, THOILUONG = ?, HINHLOGO= ?, MOTA = ? WHERE MACD = ?";
    String DELETE_SQL = "DELETE FROM CHUYENDE WHERE MACD = ?";
    String SELECT_ALL_SQL = "SELECT * FROM CHUYENDE";
    String SELECT_BY_ID_SQL = "SELECT* FROM CHUYENDE WHERE MACD= ?";

    @Override
    public void insert(ChuyenDe entity) {
        XJdbc.update(INSERT_SQL,
                entity.getMaCD(),
                entity.getTenCD(),
                entity.getHocPhi(),
                entity.getThoiLuong(),
                entity.getHinhLoGo(),
                entity.getMoTa());
    }

    @Override
    public void update(ChuyenDe entity) {
        XJdbc.update(UPDATE_SQL,
                entity.getTenCD(),
                entity.getHocPhi(),
                entity.getThoiLuong(),
                entity.getHinhLoGo(),
                entity.getMoTa(),
                entity.getMaCD());

    }

    @Override
    public void delete(String id) {
        XJdbc.update(DELETE_SQL, id);
    }

    @Override
    public ChuyenDe selectByid(String id) {
        List<ChuyenDe> lst = this.selectBySql(SELECT_BY_ID_SQL, id);
        if (lst.isEmpty()) {
            return null;
        }
        return lst.get(0);
    }

    @Override
    public List<ChuyenDe> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }

    @Override
    protected List<ChuyenDe> selectBySql(String sql, Object... args) {
        List<ChuyenDe> lst = new ArrayList<ChuyenDe>();
        try {
            ResultSet rs = XJdbc.query(sql, args);
            while (rs.next()) {
                ChuyenDe entity = new ChuyenDe();
                entity.setMaCD(rs.getString("MaCD"));
                entity.setTenCD(rs.getString("TenCD"));
                entity.setHocPhi(rs.getDouble("HocPhi"));
                entity.setThoiLuong(rs.getInt("ThoiLuong"));
                entity.setHinhLoGo(rs.getString("HinhLogo"));
                entity.setMoTa(rs.getString("MoTa"));
                lst.add(entity);
            }
            rs.getStatement().getConnection().close();
            return lst;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
