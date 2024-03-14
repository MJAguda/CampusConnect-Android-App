package com.ams.campusconnect.qrcode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.widget.ImageView;
import android.widget.Toast;

import com.ams.campusconnect.model.Employee;
import com.ams.campusconnect.model.School;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DownloadQR {

    private static Employee employee = Employee.getInstance();

    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;
    private ImageView imageView;

    private Bitmap bitmap;
    School school;

    public DownloadQR(ImageView imageView, School school) {
        this.imageView = imageView;
        this.school = school;
    }

    // TODO add sending Gmail
    public void downloadImage(Context context) {
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        if (storageManager == null) {
            Toast.makeText(context, "Storage manager is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageVolume storageVolume = null; // internal Storage
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            storageVolume = storageManager.getStorageVolumes().get(0);
        }

        File fileImage = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            String fileName = generateFileName();
            fileImage = new File(storageVolume.getDirectory().getPath() + "/Download/", fileName);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        bitmap = drawable.getBitmap();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] bytesArray = byteArrayOutputStream.toByteArray();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileImage);
            fileOutputStream.write(bytesArray);
            fileOutputStream.close();
            Toast.makeText(context, "Image saved to " + fileImage.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }
    private String generateFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy_HHmmss", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        return "QRCode_" + school.getSchoolID() + "_" + employee.getId() + "_" + employee.getFullName() + "_" + currentDateAndTime + ".png";
    }
}
