/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.polypro.ui;

import com.google.zxing.WriterException;
import com.polypro.dao.ChuyenDeDAO;
import com.polypro.dao.KhoaHocDAO;
import com.polypro.entity.ChuyenDe;
import com.polypro.entity.KhoaHoc;
import com.polypro.utils.Auth;
import com.polypro.utils.MsgBox;
import com.polypro.utils.QRCodeUtil;
import com.polypro.utils.Validate;
import com.polypro.utils.XDate;
import com.polypro.utils.XImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author MY LINH
 */
public class KhoaHocJDL extends javax.swing.JDialog {

    java.awt.Frame parent = null;
    public static String qrCodeImage = "";
    int row = - 1;
    KhoaHocDAO khDao = new KhoaHocDAO();

    private DefaultTableModel tblModel = new DefaultTableModel();

    /**
     * Creates new form KhoaHocJDL
     */
    public KhoaHocJDL(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        Auth.checkLogin(btnDelete);
        btnFirst.setIcon(XImage.insertIcon(20, 20, "image//first.png"));
        btnPrevious.setIcon(XImage.insertIcon(20, 20, "image//pre.png"));
        btnNext.setIcon(XImage.insertIcon(20, 20, "image//next.png"));
        btnLast.setIcon(XImage.insertIcon(20, 20, "image//last.png"));
        setLocationRelativeTo(null);
        fillCbo();
        initTable();
        setDisable();
        updateStatus();

    }

    public void initTable() {
        tblModel = (DefaultTableModel) tblDSKH.getModel();
        String columns[] = new String[]{
            "STT", "Mã khoá học", "Thời lượng", "Học phí", "Khai giảng", "Tạo bởi", "Ngày tạo"
        };
        tblModel.setColumnIdentifiers(columns);
    }

    private void fillCbo() {
        ChuyenDeDAO cdDao = new ChuyenDeDAO();
        DefaultComboBoxModel modelCbo = (DefaultComboBoxModel) cboCD.getModel();
        modelCbo.removeAllElements();
        List<ChuyenDe> lst = cdDao.selectAll();
        for (ChuyenDe cd : lst) {
            modelCbo.addElement(cd);
        }
        cboCD.setSelectedIndex(0);

    }

    private void fillCD() {

        tblModel.setRowCount(0);
        try {
            ChuyenDe cd = (ChuyenDe) cboCD.getSelectedItem();
            List<KhoaHoc> lstkh = khDao.selectByChuyenDe(cd.getMaCD());
            for (int i = 0; i < lstkh.size(); i++) {
                KhoaHoc kh = lstkh.get(i);
                Object[] row = {
                    i + 1,
                    kh.getMaKH(),
                    kh.getThoiLuong(),
                    XDate.toString(kh.getNgKhaiGiang(), "dd/MM/yyyy"),
                    kh.getHocPhi(),
                    kh.getMaNV(),
                    XDate.toString(kh.getNgayTao(), "dd/MM/yyyy"),};
                tblModel.addRow(row);

            }

        } catch (Exception e) {
        }
    }

    private KhoaHoc getForm() {
        if (Validate.isTxtEmpty(txtKhaiGiang.getText())) {
            MsgBox.alert(this, "Vui lòng nhập ngày khai giảng!!!");
            return null;
        }
        ChuyenDe cd = (ChuyenDe) cboCD.getSelectedItem();
        KhoaHoc kh = new KhoaHoc();
        kh.setMaCD(cd.getMaCD());
        kh.setMaNV(txtNgayTao.getText());
        kh.setNgayTao(XDate.toDate(txtNgayTao.getText(), "dd/MM/yyyy"));
        kh.setNgKhaiGiang(XDate.toDate(txtKhaiGiang.getText(), "dd/MM/yyyy"));
        kh.setThoiLuong(Integer.parseInt(txtThoiLuong.getText()));
        kh.setHocPhi(Double.parseDouble(txtHocPhi.getText()));
        kh.setMaNV(Auth.user.getMaNV());
        kh.setGhiChu(txtGhiChu.getText());
        return kh;
    }

