package com.ams.campusconnect;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ams.campusconnect.model.Employee;
import com.ams.campusconnect.model.EmployeeModel;
import com.ams.campusconnect.model.SaveData;
import com.ams.campusconnect.model.School;

import java.util.ArrayList;
import java.util.Calendar;

public class GenerateDTR extends AppCompatActivity {

    SaveData save = SaveData.getInstance();
    EmployeeModel employeeModel;
    School school;

    //private static final String TAG

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_dtr);

        // Get School Data
        school = (School) getIntent().getSerializableExtra("school");
        employeeModel = (EmployeeModel) getIntent().getSerializableExtra("employee");

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
        TableLayout table = findViewById(R.id.dtr_TableLayout);

        back.setOnClickListener(view -> {
            Intent intent = new Intent(GenerateDTR.this, Generate.class);
            intent.putExtra("school", school);
            startActivity(intent);
        });

        DTR dtr = new DTR(school, employeeModel);

        // Generate DTR
        generate.setOnClickListener(view -> {

            String month = monthSpinner.getSelectedItem().toString();
            String year = yearSpinner.getSelectedItem().toString();

            save.setMonth(month);
            save.setYear(year);

            //int day = DateUtils.getNumberOfDays(save.getMonth(), save.getYear());
            int day = DateUtils.getNumberOfDays(month, year);



            dtr.generateDTR(employeeModel.getId(), month, day, year, name, date, schoolHead, table, GenerateDTR.this);
        });

        download.setOnClickListener(view -> dtr.downloadDTR(findViewById(R.id.dtr_LinearLayout), GenerateDTR.this));
    }
}