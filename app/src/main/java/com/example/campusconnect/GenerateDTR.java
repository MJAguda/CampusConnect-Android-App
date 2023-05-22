package com.example.campusconnect;

import static android.content.ContentValues.TAG;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class GenerateDTR extends AppCompatActivity {

    SaveData save = SaveData.getInstance();
    School school = School.getInstance();
    Employee employee = Employee.getInstance();
    Read read = new Read();

    private static final int PERMISSION_REQUEST_CODE = 10;
    //private static final String TAG
    int pdfHeight = 1080;
    int pdfWidth = 720;
    private PdfDocument document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_dtr);

        // Find button in the Layout
        ImageButton back = findViewById(R.id.backButton_ImageButton);
        Button generate = findViewById(R.id.generateDTR_Button);
        Button download = findViewById(R.id.downloadDTR_Button);

        //Find the spinner in the layout
        Spinner monthSpinner = findViewById(R.id.month_Spinner);
        Spinner yearSpinner = findViewById(R.id.year_Spinner);

        // Create an ArrayList for the month
        ArrayList<String> monthList = new ArrayList<>();
        monthList.add("January");
        monthList.add("February");
        monthList.add("March");
        monthList.add("April");
        monthList.add("May");
        monthList.add("June");
        monthList.add("July");
        monthList.add("August");
        monthList.add("September");
        monthList.add("October");
        monthList.add("November");
        monthList.add("December");

        // Create an ArrayAdapter for the month spinner
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, monthList);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        // Create an ArrayList for the year
        ArrayList<String> yearList = new ArrayList<>();
        for (int i = Calendar.getInstance().get(Calendar.YEAR); i >= Calendar.getInstance().get(Calendar.YEAR) - 100; i--) {
            yearList.add(String.valueOf(i));
        }

        // Create an ArrayAdapter for the year spinner
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        // Declare components
        TextView name = findViewById(R.id.name_TextView);
        TextView date = findViewById(R.id.monthyear_TextView);
        TextView schoolHead = findViewById(R.id.schoolHead_TextView);
        TableLayout table = (TableLayout) findViewById(R.id.dtr_TableLayout);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (GenerateDTR.this, Generate.class);
                startActivity(intent);
            }
        });

        // Generate DTR
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String month = monthSpinner.getSelectedItem().toString();
                String year = yearSpinner.getSelectedItem().toString();

                save.setMonth(month);
                save.setYear(year);

                //int day = DateUtils.getNumberOfDays(save.getMonth(), save.getYear());
                int day = DateUtils.getNumberOfDays(month, year);

                DTR.generateDTR(employee.getId(), month, day, year, name, date, schoolHead, table, GenerateDTR.this);
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DTR.downloadDTR(findViewById(R.id.dtr_LinearLayout), GenerateDTR.this);
            }
        });
    }
}