    private void setForm(KhoaHoc kh) {
        if (kh.getMaCD() == null) {
            ChuyenDe cd = (ChuyenDe) cboCD.getSelectedItem();
            txtThoiLuong.setText(String.valueOf(cd.getThoiLuong()));
            txtHocPhi.setText(String.valueOf(cd.getHocPhi()));
            txtNguoiTao.setText(Auth.user.getMaNV());
        } else {
            txtThoiLuong.setText(String.valueOf(kh.getThoiLuong()));
            txtHocPhi.setText(String.valueOf(kh.getHocPhi()));
            txtNguoiTao.setText(Auth.user.getMaNV());
        }
        if (kh.getNgKhaiGiang() != null) {
            txtKhaiGiang.setText(XDate.toString(kh.getNgKhaiGiang(), "dd/MM/yyyy"));
        } else {
            txtKhaiGiang.setText(null);
        }
        if (kh.getNgayTao() != null) {
            txtNgayTao.setText(XDate.toString(kh.getNgayTao(), "dd/MM/yyyy"));
        } else {
            txtNgayTao.setText(null);
        }
        txtGhiChu.setText(kh.getGhiChu());

    }

    public void edit() {
        try {
            int maKH = Integer.parseInt(tblDSKH.getValueAt(row, 1).toString());
            KhoaHoc kh = khDao.selectByid(maKH);
            setForm(kh);
            updateStatus();

        } catch (Exception e) {
        }

    }

    private void chooseCD() {
        ChuyenDe cd = (ChuyenDe) cboCD.getSelectedItem();
        txtNameChuyenDe.setText(cd.getTenCD());
        txtThoiLuong.setText(String.valueOf(cd.getThoiLuong()));
        txtHocPhi.setText(String.valueOf(cd.getHocPhi()));
        txtGhiChu.setText(cd.getTenCD());
        txtNguoiTao.setText(Auth.user.getMaNV());
        txtNgayTao.setText(XDate.toString(new Date(), "dd/MM/yyyy"));
        txtKhaiGiang.setText("");
//        txtKhaiGiang.setText(XDate.toString(XDate.addDays(XDate.toDate(txtNgayTao.getText(),"dd/MM/yyyy"), 30), "dd/MM/yyyy"));

        this.fillCD();
        this.updateStatus();
        this.row = -1;
        tabKH.setSelectedIndex(1);
    }

    public void setDisable() {
        txtNameChuyenDe.setEditable(false);
        txtThoiLuong.setEditable(false);
        txtHocPhi.setEditable(false);
        txtNguoiTao.setEditable(false);
        txtNgayTao.setEditable(false);

    }
//   

    private void updateStatus() {
        boolean edit = (this.row >= 0);
        boolean first = (this.row == 0);
        boolean last = (this.row == tblDSKH.getRowCount() - 1);
        txtNameChuyenDe.setEditable(!edit);
        btnAdd.setEnabled(!edit);
        btnDelete.setEnabled(edit);
        btnUpdate.setEnabled(edit);
        btnFirst.setEnabled(edit && !first);
        btnLast.setEnabled(edit && !last);

    }

    public void clearForm() {
        setForm(new KhoaHoc());
        chooseCD();
        updateStatus();
    }

