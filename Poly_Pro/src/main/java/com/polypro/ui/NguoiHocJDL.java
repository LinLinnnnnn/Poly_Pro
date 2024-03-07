/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.polypro.ui;

import com.polypro.dao.NguoiHocDAO;
import com.polypro.entity.NguoiHoc;
import com.polypro.utils.Auth;
import com.polypro.utils.MsgBox;
import com.polypro.utils.Validate;
import static com.polypro.utils.Validate.isOver18;
import static com.polypro.utils.Validate.isValidEmail;
import static com.polypro.utils.Validate.isValidID_7;
import static com.polypro.utils.Validate.isValidPhoneNumber;
import com.polypro.utils.XDate;
import com.polypro.utils.XImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFileChooser;

import javax.swing.table.DefaultTableModel;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author MY LINH
 */
public class NguoiHocJDL extends javax.swing.JDialog {

    int row = - 1;
    NguoiHocDAO dao = new NguoiHocDAO();
    private DefaultTableModel tblModel = new DefaultTableModel();

    /**
     * Creates new form NguoiHocJDL
     */
    public NguoiHocJDL(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        btnFirst.setIcon(XImage.insertIcon(20, 20, "image//first.png"));
        btnPrevious.setIcon(XImage.insertIcon(20, 20, "image//pre.png"));
        btnNext.setIcon(XImage.insertIcon(20, 20, "image//next.png"));
        btnLast.setIcon(XImage.insertIcon(20, 20, "image//last.png"));
        setLocationRelativeTo(null);
        initTable();
        fillToTable();
        Auth.checkLogin(btnDelete);
        updateStatus();
    }

    public void initTable() {
        tblModel = (DefaultTableModel) tblDSNH.getModel();
        String columns[] = new String[]{
            "STT", "Mã người học", "Họ tên", "Giới tính", "Ngày sinh", "Số điện thoại", "Email", "Người tạo", "Ngày tạo"
        };
        tblModel.setColumnIdentifiers(columns);
    }

    private void fillToTable() {
        try {
            tblModel.setRowCount(0);
            List<NguoiHoc> lst = dao.selectAll();
            for (int i = 0; i < lst.size(); i++) {
                NguoiHoc sv = lst.get(i);
                Object[] rowNH = {
                    i + 1,
                    sv.getMaNH(),
                    sv.getHoTen(),
                    sv.isGioiTinh() ? "Nam" : "Nữ",
                    sv.getNgaySinh(),
                    sv.getSoDienThoai(),
                    sv.getEmail(),
                    sv.getMaNV(),
                    sv.getNgayDK()
                };
                tblModel.addRow(rowNH);

            }
            tblModel.fireTableDataChanged();
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
        }

    }

    private void fillSearch() {
        try {
            tblModel.setRowCount(0);
            String key = txtFind.getText();
            List<NguoiHoc> lst = dao.SearchByKey(key);
            for (NguoiHoc sv : lst) {
                Object[] rowNH = {
                    sv.getMaNH(),
                    sv.getHoTen(),
                    sv.isGioiTinh() ? "Nam" : "Nữ",
                    sv.getNgaySinh(),
                    sv.getSoDienThoai(),
                    sv.getEmail(),
                    sv.getMaNV(),
                    sv.getNgayDK()

                };
                tblModel.addRow(rowNH);

            }
            tblModel.fireTableDataChanged();
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
        }

    }

    private void setForm(NguoiHoc nh) {
        try {
            txtIDNguoiHoc.setText(nh.getMaNH());
            txtNameNguoiHoc.setText(nh.getHoTen());
            txtPhoneNumber.setText(nh.getSoDienThoai());
            txtEmail.setText(nh.getEmail());
            txtBirthday.setText(XDate.toString(nh.getNgaySinh(), "dd/MM/yyyy"));
            txtGhiChu.setText(nh.getGhiChu());
            rdoNam.setSelected(nh.isGioiTinh());
            rdoNu.setSelected(!nh.isGioiTinh());
        } catch (Exception e) {
        }

    }

