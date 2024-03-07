/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.polypro.ui;

import com.polypro.dao.ChuyenDeDAO;
import com.polypro.entity.ChuyenDe;
import com.polypro.utils.*;
import java.awt.Image;
import java.io.File;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author MY LINH
 */
public class ChuyenDeJDL extends javax.swing.JDialog {

    int row = -1;
    DefaultTableModel tblModel;
    ChuyenDeDAO dao = new ChuyenDeDAO();

    /**
     * Creates new form ChuyenDeJDL
     */
    public ChuyenDeJDL(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        Auth.checkLogin(btnXoa);
        setLocationRelativeTo(null);
        btnFirst.setIcon(XImage.insertIcon(30, 30, "image//first.png"));
        btnPre.setIcon(XImage.insertIcon(30, 30, "image//pre.png"));
        btnNext.setIcon(XImage.insertIcon(30, 30, "image//next.png"));
        btnLast.setIcon(XImage.insertIcon(30, 30, "image//last.png"));
        initTable();
        fillToTable();
        updateStatus();

    }

    public void initTable() {
        tblModel = (DefaultTableModel) tblDSCD.getModel();
        String columns[] = new String[]{
            "STT", "Mã chuyên đề", "Tên chuyên đề", "Học phí", "Thời lượng", "Hình"
        };
        tblModel.setColumnIdentifiers(columns);
    }

