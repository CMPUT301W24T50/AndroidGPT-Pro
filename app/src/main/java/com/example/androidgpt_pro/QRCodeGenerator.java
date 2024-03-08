package com.example.androidgpt_pro;

import android.graphics.Bitmap;
import android.graphics.Color;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeGenerator {

    // Method to generate Sign-up QR Code Bitmap
    public static Bitmap generateSignUpQRCodeBitmap(String eventId, int width, int height) {
        String data = "sign-up_" + eventId;
        return generateQRCodeBitmap(data, width, height);
    }

    // Method to generate Check-in QR Code Bitmap
    public static Bitmap generateCheckInQRCodeBitmap(String eventId, int width, int height) {
        String data = "check-in_" + eventId;
        return generateQRCodeBitmap(data, width, height);
    }

    // Private method to generate QR Code Bitmap from given data
    private static Bitmap generateQRCodeBitmap(String data, int width, int height) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, width, height);

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