    private NguoiHoc getForm() {
        if (Validate.isTxtEmpty(txtIDNguoiHoc.getText())
                || Validate.isTxtEmpty(txtBirthday.getText())
                || Validate.isTxtEmpty(txtNameNguoiHoc.getText())
                || Validate.isTxtEmpty(txtEmail.getText())
                || Validate.isTxtEmpty(txtPhoneNumber.getText())) {
            MsgBox.alert(this, "Vui lòng nhập đầy đủ(không bao gồm mô tả)!!!");
            return null;
        }

        if (!isValidID_7(txtIDNguoiHoc.getText())) {
            MsgBox.alert(this, "Mã người học không quá 7 kí tự!!!");
            return null;
        }

        if (!isValidPhoneNumber(txtPhoneNumber.getText())) {
            MsgBox.alert(this, "Số điện thoại chưa hợp lệ!");
            return null;
        }
        if (!isOver18(XDate.toDate(txtBirthday.getText(), "dd/MM/yyyy"))) {
            MsgBox.alert(this, "Tuổi người học phải > 18!!");
            return null;
        }
        if (!isValidEmail(txtEmail.getText())) {
            MsgBox.alert(this, "Email chưa hợp lệ!");
            return null;
        }

        NguoiHoc nh = new NguoiHoc();
        nh.setMaNH(txtIDNguoiHoc.getText());
        nh.setHoTen(txtNameNguoiHoc.getText());
        nh.setSoDienThoai(txtPhoneNumber.getText());
        nh.setNgaySinh(XDate.toDate(txtBirthday.getText(), "dd/MM/yyyy"));
        nh.setEmail(txtEmail.getText());
        nh.setGhiChu(txtGhiChu.getText());
        nh.setGioiTinh(rdoNam.isSelected());
        nh.setMaNV(Auth.user.getMaNV());
        nh.setNgayDK(new Date());
        return nh;
    }

    private void updateStatus() {
        boolean edit = (this.row >= 0);
        boolean first = (this.row == 0);
        boolean last = (this.row == tblDSNH.getRowCount() - 1);
        txtIDNguoiHoc.setEditable(!edit);
        btnAdd.setEnabled(!edit);
        btnDelete.setEnabled(edit);
        btnUpdate.setEnabled(edit);
        btnFirst.setEnabled(edit && !first);
        btnLast.setEnabled(edit && !last);

    }

    private void edit() {

        String manh = (String) tblDSNH.getValueAt(this.row, 1);
        System.out.println(manh);
        NguoiHoc nh = dao.selectByid(manh);
        this.setForm(nh);
        tabNH.setSelectedIndex(0);
        this.updateStatus();
    }

    private void clearForm() {
        txtIDNguoiHoc.setText("");
        txtNameNguoiHoc.setText("");
        txtBirthday.setText("");
        txtEmail.setText("");
        txtPhoneNumber.setText("");
        txtGhiChu.setText("");
        rdoNam.setSelected(true);
        this.row = -1;
        this.updateStatus();

    }

