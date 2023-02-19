package com.example.campusconnect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DownloadDTR {

    private static final float PDF_WIDTH_INCHES = 8.27f; // A4 paper width
    private static final float PDF_HEIGHT_INCHES = 11.69f; // A4 paper height
    private static final int COLUMN_MARGIN_DP = 20; // margin between columns in dp

    public static void downloadDTR(View view, Context context) {
        // Calculate the pixel dimensions based on the screen density
        int widthPixels = (int) (PDF_WIDTH_INCHES * context.getResources().getDisplayMetrics().densityDpi);
        int heightPixels = (int) (PDF_HEIGHT_INCHES * context.getResources().getDisplayMetrics().densityDpi);

        // Convert dp margin to pixels
        int columnMarginPixels = (int) (COLUMN_MARGIN_DP * context.getResources().getDisplayMetrics().density);

        // Calculate the width of each column
        int columnWidth = (widthPixels - columnMarginPixels) / 2;

        // Create a new PdfDocument object with the page dimensions
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(widthPixels, heightPixels, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        // Get the PDF canvas
        Canvas canvas = page.getCanvas();

        // Render the view to a bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas viewCanvas = new Canvas(bitmap);
        view.draw(viewCanvas);

        // Calculate the scale factor to fit each column
        float scale = (float) columnWidth / (float) bitmap.getWidth();

        // Draw the first copy of the layout
        canvas.save();
        canvas.translate(columnMarginPixels/2, 0);
        canvas.scale(scale, scale);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.restore();

        // Draw the second copy of the layout
        canvas.save();
        canvas.translate(columnMarginPixels/2 + columnWidth, 0);
        canvas.scale(scale, scale);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.restore();

        // Finish the page and document
        document.finishPage(page);

        // Save the PDF to external storage and show a toast with the file path
        String fileName = "DTR.pdf";
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(directory, fileName);
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            document.writeTo(outputStream);
            document.close();
            outputStream.flush();
            outputStream.close();
            Toast.makeText(context, "PDF saved to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