    public void insert() {
        KhoaHoc kh = getForm();
        try {
            if (kh != null) {
                khDao.insert(kh);
                fillCD();
                clearForm();
                MsgBox.alert(this, "Thêm thành công");
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi thêm mới");
        }
    }

    public void delete() {
        int maKH = (int) tblDSKH.getValueAt(row, 0);
        KhoaHoc kh = khDao.selectByid(maKH);
        try {
            if (Auth.isManager()) {
                if (MsgBox.comfirm(this, "Bạn muốn xoá khoá học này ?")) {
                    khDao.delete(maKH);
                    fillCD();
                    clearForm();
                    MsgBox.alert(this, "Xoá thành công");
                }
            } else {
                MsgBox.alert(this, "Nhân viên không có quyền xoá");
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi khi xoá");
        }
    }

    public void update() {
        int maKH = (int) tblDSKH.getValueAt(row, 0);
        KhoaHoc kh = khDao.selectByid(maKH);
        ChuyenDe cd = (ChuyenDe) cboCD.getSelectedItem();
        kh.setMaCD(cd.getMaCD());
        kh.setMaNV(txtNgayTao.getText());
        kh.setNgayTao(XDate.toDate(txtNgayTao.getText(), "dd/MM/yyyy"));
        kh.setHocPhi(Double.parseDouble(txtHocPhi.getText()));
        if (Validate.isTxtEmpty(txtKhaiGiang.getText())) {
            MsgBox.alert(this, "Vui lòng nhập ngày khai giảng!!!");
            kh.setNgKhaiGiang(null);
        } else {
            kh.setNgKhaiGiang(XDate.toDate(txtKhaiGiang.getText(), "dd/MM/yyyy"));

        }
        kh.setThoiLuong(Integer.parseInt(txtThoiLuong.getText()));
        kh.setMaNV(Auth.user.getMaNV());
        kh.setGhiChu(txtGhiChu.getText());
        System.out.println(maKH);
        try {
            if (kh.getNgKhaiGiang() != null) {

                if (MsgBox.comfirm(this, "Bạn muốn cập nhật khoá học này ?")) {
                    khDao.update(kh);
                    fillCD();
                    clearForm();
                    MsgBox.alert(this, "Cập nhật thành công");
                }
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi cập nhật");
        }
    }

    void first() {
        this.row = 0;
        this.edit();
    }

    private void last() {
        this.row = tblDSKH.getRowCount() - 1;
        this.edit();
    }

    void prev() {
        this.row--;
        if (this.row >= 0) {
            this.edit();
        } else if (this.row < 0) {
            this.row = tblDSKH.getRowCount() - 1;
            this.edit();
        }
    }

    private void next() {
        this.row++;
        System.out.println(this.row);
        if (this.row <= tblDSKH.getRowCount() - 1) {
            this.edit();
        } else if (this.row > tblDSKH.getRowCount() - 1) {
            this.row = 0;
            this.edit();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tabKH = new javax.swing.JTabbedPane();
        tabCapnhat = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtGhiChu = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        btnFirst = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnImageQR = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtHocPhi = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtNameChuyenDe = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtNguoiTao = new javax.swing.JTextField();
        txtNgayTao = new javax.swing.JTextField();
        txtKhaiGiang = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtThoiLuong = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        tabDanhsach = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDSKH = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        cboCD = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Poly Pro");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 102));
        jLabel1.setText("QUẢN LÝ KHOÁ HỌC");

        tabKH.setForeground(new java.awt.Color(0, 102, 102));

        txtGhiChu.setColumns(20);
        txtGhiChu.setRows(5);
        jScrollPane2.setViewportView(txtGhiChu);

        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
            }
        });

        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(55, Short.MAX_VALUE)
                .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLast, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(btnFirst, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPrevious, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 102, 102));
        jLabel10.setText("Ghi chú:");

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnAdd.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(0, 102, 102));
        btnAdd.setText("Thêm");
        btnAdd.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnDelete.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(0, 102, 102));
        btnDelete.setText("Xoá");
        btnDelete.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnUpdate.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(0, 102, 102));
        btnUpdate.setText("Sửa");
        btnUpdate.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnClear.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnClear.setForeground(new java.awt.Color(0, 102, 102));
        btnClear.setText("Mới");
        btnClear.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnImageQR.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnImageQR.setForeground(new java.awt.Color(0, 102, 102));
        btnImageQR.setText("QR");
        btnImageQR.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnImageQR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImageQRActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                    .addComponent(btnClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnImageQR, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnImageQR)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 102, 102));
        jLabel7.setText("Chuyên đề:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 102, 102));
        jLabel8.setText("Học phí:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 102, 102));
        jLabel4.setText("Ngày tạo:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 102));
        jLabel3.setText("Thời lượng (Giờ):");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 102));
        jLabel2.setText("Khai giảng:");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 102, 102));
        jLabel9.setText("Người tạo:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8)
                            .addComponent(txtHocPhi)
                            .addComponent(jLabel9)
                            .addComponent(txtNguoiTao)
                            .addComponent(txtNameChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel4))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(5, 5, 5)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel2))))
                                .addContainerGap(245, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtThoiLuong)
                                    .addComponent(txtNgayTao)
                                    .addComponent(txtKhaiGiang)))))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKhaiGiang, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNameChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtThoiLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHocPhi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNguoiTao, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout tabCapnhatLayout = new javax.swing.GroupLayout(tabCapnhat);
        tabCapnhat.setLayout(tabCapnhatLayout);
        tabCapnhatLayout.setHorizontalGroup(
            tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabCapnhatLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabCapnhatLayout.createSequentialGroup()
                        .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26))
                    .addGroup(tabCapnhatLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(tabCapnhatLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tabCapnhatLayout.setVerticalGroup(
            tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabCapnhatLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        tabKH.addTab("CẬP NHẬT", tabCapnhat);

        tblDSKH.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã khóa học", "Thời lượng", "Học phí", "Khai giảng", "Tạo bởi", "Ngày tạo"
            }
        ));
        tblDSKH.setSelectionBackground(new java.awt.Color(0, 153, 153));
        tblDSKH.setSelectionForeground(new java.awt.Color(255, 255, 255));
        tblDSKH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDSKHMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDSKHMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblDSKH);

        javax.swing.GroupLayout tabDanhsachLayout = new javax.swing.GroupLayout(tabDanhsach);
        tabDanhsach.setLayout(tabDanhsachLayout);
        tabDanhsachLayout.setHorizontalGroup(
            tabDanhsachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1055, Short.MAX_VALUE)
        );
        tabDanhsachLayout.setVerticalGroup(
            tabDanhsachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
        );

        tabKH.addTab("DANH SÁCH", tabDanhsach);

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 102, 102));
        jLabel6.setText("Chuyên đề:");

        cboCD.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        cboCD.setForeground(new java.awt.Color(0, 102, 102));
        cboCD.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboCD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboCD, 0, 870, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cboCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(tabKH, javax.swing.GroupLayout.PREFERRED_SIZE, 1055, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(389, 389, 389)
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabKH, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        last();        // TODO add your handling code here:
    }//GEN-LAST:event_btnLastActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearForm();
    }//GEN-LAST:event_btnClearActionPerformed

    private void cboCDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCDActionPerformed
        // TODO add your handling code here:
        int i = cboCD.getSelectedIndex();
        if (i > -1) {
            chooseCD();
        }
    }//GEN-LAST:event_cboCDActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        // TODO add your handling code here:
        first();
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        prev();        // TODO add your handling code here:
    }//GEN-LAST:event_btnPreviousActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        next();        // TODO add your handling code here:
    }//GEN-LAST:event_btnNextActionPerformed

    private void tblDSKHMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDSKHMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDSKHMouseClicked

    private void tblDSKHMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDSKHMousePressed
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            row = tblDSKH.rowAtPoint(evt.getPoint());
            edit();
            tabKH.setSelectedIndex(0);
        }
    }//GEN-LAST:event_tblDSKHMousePressed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        insert();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        delete();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        update();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnImageQRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImageQRActionPerformed
        // TODO add your handling code here:
        try {
            ChuyenDe chuyenDe = (ChuyenDe) cboCD.getSelectedItem();
            Integer makh = (Integer) tblDSKH.getValueAt(this.row, 1);
            KhoaHoc kh = khDao.selectByid(makh);
            String qrCodeText = "Chuyen De: " + chuyenDe.getMaCD() + " Khoa hoc khai giang: " + kh.getNgKhaiGiang();
            String filePath = kh.getMaKH() + "_" + chuyenDe.getMaCD() + "_" + kh.getNgKhaiGiang() + ".jpg";
            File destination = new File("QR", filePath);

            filePath = Paths.get(destination.getAbsolutePath()).toString();
            int size = 200;
            String fileType = "jpg";
            File qrFile = new File(filePath);
            QRCodeUtil.createQRImage(qrFile, qrCodeText, size, fileType);
            this.qrCodeImage = filePath;
            new QRCodeKHJDL(this.parent, true).setVisible(true);
        } catch (WriterException ex) {
        } catch (IOException ex) {
        }
    }//GEN-LAST:event_btnImageQRActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(KhoaHocJDL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(KhoaHocJDL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(KhoaHocJDL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(KhoaHocJDL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                KhoaHocJDL dialog = new KhoaHocJDL(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnImageQR;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cboCD;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel tabCapnhat;
    private javax.swing.JPanel tabDanhsach;
    private javax.swing.JTabbedPane tabKH;
    private javax.swing.JTable tblDSKH;
    private javax.swing.JTextArea txtGhiChu;
    private javax.swing.JTextField txtHocPhi;
    private javax.swing.JTextField txtKhaiGiang;
    private javax.swing.JTextField txtNameChuyenDe;
    private javax.swing.JTextField txtNgayTao;
    private javax.swing.JTextField txtNguoiTao;
    private javax.swing.JTextField txtThoiLuong;
    // End of variables declaration//GEN-END:variables
}
