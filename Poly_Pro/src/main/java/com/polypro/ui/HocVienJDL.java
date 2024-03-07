/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.polypro.ui;

import com.polypro.dao.ChuyenDeDAO;
import com.polypro.dao.HocVienDAO;
import com.polypro.dao.KhoaHocDAO;
import com.polypro.dao.NguoiHocDAO;
import com.polypro.entity.ChuyenDe;
import com.polypro.entity.HocVien;
import com.polypro.entity.KhoaHoc;
import com.polypro.entity.NguoiHoc;
import com.polypro.utils.Auth;
import com.polypro.utils.MsgBox;
import com.polypro.utils.Validate;

import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author MY LINH
 */
public class HocVienJDL extends javax.swing.JDialog {

    private DefaultTableModel tblModelHV = new DefaultTableModel();
    private DefaultTableModel tblModelNH = new DefaultTableModel();

    HocVienDAO hvDao = new HocVienDAO();
    NguoiHocDAO nhDao = new NguoiHocDAO();
    KhoaHocDAO khDao = new KhoaHocDAO();
    ChuyenDeDAO cdDao = new ChuyenDeDAO();

    /**
     * Creates new form HocVienJDL
     */
    public HocVienJDL(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        initTable();
        fillCboChuyenDe();
        Auth.checkLogin(btnDelete);

    }

    private void initTable() {
        tblModelHV = (DefaultTableModel) tblHV.getModel();
        String columnhv[] = new String[]{
            "Thứ tự", "Mã học viên", "Mã người học", "Họ tên", "Điểm"
        };
        tblModelHV.setColumnIdentifiers(columnhv);
        tblModelNH = (DefaultTableModel) tblNH.getModel();
        String columnnh[] = new String[]{
            "Thứ tự", "Mã người học", "Họ và tên", "Giới tính", "Ngày sinh", "Điện thoại", "Email"
        };
        tblModelNH.setColumnIdentifiers(columnnh);
    }

    private void fillCboChuyenDe() {
        try {
            DefaultComboBoxModel cboCDModel = (DefaultComboBoxModel) cboCD.getModel();
            cboCDModel.removeAllElements();
            List<ChuyenDe> lstChuyenDe = cdDao.selectAll();
            for (ChuyenDe cd : lstChuyenDe) {
                cboCDModel.addElement(cd);
            }
            cboCD.setModel(cboCDModel);
            this.fillCboKhoaHoc();
        } catch (Exception e) {
        }

    }

    private void fillCboKhoaHoc() {
        try {
            DefaultComboBoxModel cboKHModel = (DefaultComboBoxModel) cboKH.getModel();
            cboKHModel.removeAllElements();
            ChuyenDe cd = (ChuyenDe) cboCD.getSelectedItem();
            if (cd != null) {
                List<KhoaHoc> lstKhoaHoc = khDao.selectByChuyenDe(cd.getMaCD());
                for (KhoaHoc kh : lstKhoaHoc) {
                    cboKHModel.addElement(kh);
                }
                this.fillTableHV();
            }
        } catch (Exception e) {
        }

    }

    private void fillTableNH() {
        tblModelNH.setRowCount(0);
        try {
            KhoaHoc kh = (KhoaHoc) cboKH.getSelectedItem();
            String keyword = txtFind.getText();

            List<NguoiHoc> lst = nhDao.selectNotInCourse(kh.getMaKH(), keyword);
            for (int i = 0; i < lst.size(); i++) {
                NguoiHoc nh = lst.get(i);
                tblModelNH.addRow(new Object[]{
                    i + 1,
                    nh.getMaNH(),
                    nh.getHoTen(),
                    nh.isGioiTinh() ? "Nam" : "Nữ",
                    nh.getNgaySinh(),
                    nh.getSoDienThoai(),
                    nh.getEmail()
                });
            }
        } catch (Exception e) {
        }
    }
    ///#**********************************************************************#

    private void fillTableHV() {
        try {
            tblModelHV.setRowCount(0);
            KhoaHoc kh = (KhoaHoc) cboKH.getSelectedItem();
            if (kh != null) {
                List<HocVien> lst = hvDao.selectByKhoaHoc(kh.getMaKH());
                for (int i = 0; i < lst.size(); i++) {
                    HocVien hv = lst.get(i);
                    String hoten = nhDao.selectByid(hv.getMaNH()).getHoTen();
                    tblModelHV.addRow(new Object[]{
                        i + 1,
                        hv.getMaHV(),
                        hv.getMaNH(),
                        hoten,
                        hv.getDiemTB()
                    });
                }
                this.fillTableNH();
            }
        } catch (Exception e) {
        }

    }
///#**********************************************************************#

    private void addHocVien() {
        try {
            KhoaHoc kh = (KhoaHoc) cboKH.getSelectedItem();
            for (int row : tblNH.getSelectedRows()) {
                HocVien hv = new HocVien();
                hv.setMaKH(kh.getMaKH());
                hv.setDiemTB(0);
                hv.setMaNH((String) tblNH.getValueAt(row, 1));
                hvDao.insert(hv);
            }
        } catch (Exception e) {
        }

        this.fillTableHV();
        this.tabs.setSelectedIndex(0);
    }

    private void removeHocVien() {
        try {
            if (!Auth.isManager()) {
                MsgBox.alert(this, "Bạn không có quyền xóa!");
            } else {
                if (MsgBox.comfirm(this, "Bạn muốn xóa học viên được chọn?")) {
                    for (int row : tblHV.getSelectedRows()) {
                        int mahv = (int) tblHV.getValueAt(row, 1);
                        // int
                        hvDao.delete(mahv);
                    }
                    this.fillTableHV();
                }
            }
        } catch (Exception e) {

        }

    }

