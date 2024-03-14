package com.ams.campusconnect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.ams.campusconnect.firebase.Read;
import com.ams.campusconnect.model.Employee;
import com.ams.campusconnect.model.SaveData;
import com.ams.campusconnect.model.School;
import com.ams.campusconnect.model.SchoolModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DTR {
    static Employee employee = Employee.getInstance();
    static SaveData save = SaveData.getInstance();

    static Read read = new Read();

    static School school = School.getInstance();
    SchoolModel schoolModel;

    private static final float PDF_WIDTH_INCHES = 8.27f; // A4 paper width
    private static final float PDF_HEIGHT_INCHES = 11.69f; // A4 paper height
    private static final int COLUMN_MARGIN_DP = 20; // margin between columns in dp


    // Constructor
    public DTR() {
    }

    public DTR(SchoolModel schoolModel) {
        this.schoolModel = schoolModel;
    }


    public interface GenerateDTRCallback {
        void onGenerateDTRFinished();
    }

    // TODO add sending Gmail
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
        String fileName = generateFileName();
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(directory, fileName);
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            document.writeTo(outputStream);
            document.close();
            outputStream.flush();
            outputStream.close();
            Toast.makeText(context, "PDF saved to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            Log.d("Status: ", "Downloaded");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy_HHmmss", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        return "DTR_" + school.getSchoolID() + "_" + employee.getId() + "_" + employee.getFullName() + "_" + save.getMonth() + ", " + save.getYear() + "_" + currentDateAndTime + ".pdf";
//        return "QRCode_" + school.getSchoolID() + "_" + employee.getId() + "_" + employee.getFullName() + "_" + currentDateAndTime + ".png";
    }


    public void generateDTR(String id, String month, int day, String year, TextView name, TextView date, TextView schoolHead, TableLayout table, Context context, GenerateDTRCallback callback) {
        read.readRecord(schoolModel.getSchoolID() + "/employee/" + employee.getId(), new Read.OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("fullname").getValue().toString());
                date.setText(String.format("%s %s", month, year));

                read.readRecord(schoolModel.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + year + "/" + month, new Read.OnGetDataListener() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {


                        table.removeAllViews();

                        for(int i = 1 ; i <= day ; i++){

                            DataSnapshot child = dataSnapshot.child(String.valueOf(i));

                            // Instance of the row
                            TableRow row = new TableRow(context);

                            // Add day to the row
                            TextView day = new TextView(context);
                            day.setText(String.format("%02d", Integer.parseInt(child.getKey())));
                            day.setTextColor(Color.BLACK);
                            day.setTextSize(7);
                            day.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            day.setPadding(0,10,0,10);
                            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
                            day.setLayoutParams(params);
                            day.setBackground(ContextCompat.getDrawable(context, R.drawable.table_border));

                            row.addView(day);
                            // Add time TextView to the row
                            for(DataSnapshot grandChild : child.getChildren()){
                                //Log.d("Time", grandChild.getKey() + " : " + grandChild.getValue());

                                // Add time to the row
                                TextView time = new TextView(context);

                                time.setText(grandChild.getValue().toString());
                                time.setTextSize(7);
                                time.setTextColor(Color.BLACK);
                                time.setLayoutParams(params);
                                time.setGravity(Gravity.CENTER);
                                time.setPadding(0,10,0,10);
                                time.setBackground(ContextCompat.getDrawable(context, R.drawable.table_border));

                                row.addView(time);
                            }

                            for(int j = 1 ; j <= 2 ; j++){
                                TextView blank = new TextView(context);

                                blank.setText(" ");
                                blank.setTextSize(7);
                                blank.setTextColor(Color.BLACK);
                                blank.setLayoutParams(params);
                                blank.setGravity(Gravity.CENTER);
                                blank.setPadding(0,10,0,10);
                                blank.setBackground(ContextCompat.getDrawable(context, R.drawable.table_border));

                                row.addView(blank);
                            }
                            table.addView(row);
                        }
                    }

                    @Override
                    public void onFailure(DatabaseError databaseError) {
                        Log.d("Read", "Error: " + databaseError.getMessage());
                        Toast.makeText(context, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


                // Set School head
                schoolHead.setText(schoolModel.getSchoolHead());
                Log.d("Status:", "Generated");

                callback.onGenerateDTRFinished(); // Invoke the callback to signal completion
            }

            @Override
            public void onFailure(DatabaseError databaseError) {
                Log.d("Read", "Error: " + databaseError.getMessage());
                Toast.makeText(context, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void generateDTR(String id, String month, int day, String year, TextView name, TextView date, TextView schoolHead, TableLayout table, Context context) {
        read.readRecord(school.getSchoolID() + "/employee/" + employee.getId(), new Read.OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("fullname").getValue().toString());
                date.setText(String.format("%s %s", month, year));

                read.readRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + year + "/" + month, new Read.OnGetDataListener() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {


                        table.removeAllViews();

                        for(int i = 1 ; i <= day ; i++){

                            DataSnapshot child = dataSnapshot.child(String.valueOf(i));

                            // Instance of the row
                            TableRow row = new TableRow(context);

                            // Add day to the row
                            TextView day = new TextView(context);
                            day.setText(String.format("%02d", Integer.parseInt(child.getKey())));
                            day.setTextColor(Color.BLACK);
                            day.setTextSize(7);
                            day.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            day.setPadding(0,10,0,10);
                            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
                            day.setLayoutParams(params);
                            day.setBackground(ContextCompat.getDrawable(context, R.drawable.table_border));

                            row.addView(day);
                            // Add time TextView to the row
                            for(DataSnapshot grandChild : child.getChildren()){
                                //Log.d("Time", grandChild.getKey() + " : " + grandChild.getValue());

                                // Add time to the row
                                TextView time = new TextView(context);

                                time.setText(grandChild.getValue().toString());
                                time.setTextSize(7);
                                time.setTextColor(Color.BLACK);
                                time.setLayoutParams(params);
                                time.setGravity(Gravity.CENTER);
                                time.setPadding(0,10,0,10);
                                time.setBackground(ContextCompat.getDrawable(context, R.drawable.table_border));

                                row.addView(time);
                            }

                            for(int j = 1 ; j <= 2 ; j++){
                                TextView blank = new TextView(context);

                                blank.setText(" ");
                                blank.setTextSize(7);
                                blank.setTextColor(Color.BLACK);
                                blank.setLayoutParams(params);
                                blank.setGravity(Gravity.CENTER);
                                blank.setPadding(0,10,0,10);
                                blank.setBackground(ContextCompat.getDrawable(context, R.drawable.table_border));

                                row.addView(blank);
                            }
                            table.addView(row);
                        }
                    }

                    @Override
                    public void onFailure(DatabaseError databaseError) {
                        Log.d("Read", "Error: " + databaseError.getMessage());
                        Toast.makeText(context, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


                // Set School head
                schoolHead.setText(school.getSchoolHead());
                Log.d("Status:", "Generated");
            }

            @Override
            public void onFailure(DatabaseError databaseError) {
                Log.d("Read", "Error: " + databaseError.getMessage());
                Toast.makeText(context, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
