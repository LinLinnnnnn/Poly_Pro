
package com.polypro.utils;


import com.polypro.dao.ThongKeDAO;
import com.polypro.entity.KhoaHoc;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;

public class ExcelUtil {
    
    public static Workbook printBangDiemKhoaHocToExcel(javax.swing.JTable tblBangDiem, 
        javax.swing.JComboBox<String> cbKhoaHoc, ThongKeDAO tkDAO) throws FileNotFoundException, IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("BangDiemExcel");
        
        KhoaHoc kh = (KhoaHoc) cbKhoaHoc.getSelectedItem();
        List<Object[]> list = tkDAO.getBangDiem(kh.getMaKH());
        
        int rownum = 0; 
        Cell cell; 
        Row row;
        // 
        HSSFCellStyle style = createStyleForTitle(workbook);
        
        row = sheet.createRow(rownum);
        
        //MaNH
        cell = row.createCell(0,CellType.STRING);
        cell.setCellValue("Student ID");
        cell.setCellStyle(style);
        
        //Họ Tên
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Full Name");
        cell.setCellStyle(style);
        
        //Điểm 
        cell = row.createCell(2,CellType.NUMERIC);
        cell.setCellValue("Point");
        cell.setCellStyle(style);
        
        //Xếp loại
        cell = row.createCell(3,CellType.STRING);
        cell.setCellValue("Classfication");
        cell.setCellStyle(style);
        
        //DATA 
        for(int i = 0; i<list.size();i++){
            rownum++;
            row = sheet.createRow(rownum);
            
            //ID Student
            cell = row.createCell(0,CellType.STRING);
            cell.setCellValue((String)tblBangDiem.getValueAt(i, 0));
            
            //FULL NAME
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue((String)tblBangDiem.getValueAt(i, 1));
            
            //POINT
            cell = row.createCell(2,CellType.NUMERIC);
            cell.setCellValue((Double)tblBangDiem.getValueAt(i, 2)); 
            
            //CLASSIFICATION
            cell = row.createCell(3,CellType.STRING);
            cell.setCellValue((String)tblBangDiem.getValueAt(i, 3));
            
        }
        return workbook;
      
    }
    
    private static HSSFCellStyle createStyleForTitle(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }
}
