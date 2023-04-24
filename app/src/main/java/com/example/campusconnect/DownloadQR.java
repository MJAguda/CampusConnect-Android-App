package com.example.campusconnect;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;

public class DownloadQR {

    static Employee employee = Employee.getInstance();
    static School school = School.getInstance();

    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;
    private ImageView imageView;
    private String fileName;
    private Bitmap bitmap;
    private FileOutputStream outputStream;

    public DownloadQR(ImageView imageView) {
        this.imageView = imageView;
    }

    // TODO add sending Gmail
    public void downloadImage() {
        fileName = "QRCode_" + school.getSchoolID() + "_" + employee.getId() + "_" + employee.getLastName() + ".png";
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        bitmap = drawable.getBitmap();
        if (ContextCompat.checkSelfPermission(imageView.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) imageView.getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
        } else {
            try {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();
                Toast.makeText(imageView.getContext(), "Image saved to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
