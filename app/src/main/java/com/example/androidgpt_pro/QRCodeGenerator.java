package com.example.androidgpt_pro;

import android.graphics.Bitmap;
import android.graphics.Color;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * QRCodeGenerator is a utility class responsible for generating QR code bitmaps.
 * It provides methods to create QR codes specific to event sign-up and check-in processes.
 * These QR codes are generated as Bitmap images which can be displayed in the application.
 *
 * No outstanding issues are currently identified in this class.
 */
public class QRCodeGenerator {
    /**
     * Generates a QR code bitmap for event sign-up.
     *
     * @param eventId The unique ID of the event.
     * @param width   The width of the QR code bitmap.
     * @param height  The height of the QR code bitmap.
     * @return Bitmap representing the QR code for sign-up.
     */
    // Method to generate Sign-up QR Code Bitmap
    public static Bitmap generateSignUpQRCodeBitmap(String eventId, int width, int height) {
        String data = "sign-up_" + eventId;
        return generateQRCodeBitmap(data, width, height);
    }

    /**
     * Generates a QR code bitmap for event check-in.
     *
     * @param eventId The unique ID of the event.
     * @param width   The width of the QR code bitmap.
     * @param height  The height of the QR code bitmap.
     * @return Bitmap representing the QR code for check-in.
     */
    // Method to generate Check-in QR Code Bitmap
    public static Bitmap generateCheckInQRCodeBitmap(String eventId, int width, int height) {
        String data = "check-in_" + eventId;
        return generateQRCodeBitmap(data, width, height);
    }

    /**
     * Private method to generate a QR code bitmap.
     *
     * @param data   The data to be encoded in the QR code.
     * @param width  The width of the QR code bitmap.
     * @param height The height of the QR code bitmap.
     * @return Bitmap of the generated QR code.
     */
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
