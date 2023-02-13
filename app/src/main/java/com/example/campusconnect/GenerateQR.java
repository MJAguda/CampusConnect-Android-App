package com.example.campusconnect;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class GenerateQR {

    public Bitmap generateQRCode(String inputText) {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(inputText, BarcodeFormat.QR_CODE, 200, 200, null);
        } catch (WriterException e) {
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 200, 0, 0, w, h);
        return bitmap;
    }
}
/*
// Instance
GenerateQR generateQR = new GenerateQR();

// Declare ImageView
ImageView qr = findViewById(R.id.qrCode_ImageView);
// call generateQRCode method from GenerateQR class
qr.setImageBitmap(generateQR.generateQRCode(save.getId()));
 */