    private File chonFileExcelImportNguoiHoc() {
        File excelFile = null;
        JFileChooser fileChooser = new JFileChooser();

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            excelFile = XImage.saveExel(file); // lưu hình vào thư mục logos

        }
        return excelFile;
    }

    private void importNguoiHocFromExcel(File excelFile) {
        NguoiHoc nguoiHoc = new NguoiHoc();
        try {
            FileInputStream file = new FileInputStream(excelFile);

            //Create Workbook instance holding reference to .xlsx file
            HSSFWorkbook workbook = new HSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            HSSFSheet sheet = workbook.getSheetAt(0);

            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();//Skip the header row
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                String maMH = row.getCell(0).getStringCellValue();
                if (dao.selectByid(maMH) != null) {
                    MsgBox.alert(this, "Mã người học" + maMH + " đã tồn tại!! Vui lòng kiểm tra dữ liệu tệp ecxel!!");
//                    row = rowIterator.next();
                    return;
                }
                nguoiHoc.setMaNH(maMH);
                nguoiHoc.setHoTen(row.getCell(1).getStringCellValue());
                if (row.getCell(2).getStringCellValue().equals("Nam")) {
                    nguoiHoc.setGioiTinh(true);
                } else {
                    nguoiHoc.setGioiTinh(false);
                }
                nguoiHoc.setNgaySinh(XDate.toDate(row.getCell(3).getStringCellValue(),
                        "MM/dd/yyyy"));
                nguoiHoc.setSoDienThoai(row.getCell(4).getStringCellValue());
                nguoiHoc.setEmail(row.getCell(5).getStringCellValue());
                nguoiHoc.setGhiChu("");
                nguoiHoc.setMaNV(Auth.user.getMaNV());
                nguoiHoc.setNgayDK(new Date());
                dao.insert(nguoiHoc);
                System.out.println(nguoiHoc);
            }
            file.close();
            this.fillToTable();
        } catch (Exception e) {
            MsgBox.alert(this, "Thêm mới thất bại");
            return;
        }
        MsgBox.alert(this, "Import danh sách người học thành công!");

    }

    private void insert() {
        try {
            NguoiHoc nh = getForm();
            if (nh != null) {
                if (dao.selectByid(nh.getMaNH()) != null) {
                    MsgBox.alert(this, "Mã người học đã tồn tại");
                    return;
                }
                dao.insert(nh);
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
            MsgBox.alert(this, "Bạn không có quyền xóa người học ");
        } else {
            String manh = txtIDNguoiHoc.getText();
            if (MsgBox.comfirm(this, "Bạn thực sự muốn xóa người học " + manh + "?")) {
                try {
                    dao.delete(manh);
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
        NguoiHoc nh = getForm();
        try {
            dao.update(nh);
            this.fillToTable();
            this.clearForm();
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
        this.row = tblDSNH.getRowCount() - 1;
        this.edit();
    }

    void prev() {
        this.row--;
        if (this.row >= 0) {
            this.edit();
        } else if (this.row < 0) {
            this.row = tblDSNH.getRowCount() - 1;
            this.edit();
        }
    }

    private void next() {
        this.row++;
        System.out.println(this.row);
        if (this.row <= tblDSNH.getRowCount() - 1) {
            this.edit();
        } else if (this.row > tblDSNH.getRowCount() - 1) {
            this.row = 0;
            this.edit();
        }
    }

    private void find() {
        this.fillSearch();
        this.updateStatus();
        this.row = - 1;
        this.clearForm();

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
        jPanel17 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        tabNH = new javax.swing.JTabbedPane();
        tabCapnhat5 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        txtBirthday = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtGhiChu = new javax.swing.JTextArea();
        jPanel19 = new javax.swing.JPanel();
        btnFirst = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        txtIDNguoiHoc = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        txtNameNguoiHoc = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        txtPhoneNumber = new javax.swing.JTextField();
        rdoNam = new javax.swing.JRadioButton();
        rdoNu = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        btnImport = new javax.swing.JButton();
        tabDanhsach = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDSNH = new javax.swing.JTable();
        jPanel20 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        txtFind = new javax.swing.JTextField();
        btnFind = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Poly Pro");

        jLabel42.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(0, 102, 102));
        jLabel42.setText("QUẢN LÝ NGƯỜI HỌC");

        tabNH.setForeground(new java.awt.Color(0, 102, 102));

        tabCapnhat5.setBackground(new java.awt.Color(0, 153, 153));

        jLabel43.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setText("Ngày sinh:");

        jLabel44.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setText("Địa chỉ email:");

        jLabel45.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setText("Mã người học:");

        txtGhiChu.setColumns(20);
        txtGhiChu.setRows(5);
        jScrollPane7.setViewportView(txtGhiChu);

        jPanel19.setBackground(new java.awt.Color(0, 153, 153));
        jPanel19.setLayout(new java.awt.GridLayout(1, 4, 8, 0));

        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });
        jPanel19.add(btnFirst);

        btnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
            }
        });
        jPanel19.add(btnPrevious);

        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });
        jPanel19.add(btnNext);

        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });
        jPanel19.add(btnLast);

        jLabel46.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setText("Họ và tên:");

        jLabel47.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 255, 255));
        jLabel47.setText("Giới tính:");

        jLabel48.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(255, 255, 255));
        jLabel48.setText("Ghi chú:");

        jLabel49.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(255, 255, 255));
        jLabel49.setText("Điện thoại:");

        buttonGroup1.add(rdoNam);
        rdoNam.setForeground(new java.awt.Color(255, 255, 255));
        rdoNam.setText("Nam");

        buttonGroup1.add(rdoNu);
        rdoNu.setForeground(new java.awt.Color(255, 255, 255));
        rdoNu.setText("Nữ");

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));

        btnAdd.setText("Thêm");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnDelete.setText("Xoá");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnUpdate.setText("Sửa");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnNew.setText("Mới");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        btnImport.setText("Import");
        btnImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNew, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnImport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnImport)
                .addContainerGap(41, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout tabCapnhat5Layout = new javax.swing.GroupLayout(tabCapnhat5);
        tabCapnhat5.setLayout(tabCapnhat5Layout);
        tabCapnhat5Layout.setHorizontalGroup(
            tabCapnhat5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabCapnhat5Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(tabCapnhat5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(tabCapnhat5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(tabCapnhat5Layout.createSequentialGroup()
                            .addComponent(jLabel48)
                            .addGap(51, 51, 51)
                            .addComponent(jScrollPane7))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, tabCapnhat5Layout.createSequentialGroup()
                            .addGroup(tabCapnhat5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGap(18, 18, 18)
                            .addGroup(tabCapnhat5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtBirthday)
                                .addComponent(txtEmail)))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, tabCapnhat5Layout.createSequentialGroup()
                            .addGroup(tabCapnhat5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel46)
                                .addComponent(jLabel47)
                                .addComponent(jLabel49, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(tabCapnhat5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(tabCapnhat5Layout.createSequentialGroup()
                                    .addComponent(rdoNam)
                                    .addGap(50, 50, 50)
                                    .addComponent(rdoNu))
                                .addComponent(txtIDNguoiHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNameNguoiHoc, javax.swing.GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE)
                                .addComponent(txtPhoneNumber))))
                    .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, 582, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        tabCapnhat5Layout.setVerticalGroup(
            tabCapnhat5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabCapnhat5Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(tabCapnhat5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabCapnhat5Layout.createSequentialGroup()
                        .addGroup(tabCapnhat5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel45)
                            .addComponent(txtIDNguoiHoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(tabCapnhat5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel46)
                            .addComponent(txtNameNguoiHoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tabCapnhat5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel47)
                            .addComponent(rdoNu)
                            .addComponent(rdoNam))
                        .addGap(18, 18, 18)
                        .addGroup(tabCapnhat5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel49))
                        .addGap(18, 18, 18)
                        .addGroup(tabCapnhat5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel43)
                            .addComponent(txtBirthday, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(tabCapnhat5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel44)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(tabCapnhat5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel48)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(51, Short.MAX_VALUE))
        );

        tabNH.addTab("CẬP NHẬT", tabCapnhat5);

        tblDSNH.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã người học", "Họ tên", "Giới tính", "Ngày sinh ", "Số điện thoại", "Email", "Ngày tạo", "Người nhập"
            }
        ));
        tblDSNH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDSNHMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblDSNH);

        jPanel20.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel50.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(0, 102, 102));
        jLabel50.setText("Tìm kiếm:");

        btnFind.setForeground(new java.awt.Color(0, 102, 102));
        btnFind.setText("Tìm");
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindActionPerformed(evt);
            }
        });

        jButton1.setForeground(new java.awt.Color(0, 102, 102));
        jButton1.setText("Danh sách");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel50)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtFind)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnFind, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel50)
                    .addComponent(txtFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFind)
                    .addComponent(jButton1))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout tabDanhsachLayout = new javax.swing.GroupLayout(tabDanhsach);
        tabDanhsach.setLayout(tabDanhsachLayout);
        tabDanhsachLayout.setHorizontalGroup(
            tabDanhsachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabDanhsachLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabDanhsachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 909, Short.MAX_VALUE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        tabDanhsachLayout.setVerticalGroup(
            tabDanhsachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabDanhsachLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE))
        );

        tabNH.addTab("DANH SÁCH", tabDanhsach);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addComponent(tabNH)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel42)
                .addGap(326, 326, 326))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel42)
                .addGap(18, 18, 18)
                .addComponent(tabNH, javax.swing.GroupLayout.PREFERRED_SIZE, 554, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        clearForm();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindActionPerformed
        // TODO add your handling code here:
        find();
    }//GEN-LAST:event_btnFindActionPerformed

    private void tblDSNHMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDSNHMousePressed
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.row = tblDSNH.getSelectedRow();
            edit();
        }
    }//GEN-LAST:event_tblDSNHMousePressed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        // TODO add your handling code here:
        first();
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        // TODO add your handling code here:
        prev();
    }//GEN-LAST:event_btnPreviousActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        next();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        // TODO add your handling code here:
        last();
    }//GEN-LAST:event_btnLastActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        insert();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        detele();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        update();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        fillToTable();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportActionPerformed
        // TODO add your handling code here:
        File file = this.chonFileExcelImportNguoiHoc();
        if (file == null) {
            MsgBox.alert(this, "Lỗi dọc tập tin Excel!");
        } else {
            this.importNguoiHocFromExcel(file);
        }
    }//GEN-LAST:event_btnImportActionPerformed

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
            java.util.logging.Logger.getLogger(NguoiHocJDL.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NguoiHocJDL.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NguoiHocJDL.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NguoiHocJDL.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                NguoiHocJDL dialog = new NguoiHocJDL(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnImport;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnUpdate;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JRadioButton rdoNam;
    private javax.swing.JRadioButton rdoNu;
    private javax.swing.JPanel tabCapnhat5;
    private javax.swing.JPanel tabDanhsach;
    private javax.swing.JTabbedPane tabNH;
    private javax.swing.JTable tblDSNH;
    private javax.swing.JTextField txtBirthday;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFind;
    private javax.swing.JTextArea txtGhiChu;
    private javax.swing.JTextField txtIDNguoiHoc;
    private javax.swing.JTextField txtNameNguoiHoc;
    private javax.swing.JTextField txtPhoneNumber;
    // End of variables declaration//GEN-END:variables
}