    private void choose() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            XImage.save(file);
            ImageIcon ico = XImage.read(file.getName());
            Image img = ico.getImage().getScaledInstance(lblHinh.getWidth(), lblHinh.getHeight(), Image.SCALE_SMOOTH);
            lblHinh.setIcon(new ImageIcon(img));
            lblHinh.setToolTipText(file.getName());
        }
    }

    private void setForm(ChuyenDe cd) {
        try {
            txtIDChuyenDe.setText(cd.getMaCD());
            txtNameChuyenDe.setText(cd.getTenCD());
            txtThoiLuong.setText(String.valueOf(cd.getThoiLuong()));
            txtHocPhi.setText(String.valueOf(cd.getHocPhi()));
            txtMoTa.setText(cd.getMoTa());
            if (cd.getHinhLoGo() != null) {
                lblHinh.setToolTipText(cd.getHinhLoGo());
                ImageIcon ico = XImage.read(cd.getHinhLoGo());
                Image img = ico.getImage().getScaledInstance(lblHinh.getWidth(), lblHinh.getHeight(), Image.SCALE_SMOOTH);
                lblHinh.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {
        }

    }

    private ChuyenDe getForm() {
        if (Validate.isTxtEmpty(txtHocPhi.getText())
                || Validate.isTxtEmpty(txtIDChuyenDe.getText())
                || Validate.isTxtEmpty(txtNameChuyenDe.getText())
                || Validate.isTxtEmpty(txtThoiLuong.getText())) {
            MsgBox.alert(this, "Vui lòng nhập đầy đủ!!!");
            return null;
        }
//        if(Validate.isTxtEmpty(lblHinh.getToolTipText())){
//             MsgBox.alert(this, "Vui lòng chọn hình!!!");
//            return null;
//        }
        if (!Validate.isValidID_7(txtIDChuyenDe.getText())) {
            MsgBox.alert(this, "Mã chuyên đề không quá 7 kí tự!");
            return null;
        }

        try {
            double hp = Double.parseDouble(txtHocPhi.getText());
            int tl = Integer.parseInt(txtThoiLuong.getText());
            if (!Validate.isValidRangeNumber(tl, 0, 1000)) {
                MsgBox.alert(this, "Thời lượng trong khoảng [0 - 1000]");
                return null;
            }

            if (!Validate.isValidRangeNumber(hp, 0, 100000000)) {
                MsgBox.alert(this, "Thời lượng trong khoảng [0 - 100.000.000]");
                return null;
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Vui lòng nhập số cho học phí và thời lượng!!");
            return null;
        }
        ChuyenDe cd = new ChuyenDe();
        cd.setMaCD(txtIDChuyenDe.getText());
        cd.setTenCD(txtNameChuyenDe.getText());
        cd.setThoiLuong(Integer.parseInt(txtThoiLuong.getText()));
        cd.setHocPhi(Double.valueOf(txtHocPhi.getText()));
        cd.setMoTa(txtMoTa.getText());
        cd.setHinhLoGo(lblHinh.getToolTipText());
        return cd;
    }

    private void fillToTable() {
        try {
            List<ChuyenDe> lst = dao.selectAll();
            tblModel.setRowCount(0);
            for (int i = 0; i < lst.size(); i++) {
                ChuyenDe cd = lst.get(i);
                Object[] rowCd = {
                    i + 1,
                    cd.getMaCD(),
                    cd.getTenCD(),
                    cd.getHocPhi(),
                    cd.getThoiLuong(),
                    cd.getHinhLoGo()
                };
                tblModel.addRow(rowCd);
            }
            tblModel.fireTableDataChanged();
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
        }
    }

    private void updateStatus() {
        boolean edit = (this.row >= 0);
        boolean first = (this.row == 0);
        boolean last = (this.row == tblDSCD.getRowCount() - 1);
        txtIDChuyenDe.setEditable(!edit);
        btnThem.setEnabled(!edit);
        btnXoa.setEnabled(edit);
        btnSua.setEnabled(edit);
        btnFirst.setEnabled(edit && !first);
        btnLast.setEnabled(edit && !last);

    }

    private void edit() {
        String macd = (String) tblDSCD.getValueAt(this.row, 1);
        ChuyenDe cd = dao.selectByid(macd);
        this.setForm(cd);
        tabCD.setSelectedIndex(0);
        this.updateStatus();
    }

    private void clearForm() {
        ChuyenDe nv = new ChuyenDe();
        lblHinh.setIcon(null);
        lblHinh.setToolTipText("");
        this.setForm(nv);
        this.row = -1;
        this.updateStatus();

    }

    private void insert() {
        ChuyenDe cd = getForm();
        try {
            if (cd != null) {
                if (dao.selectByid(cd.getMaCD()) != null) {
                    MsgBox.alert(this, "Mã chuyên đề đã tồn tại");
                    return;
                }
                dao.insert(cd);
                this.fillToTable();
                this.clearForm();
                MsgBox.alert(this, "Thêm mới thành công");
            }

        } catch (Exception e) {
            MsgBox.alert(this, "Thêm mới thất bại");

        }

    }

    private void detele() {
        if (!Auth.isManager()) {
            MsgBox.alert(this, "Bạn không có quyền xóa chuyên đề");
        } else {
            String macd = txtIDChuyenDe.getText();
            if (MsgBox.comfirm(this, "Bạn thực sự muốn xóa chuyên đề này?")) {
                try {
                    dao.delete(macd);
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
        ChuyenDe cd = getForm();
        try {
            dao.update(cd);
            this.fillToTable();
            MsgBox.alert(this, "Cập nhật thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Cập nhật thất bại");

        }

    }

    void first() {
        this.row = 0;
        this.edit();
    }

    private void last() {
        this.row = tblDSCD.getRowCount() - 1;
        this.edit();
    }

    void prev() {
        this.row--;
        if (this.row >= 0) {
            this.edit();
        } else if (this.row < 0) {
            this.row = tblDSCD.getRowCount() - 1;
            this.edit();
        }
    }

    private void next() {
        this.row++;
        System.out.println(this.row);
        if (this.row <= tblDSCD.getRowCount() - 1) {
            this.edit();
        } else if (this.row > tblDSCD.getRowCount() - 1) {
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
        tabCD = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtNameChuyenDe = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtThoiLuong = new javax.swing.JTextField();
        txtIDChuyenDe = new javax.swing.JTextField();
        txtHocPhi = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtMoTa = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        btnFirst = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnPre = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        lblHinh = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        btnThem = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnMoi = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDSCD = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Poly Pro");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        tabCD.setBackground(new java.awt.Color(255, 255, 255));
        tabCD.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tabCD.setForeground(new java.awt.Color(0, 102, 102));
        tabCD.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));

        jPanel4.setBackground(new java.awt.Color(0, 153, 153));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Mã chuyên đề:");

        txtNameChuyenDe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameChuyenDeActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Tên chuyên đề:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Thời lượng:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Học phí:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Mô tả chuyên đề:");

        txtMoTa.setColumns(20);
        txtMoTa.setRows(5);
        jScrollPane2.setViewportView(txtMoTa);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtHocPhi)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(0, 437, Short.MAX_VALUE))
                    .addComponent(txtIDChuyenDe, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtNameChuyenDe, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtThoiLuong, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(3, 3, 3)
                .addComponent(txtIDChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNameChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtThoiLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtHocPhi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Logo:");

        jPanel5.setBackground(new java.awt.Color(0, 153, 153));

        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnFirst, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnPre, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPre, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        lblHinh.setBackground(new java.awt.Color(255, 255, 255));
        lblHinh.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblHinh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblHinhMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(lblHinh, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(lblHinh, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPanel6.setBackground(new java.awt.Color(0, 153, 153));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chức năng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        btnThem.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnThem.setForeground(new java.awt.Color(0, 102, 102));
        btnThem.setText("Thêm");
        btnThem.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnXoa.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnXoa.setForeground(new java.awt.Color(0, 102, 102));
        btnXoa.setText("Xóa");
        btnXoa.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnMoi.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnMoi.setForeground(new java.awt.Color(0, 102, 102));
        btnMoi.setText("Mới");
        btnMoi.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiActionPerformed(evt);
            }
        });

        btnSua.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnSua.setForeground(new java.awt.Color(0, 102, 102));
        btnSua.setText("Sửa");
        btnSua.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnMoi, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                    .addComponent(btnThem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnXoa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(7, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(217, 217, 217)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        tabCD.addTab("Cập nhật", jPanel2);

        jPanel3.setBackground(new java.awt.Color(0, 153, 153));

        tblDSCD.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 153)));
        tblDSCD.setForeground(new java.awt.Color(0, 102, 102));
        tblDSCD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"ssssss", null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã chuyên đề", "Tên chuyên đề", "Học phí", "Thời lượng", "Hình"
            }
        ));
        tblDSCD.setGridColor(new java.awt.Color(0, 51, 51));
        tblDSCD.setSelectionBackground(new java.awt.Color(0, 153, 153));
        tblDSCD.setSelectionForeground(new java.awt.Color(255, 255, 255));
        tblDSCD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDSCDMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblDSCDMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblDSCD);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 958, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
        );

        tabCD.addTab("Danh sách", jPanel3);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 102, 102));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Quản Lí Chuyên Đề");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(tabCD, javax.swing.GroupLayout.PREFERRED_SIZE, 962, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(tabCD))
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNameChuyenDeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameChuyenDeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameChuyenDeActionPerformed

    private void btnMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiActionPerformed
        // TODO add your handling code here:
        clearForm();
    }//GEN-LAST:event_btnMoiActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFirstActionPerformed

    private void lblHinhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHinhMouseClicked
        // TODO add your handling code here:
        choose();
    }//GEN-LAST:event_lblHinhMouseClicked

    private void tblDSCDMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDSCDMousePressed
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.row = tblDSCD.getSelectedRow();
            edit();
        }
    }//GEN-LAST:event_tblDSCDMousePressed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        insert();
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        update();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        detele();
    }//GEN-LAST:event_btnXoaActionPerformed

    private void tblDSCDMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDSCDMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDSCDMouseReleased

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
            java.util.logging.Logger.getLogger(ChuyenDeJDL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChuyenDeJDL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChuyenDeJDL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChuyenDeJDL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ChuyenDeJDL dialog = new ChuyenDeJDL(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnMoi;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPre;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblHinh;
    private javax.swing.JTabbedPane tabCD;
    private javax.swing.JTable tblDSCD;
    private javax.swing.JTextField txtHocPhi;
    private javax.swing.JTextField txtIDChuyenDe;
    private javax.swing.JTextArea txtMoTa;
    private javax.swing.JTextField txtNameChuyenDe;
    private javax.swing.JTextField txtThoiLuong;
    // End of variables declaration//GEN-END:variables
}
