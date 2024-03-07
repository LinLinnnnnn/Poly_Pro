/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.polypro.ui;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.google.zxing.WriterException;
import com.polypro.entity.NhanVien;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import com.polypro.dao.NhanVienDAO;
import com.polypro.utils.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 *
 * @author MY LINH
 */
public class NhanVienJDL extends javax.swing.JDialog {

    public static String qrCodeImage = "";
    java.awt.Frame parent = null;
    int row = -1;
    private DefaultTableModel tblModel = new DefaultTableModel();
    NhanVienDAO dao = new NhanVienDAO();

    /**
     * Creates new form NhanVienJDL
     *
     * @param parent
     * @param modal
     */
    public NhanVienJDL(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        btnFirst.setIcon(XImage.insertIcon(20, 20, "image//first.png"));
        btnPrevious.setIcon(XImage.insertIcon(20, 20, "image//pre.png"));
        btnNext.setIcon(XImage.insertIcon(20, 20, "image//next.png"));
        btnLast.setIcon(XImage.insertIcon(20, 20, "image//last.png"));
        setLocationRelativeTo(null);
        initTable();
        fillToTable();
        this.row = 0;
        this.updateStatus();
        
        edit();
        Auth.checkLogin(btnDelete);

    }

    private void initTable() {
        tblModel = (DefaultTableModel) tblDS.getModel();
        String columns[] = new String[]{
            "STT", "Mã nhân viên", "Mật khẩu", "Họ và tên", "Vai trò"
        };
        tblModel.setColumnIdentifiers(columns);
    }

    private void fillToTable() {
        try {
            List<NhanVien> lst = dao.selectAll();
            System.out.println(lst.size());
            tblModel.setRowCount(0);
            for (int i = 0; i < lst.size(); i++) {
                NhanVien nv = lst.get(i);
                Object[] rowNV = {
                    i + 1,
                    nv.getMaNV(),
                    "********",
                    nv.getHoTen(),
                    nv.isVaiTro() ? "Trưởng Phòng" : "Nhân Viên"
                };
                tblModel.addRow(rowNV);
            }
            tblModel.fireTableDataChanged();
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
        }
    }

    private void updateStatus() {
        boolean edit = (this.row >= 0);
        boolean first = (this.row == 0);
        boolean last = (this.row == tblDS.getRowCount() - 1);
        txtID.setEditable(!edit);
        btnAdd.setEnabled(!edit);
        btnDelete.setEnabled(edit);
        btnUpdate.setEnabled(edit);
        btnFirst.setEnabled(edit && !first);
        btnLast.setEnabled(edit && !last);

    }

    private void setForm(NhanVien nv) {
        try {
            txtID.setText(nv.getMaNV());
            txtName.setText(nv.getHoTen());
            txtPassword.setText(nv.getMatKhau());
            txtPasswordAgain.setText(nv.getMatKhau());
            rdoTP.setSelected(nv.isVaiTro());
            rdoNV.setSelected(!nv.isVaiTro());
        } catch (Exception e) {
        }

    }

    private NhanVien getForm() {

        if (Validate.isTxtEmpty(txtID.getText())) {
            MsgBox.alert(this, "Mã nhân viên không được để trống!");
            return null;
        }
        if (Validate.isTxtEmpty(txtPassword.getText())) {
            MsgBox.alert(this, "Mật khẩu không được để trống!");
            return null;
        }
        if (Validate.isTxtEmpty(txtPasswordAgain.getText())) {
            MsgBox.alert(this, "Mật khẩu xác nhận không được để trống!");
            return null;
        }
        if (Validate.isTxtEmpty(txtName.getText())) {
            MsgBox.alert(this, "Vui lòng nhập họ tên nhân viên!");
            return null;
        }
        if (!Validate.isValidPassword(txtPassword.getText())) {
            MsgBox.alert(this, "Vui lòng nhập mật khẩu có ít nhất một số, một kí tự hoa, một kí tự thường!!");
            return null;
        }
        NhanVien nv = new NhanVien();
        nv.setMaNV(txtID.getText());
        nv.setHoTen(txtName.getText());
        nv.setMatKhau(txtPassword.getText());
        nv.setVaiTro(rdoTP.isSelected());
        return nv;
    }

