/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.polypro.ui;

import com.polypro.dao.KhoaHocDAO;
import com.polypro.dao.ThongKeDAO;
import com.polypro.entity.KhoaHoc;
import com.polypro.utils.Auth;
import com.polypro.utils.ExcelUtil;
import com.polypro.utils.MsgBox;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author MY LINH
 */
public class ThongKeJDL extends javax.swing.JDialog {
    
    private DefaultTableModel modelTblBD = new DefaultTableModel();
    private DefaultTableModel modelTblNH = new DefaultTableModel();
    private DefaultTableModel modelTblCD = new DefaultTableModel();
    private DefaultTableModel modelTblDT = new DefaultTableModel();
    KhoaHocDAO khDAO = new KhoaHocDAO();
    ThongKeDAO tkDAO = new ThongKeDAO();

    /**
     * Creates new form ThongKeJDL
     *
     * @param parent
     * @param modal
     * @param i
     */
    public ThongKeJDL(java.awt.Frame parent, boolean modal, int i) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        Auth.checkLogin(null);
        initTable();
        fillCboKhoaHoc();
        fillCboNam();
        fillTableDiemChuyenDe();
        fillTableLuongNguoiHoc();
        fillTableDoanhThu();
        if (!Auth.isManager()) {
            tabsMain2.remove(3);
        }
        tabsMain2.setSelectedIndex(i);
    }
    
    public void initTable() {
        modelTblBD = (DefaultTableModel) tblBD.getModel();
        String columnbd[] = new String[]{
            "Mã người học", "Họ và tên", "Điểm", "Xếp loại"
        };
        modelTblBD.setColumnIdentifiers(columnbd);
        
        modelTblNH = (DefaultTableModel) tblNH.getModel();
        String columnnh[] = new String[]{
            "Năm", "Số người học", "Đăng ký sớm nhất", "Đăng ký muộn nhất"
        };
        modelTblNH.setColumnIdentifiers(columnnh);
        
        modelTblCD = (DefaultTableModel) tblDCD.getModel();
        String columndcd[] = new String[]{
            "Chuyên đề", "Số lượng học viên", "Điểm thấp nhất", "Điểm cao nhất", "Điểm trung bình"
        };
        modelTblCD.setColumnIdentifiers(columndcd);
        
        modelTblDT = (DefaultTableModel) tblDT.getModel();
        String columndt[] = new String[]{
            "Chuyên đề", "Số khoá học", "Số học viên", "Doanh thu", "Học phí thấp nhất", "Học phí cao nhất", "Học phí trung bình"
        };
        modelTblDT.setColumnIdentifiers(columndt);
    }
    
    private void fillCboKhoaHoc() {
        DefaultComboBoxModel modelCboKH = (DefaultComboBoxModel) cboKhoaHoc.getModel();
        modelCboKH.removeAllElements();
        List<KhoaHoc> lst = khDAO.selectAll();
        for (KhoaHoc kh : lst) {
            modelCboKH.addElement(kh);
        }
    }
    
    private void fillTblBangDiem() {
        modelTblBD = (DefaultTableModel) tblBD.getModel();
        modelTblBD.setRowCount(0);
        try {
            KhoaHoc kh = (KhoaHoc) cboKhoaHoc.getSelectedItem();
            List<Object[]> lst = tkDAO.getBangDiem(kh.getMaKH());
            for (Object[] row : lst) {
                double diemD = (double) row[2];
                String diem = String.format("%.1f", diemD);
                modelTblBD.addRow(new Object[]{
                    row[0],
                    row[1],
                    Double.valueOf(diem),
                    getXepLoai(Double.parseDouble(diem))
                }
                );
            }
        } catch (Exception e) {
            
        }
        
    }
    
    private String getXepLoai(double diem) {
        if (diem < 5) {
            return "Chưa đạt";
        }
        if (diem < 6.5) {
            return "Trung bình";
        }
        if (diem < 7.5) {
            return "Khá";
        }
        if (diem < 0) {
            return "Giỏi";
        }
        return "Xuất sắc";
    }
    
    private void fillTableLuongNguoiHoc() {
        modelTblNH = (DefaultTableModel) tblNH.getModel();
        modelTblNH.setRowCount(0);
        List<Object[]> lst = tkDAO.getLuongNguoiHoc();
        for (Object[] row : lst) {
            modelTblNH.addRow(row);
        }
        
    }
    
    private void fillTableDiemChuyenDe() {
        modelTblCD = (DefaultTableModel) tblDCD.getModel();
        modelTblCD.setRowCount(0);
        List<Object[]> lst = tkDAO.getDiemChuyenDe();
        for (Object[] row : lst) {
            modelTblCD.addRow(new Object[]{
                row[0],
                row[1],
                row[2],
                row[3],
                String.format("%.1f", row[4])
            }
            );
        }
        
    }
    
    private void fillCboNam() {
        DefaultComboBoxModel modelCboNam = (DefaultComboBoxModel) cboNam.getModel();
        modelCboNam.removeAllElements();
        List<Integer> lst = khDAO.selectYears();
        for (Integer year : lst) {
            modelCboNam.addElement(year);
        }
    }
    
    private void fillTableDoanhThu() {
        modelTblDT = (DefaultTableModel) tblDT.getModel();
        modelTblDT.setRowCount(0);
        List<Object[]> lst = tkDAO.getDoanhThu(Integer.parseInt(cboNam.getSelectedItem().toString()));
        for (Object[] row : lst) {
            int i = 0;
            modelTblDT.addRow(new Object[]{
                row[0],
                row[1],
                row[2],
                String.format("%.1f", row[3]),
                String.format("%.1f", row[4]),
                String.format("%.1f", row[5]),
                String.format("%.1f", row[6])
            }
            );
        }
        
    }
    
    private void chooseDirectoryToSave(Workbook workbook) {
        JFileChooser choose = new JFileChooser();
        int x = choose.showSaveDialog(null);
        if (x == JFileChooser.APPROVE_OPTION) {
            try {
                String file = choose.getSelectedFile().getAbsolutePath().toString();
                FileOutputStream outFile = new FileOutputStream(file);
                workbook.write(outFile);
                workbook.close();
                outFile.close();
                MsgBox.alert(this, "Xuất tệp Excel thành công!");
            } catch (IOException ex) {
                Logger.getLogger(ThongKeJDL.class.getName()).log(Level.SEVERE, null, ex);
            }
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

        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        tabsMain2 = new javax.swing.JTabbedPane();
        tabsNgHoc2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblNH = new javax.swing.JTable();
        tabsDiem = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblDCD = new javax.swing.JTable();
        tabsKH = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblBD = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        cboKhoaHoc = new javax.swing.JComboBox<>();
        btnExport = new javax.swing.JButton();
        tabsDoanhThu = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblDT = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        cboNam = new javax.swing.JComboBox<>();

        jButton1.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Poly Pro");

        jLabel3.setFont(new java.awt.Font("Source Sans Pro Black", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 102));
        jLabel3.setText("TỔNG HỢP THỐNG KÊ");

        tabsMain2.setForeground(new java.awt.Color(0, 102, 102));

        tblNH.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Năm", "Số người học", "Đăng kí sớm nhất", "Đăng kí trễ nhất"
            }
        ));
        tblNH.setSelectionBackground(new java.awt.Color(0, 153, 153));
        tblNH.setSelectionForeground(new java.awt.Color(255, 255, 255));
        jScrollPane3.setViewportView(tblNH);

        javax.swing.GroupLayout tabsNgHoc2Layout = new javax.swing.GroupLayout(tabsNgHoc2);
        tabsNgHoc2.setLayout(tabsNgHoc2Layout);
        tabsNgHoc2Layout.setHorizontalGroup(
            tabsNgHoc2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1118, Short.MAX_VALUE)
        );
        tabsNgHoc2Layout.setVerticalGroup(
            tabsNgHoc2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)
        );

        tabsMain2.addTab("NGƯỜI HỌC", tabsNgHoc2);

        tblDCD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Chuyên đề", "Số lượng người học", "Điểm thấp nhất", "Điểm cao nhất", "Điểm trung bình"
            }
        ));
        tblDCD.setSelectionBackground(new java.awt.Color(0, 153, 153));
        tblDCD.setSelectionForeground(new java.awt.Color(255, 255, 255));
        jScrollPane4.setViewportView(tblDCD);

        javax.swing.GroupLayout tabsDiemLayout = new javax.swing.GroupLayout(tabsDiem);
        tabsDiem.setLayout(tabsDiemLayout);
        tabsDiemLayout.setHorizontalGroup(
            tabsDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1118, Short.MAX_VALUE)
        );
        tabsDiemLayout.setVerticalGroup(
            tabsDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)
        );

        tabsMain2.addTab("ĐIỂM CHUYÊN ĐỀ", tabsDiem);

        tblBD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã người học", "Họ tên", "Điểm", "Xếp loại"
            }
        ));
        tblBD.setSelectionBackground(new java.awt.Color(0, 153, 153));
        tblBD.setSelectionForeground(new java.awt.Color(255, 255, 255));
        jScrollPane5.setViewportView(tblBD);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Khóa Học", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(0, 102, 102))); // NOI18N

        cboKhoaHoc.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cboKhoaHoc.setForeground(new java.awt.Color(0, 102, 102));
        cboKhoaHoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboKhoaHoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboKhoaHocActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cboKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 925, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cboKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        btnExport.setText("Export");
        btnExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tabsKHLayout = new javax.swing.GroupLayout(tabsKH);
        tabsKH.setLayout(tabsKHLayout);
        tabsKHLayout.setHorizontalGroup(
            tabsKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5)
            .addGroup(tabsKHLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnExport, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                .addContainerGap())
        );
        tabsKHLayout.setVerticalGroup(
            tabsKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabsKHLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(tabsKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabsKHLayout.createSequentialGroup()
                        .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE))
        );

        tabsMain2.addTab("BẢNG ĐIỂM", tabsKH);

        tblDT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Chuyên đề", "Số khóa", "Số học viên", "Doanh thu", "Học phí thấp nhất", "Học phí cao nhất", "Học phí trung bình"
            }
        ));
        tblDT.setSelectionBackground(new java.awt.Color(0, 153, 153));
        tblDT.setSelectionForeground(new java.awt.Color(255, 255, 255));
        jScrollPane6.setViewportView(tblDT);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Năm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(0, 102, 102))); // NOI18N

        cboNam.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cboNam.setForeground(new java.awt.Color(0, 102, 102));
        cboNam.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboNam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboNamActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cboNam, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(cboNam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        javax.swing.GroupLayout tabsDoanhThuLayout = new javax.swing.GroupLayout(tabsDoanhThu);
        tabsDoanhThu.setLayout(tabsDoanhThuLayout);
        tabsDoanhThuLayout.setHorizontalGroup(
            tabsDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 1118, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        tabsDoanhThuLayout.setVerticalGroup(
            tabsDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabsDoanhThuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE))
        );

        tabsMain2.addTab("DOANH THU", tabsDoanhThu);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabsMain2)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(383, 383, 383)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(tabsMain2))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboKhoaHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboKhoaHocActionPerformed
        // TODO add your handling code here:
//        int i = cboKhoaHoc.getSelectedIndex();
//        if (i > -1) {
        fillTblBangDiem();
//        }
    }//GEN-LAST:event_cboKhoaHocActionPerformed

    private void cboNamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboNamActionPerformed
        // TODO add your handling code here:
        int i = cboNam.getSelectedIndex();
        if (i > -1) {
            fillTableDoanhThu();
        }
    }//GEN-LAST:event_cboNamActionPerformed

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
        // TODO add your handling code here:
        try {
            
            Workbook workBook = ExcelUtil.printBangDiemKhoaHocToExcel(tblBD, cboKhoaHoc, tkDAO);
            this.chooseDirectoryToSave(workBook);
            Logger.getLogger(ThongKeJDL.class.getName()).log(Level.INFO,
                    "Xuất file thành công!");
        } catch (IOException ex) {
            Logger.getLogger(ThongKeJDL.class.getName()).log(Level.SEVERE, null, ex);
            
        }

    }//GEN-LAST:event_btnExportActionPerformed

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
            java.util.logging.Logger.getLogger(ThongKeJDL.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
            
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ThongKeJDL.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
            
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ThongKeJDL.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
            
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ThongKeJDL.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ThongKeJDL dialog = new ThongKeJDL(new javax.swing.JFrame(), true, 0);
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
    private javax.swing.JButton btnExport;
    private javax.swing.JComboBox<String> cboKhoaHoc;
    private javax.swing.JComboBox<String> cboNam;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JPanel tabsDiem;
    private javax.swing.JPanel tabsDoanhThu;
    private javax.swing.JPanel tabsKH;
    private javax.swing.JTabbedPane tabsMain2;
    private javax.swing.JPanel tabsNgHoc2;
    private javax.swing.JTable tblBD;
    private javax.swing.JTable tblDCD;
    private javax.swing.JTable tblDT;
    private javax.swing.JTable tblNH;
    // End of variables declaration//GEN-END:variables
}