    private void updateDiem() {
        for (int i = 0; i < tblHV.getRowCount(); i++) {
            int mahv = (int) tblHV.getValueAt(i, 1);
            double diem = Double.parseDouble(tblHV.getValueAt(i, 4).toString());

            if (!Validate.isTxtEmpty(String.valueOf(diem))) {
                MsgBox.alert(this, "Vui lòng nhập đầy đủ!");
                this.fillTableHV();
                return;

            } else if (!Validate.isValidScore(diem)) {
                MsgBox.alert(this, "Điểm phải là số trong khoảng [0-10]!");
                this.fillTableHV();
                return;
            }

            HocVien hv = hvDao.selectByid(mahv);
            hv.setDiemTB(diem);
            hvDao.update(hv);
        }
        this.fillTableHV();
        MsgBox.alert(this, "Cập nhật điểm thành công!");
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
        tabs = new javax.swing.JTabbedPane();
        tabCapnhat = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblHV = new javax.swing.JTable();
        btnDelete = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        tabDanhsach = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblNH = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        txtFind = new javax.swing.JTextField();
        btnFind = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        cboCD = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        cboKH = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Poly Pro");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 102));
        jLabel1.setText("QUẢN LÝ HỌC VIÊN");

        tabs.setForeground(new java.awt.Color(0, 102, 102));
        tabs.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        tblHV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Thứ tự", "Mã học viên", "Mã người học", "Họ tên", "Điểm"
            }
        ));
        tblHV.setSelectionBackground(new java.awt.Color(0, 153, 153));
        tblHV.setSelectionForeground(new java.awt.Color(255, 255, 255));
        jScrollPane3.setViewportView(tblHV);

        btnDelete.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(0, 102, 102));
        btnDelete.setText("Xoá khỏi khoá học");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnUpdate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(0, 102, 102));
        btnUpdate.setText("Cập nhật điểm");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tabCapnhatLayout = new javax.swing.GroupLayout(tabCapnhat);
        tabCapnhat.setLayout(tabCapnhatLayout);
        tabCapnhatLayout.setHorizontalGroup(
            tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabCapnhatLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(294, 294, 294))
            .addGroup(tabCapnhatLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1054, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );
        tabCapnhatLayout.setVerticalGroup(
            tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabCapnhatLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        tabs.addTab("HỌC VIÊN", tabCapnhat);

        tblNH.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã người học", "Họ tên ", "Giới tính", "Ngày sinh", "Số điện thoại", "Email"
            }
        ));
        tblNH.setSelectionBackground(new java.awt.Color(0, 153, 153));
        tblNH.setSelectionForeground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(tblNH);

        jPanel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        txtFind.setToolTipText("");

        btnFind.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnFind.setForeground(new java.awt.Color(0, 102, 102));
        btnFind.setText("Tìm");
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 102));
        jLabel2.setText("Tìm kiếm:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(txtFind, javax.swing.GroupLayout.PREFERRED_SIZE, 790, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(btnFind, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFind, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFind, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        btnAdd.setText("Thêm học viên");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        jButton1.setText("Danh sách");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tabDanhsachLayout = new javax.swing.GroupLayout(tabDanhsach);
        tabDanhsach.setLayout(tabDanhsachLayout);
        tabDanhsachLayout.setHorizontalGroup(
            tabDanhsachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabDanhsachLayout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(tabDanhsachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tabDanhsachLayout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14))
                    .addComponent(jScrollPane1))
                .addGap(18, 18, 18))
        );
        tabDanhsachLayout.setVerticalGroup(
            tabDanhsachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabDanhsachLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tabDanhsachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        tabs.addTab("NGƯỜI HỌC", tabDanhsach);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chuyên đề", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(0, 102, 102))); // NOI18N

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
                .addComponent(cboCD, 0, 492, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cboCD, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Khóa học", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(0, 102, 102))); // NOI18N

        cboKH.setForeground(new java.awt.Color(0, 102, 102));
        cboKH.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboKHActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cboKH, javax.swing.GroupLayout.PREFERRED_SIZE, 492, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cboKH, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 1071, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(365, 365, 365)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 462, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboCDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCDActionPerformed
        // TODO add your handling code here:
        int i = cboCD.getSelectedIndex();
        if (i > -1) {
            fillCboKhoaHoc();
        }


    }//GEN-LAST:event_cboCDActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        addHocVien();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        removeHocVien();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        updateDiem();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindActionPerformed
        // TODO add your handling code here:
        fillTableNH();
    }//GEN-LAST:event_btnFindActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        txtFind.setText("");
        fillTableNH();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cboKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboKHActionPerformed
        // TODO add your handling code here:
        fillTableHV();
    }//GEN-LAST:event_cboKHActionPerformed

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
            java.util.logging.Logger.getLogger(HocVienJDL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HocVienJDL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HocVienJDL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HocVienJDL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                HocVienJDL dialog = new HocVienJDL(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFind;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cboCD;
    private javax.swing.JComboBox<String> cboKH;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPanel tabCapnhat;
    private javax.swing.JPanel tabDanhsach;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblHV;
    private javax.swing.JTable tblNH;
    private javax.swing.JTextField txtFind;
    // End of variables declaration//GEN-END:variables
}