    private void clearForm() {
        NhanVien nv = new NhanVien();
        this.setForm(nv);
        this.row = -1;
        this.updateStatus();

    }

    private void edit() {
        String manv = (String) tblDS.getValueAt(this.row, 1);
        NhanVien nv = dao.selectByid(manv);
        this.setForm(nv);
        tabNV.setSelectedIndex(0);
        this.updateStatus();
    }

    private void insert() {
        NhanVien nv = getForm();
        String mk2 = txtPasswordAgain.getText();
        if (nv != null) {
            if (dao.selectByid(nv.getMaNV()) != null) {
                MsgBox.alert(this, "Mã nhân viên đã tồn tại");

            } else if (!mk2.equals(nv.getMatKhau())) {
                MsgBox.alert(this, "Mật khẩu xác nhân chưa đúng!");
            } else {
                try {
                    nv.setMatKhau(Validate.hashPassword(txtPassword.getText()));
                    dao.insert(nv);
                    this.fillToTable();
                    this.clearForm();
                    MsgBox.alert(this, "Thêm mới thành công");

                } catch (Exception e) {
                    MsgBox.alert(this, "Thêm mới thất bại");
                }
            }
        }
    }

    private void detele() {
        if (!Auth.isManager()) {
            MsgBox.alert(this, "Bạn không có quyền xóa nhân viên");
        } else {
            String maNV = txtID.getText();
            if (maNV.equals(Auth.user.getMaNV())) {
                btnDelete.setEnabled(false);
            } else if (MsgBox.comfirm(this, "Bạn thực sự muốn xóa nhân viên " + maNV + "?")) {
                btnDelete.setEnabled(true);
                try {
                    dao.delete(maNV);
                    this.fillToTable();
                    this.clearForm();
                    MsgBox.alert(this, "Xóa thành công");
                } catch (Exception e) {
                    MsgBox.alert(this, "Xóa thất bại");
                }
            }
        }
    }

    private void update() {
        NhanVien nv = getForm();
        String mk2 = txtPasswordAgain.getText();
        if (nv != null) {
            if (!mk2.equals(nv.getMatKhau())) {
                MsgBox.alert(this, "Mật khẩu xác nhân chưa đúng!");
            } else {
                try {
                    nv.setMatKhau(Validate.hashPassword(txtPassword.getText()));
                    dao.update(nv);
                    this.fillToTable();
                    MsgBox.alert(this, "Cập nhật thành công");
                } catch (Exception e) {
                    MsgBox.alert(this, "Cập nhật thất bại");

                }
            }
        }
    }

    void first() {
        this.row = 0;
        this.edit();
    }

    private void last() {
        this.row = tblDS.getRowCount() - 1;
        this.edit();
    }

    void prev() {
        this.row--;
        if (this.row >= 0) {
            this.edit();
        } else if (this.row < 0) {
            this.row = tblDS.getRowCount() - 1;
            this.edit();
        }
    }

