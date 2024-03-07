/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.polypro.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Hashtable;
import javax.imageio.ImageIO;

/**
 *
 * @author Ha Thanh Liem
 */
public class QRCodeUtil {
    public static void main(String[] args) throws WriterException, IOException {
		String qrCodeText = "Welcome to DuAnMau-UDPM-Java";
                String filePath = "DuAnMau.jpg";
                File destination = new File("storeFiles", filePath);
                
                filePath = Paths.get(destination.getAbsolutePath()).toString();
		int size = 200;
		String fileType = "jpg";
		File qrFile = new File(filePath);
		createQRImage(qrFile, qrCodeText, size, fileType);
		System.out.println("DONE " + filePath);
	}
    public static void createQRImage(File qrFile, String qrCodeText, int size, String fileType)
			throws WriterException, IOException {
        // Create the ByteMatrix for the QR-Code that encodes the given String
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, 
                                            size, hintMap);
        // Make the BufferedImage that are to hold the QRCode
        int matrixWidth = byteMatrix.getWidth();
        BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixWidth);
        // Paint and save the image using the ByteMatrix
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < matrixWidth; i++) {
                for (int j = 0; j < matrixWidth; j++) {
                        if (byteMatrix.get(i, j)) {
                                graphics.fillRect(i, j, 1, 1);
                        }
                }
        }
        ImageIO.write(image, fileType, qrFile);
    }
}