    private void next() {
        this.row++;
        System.out.println(this.row);
        if (this.row <= tblDS.getRowCount() - 1) {
            this.edit();
        } else if (this.row > tblDS.getRowCount() - 1) {
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tabNV = new javax.swing.JTabbedPane();
        tabCapnhat = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        txtPasswordAgain = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        rdoTP = new javax.swing.JRadioButton();
        rdoNV = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnImageQR = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnNext = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnFirst = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        tabDanhsach = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDS = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Poly Pro");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 102));
        jLabel1.setText("QUẢN LÝ NHÂN VIÊN ");

        tabNV.setForeground(new java.awt.Color(0, 102, 102));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 102));
        jLabel2.setText("Mã nhân viên:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 102));
        jLabel3.setText("Mật khẩu:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 102, 102));
        jLabel4.setText("Xác nhận mật khẩu:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 102, 102));
        jLabel5.setText("Họ và tên:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 102, 102));
        jLabel6.setText("Vai trò:");

        buttonGroup1.add(rdoTP);
        rdoTP.setForeground(new java.awt.Color(0, 102, 102));
        rdoTP.setText("Trưởng phòng");

        buttonGroup1.add(rdoNV);
        rdoNV.setForeground(new java.awt.Color(0, 102, 102));
        rdoNV.setText("Nhân viên");

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnAdd.setForeground(new java.awt.Color(0, 102, 102));
        btnAdd.setText("Thêm");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnDelete.setForeground(new java.awt.Color(0, 102, 102));
        btnDelete.setText("Xoá");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnUpdate.setForeground(new java.awt.Color(0, 102, 102));
        btnUpdate.setText("Sửa");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnClear.setForeground(new java.awt.Color(0, 102, 102));
        btnClear.setText("Mới");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnImageQR.setText("QR");
        btnImageQR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImageQRActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnImageQR, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnImageQR, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

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

        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
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
                .addContainerGap()
                .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout tabCapnhatLayout = new javax.swing.GroupLayout(tabCapnhat);
        tabCapnhat.setLayout(tabCapnhatLayout);
        tabCapnhatLayout.setHorizontalGroup(
            tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabCapnhatLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabCapnhatLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(tabCapnhatLayout.createSequentialGroup()
                        .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtID)
                            .addComponent(txtPassword)
                            .addComponent(txtPasswordAgain)
                            .addGroup(tabCapnhatLayout.createSequentialGroup()
                                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addGroup(tabCapnhatLayout.createSequentialGroup()
                                        .addComponent(rdoNV)
                                        .addGap(44, 44, 44)
                                        .addComponent(rdoTP)))
                                .addGap(0, 269, Short.MAX_VALUE))
                            .addComponent(txtName))
                        .addGap(38, 38, 38)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))))
        );
        tabCapnhatLayout.setVerticalGroup(
            tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabCapnhatLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabCapnhatLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPasswordAgain, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdoTP)
                    .addComponent(rdoNV))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        tabNV.addTab("CẬP NHẬT", tabCapnhat);

        tblDS.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã nhân viên", "Mật khẩu", "Họ tên", "Vai trò"
            }
        ));
        tblDS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDSMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblDS);

        javax.swing.GroupLayout tabDanhsachLayout = new javax.swing.GroupLayout(tabDanhsach);
        tabDanhsach.setLayout(tabDanhsachLayout);
        tabDanhsachLayout.setHorizontalGroup(
            tabDanhsachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 758, Short.MAX_VALUE)
        );
        tabDanhsachLayout.setVerticalGroup(
            tabDanhsachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabDanhsachLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabNV.addTab("DANH SÁCH", tabDanhsach);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabNV)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(213, 213, 213)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tabNV))
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearForm();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        insert();        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        detele();        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        update();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        // TODO add your handling code here:
        prev();
    }//GEN-LAST:event_btnPreviousActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        // TODO add your handling code here:
        first();
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        next();        // TODO add your handling code here:
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        // TODO add your handling code here:
        last();
    }//GEN-LAST:event_btnLastActionPerformed

    private void tblDSMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDSMousePressed
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.row = tblDS.getSelectedRow();
            edit();
        }
    }//GEN-LAST:event_tblDSMousePressed

    private void btnImageQRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImageQRActionPerformed
        try {
            String manv = (String) tblDS.getValueAt(this.row, 1);
            NhanVien nv = dao.selectByid(manv);
            String qrCodeText = nv.getMaNV() + ".byLinh" + nv.getMatKhau();
            String filePath = nv.getHoTen() + ".jpg";
            File destination = new File("QR", filePath);

            filePath = Paths.get(destination.getAbsolutePath()).toString();
            int size = 200;
            String fileType = "jpg";
            File qrFile = new File(filePath);
            QRCodeUtil.createQRImage(qrFile, qrCodeText, size, fileType);
            this.qrCodeImage = filePath;
            new QRCodeJDL(this.parent, true).setVisible(true);
        } catch (WriterException | IOException ex) {
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
            java.util.logging.Logger.getLogger(NhanVienJDL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NhanVienJDL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NhanVienJDL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NhanVienJDL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                NhanVienJDL dialog = new NhanVienJDL(new javax.swing.JFrame(), true);
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
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rdoNV;
    private javax.swing.JRadioButton rdoTP;
    private javax.swing.JPanel tabCapnhat;
    private javax.swing.JPanel tabDanhsach;
    private javax.swing.JTabbedPane tabNV;
    private javax.swing.JTable tblDS;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtName;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JPasswordField txtPasswordAgain;
    // End of variables declaration//GEN-END:variables
}
