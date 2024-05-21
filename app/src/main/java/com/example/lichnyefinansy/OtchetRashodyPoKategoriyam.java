package com.example.lichnyefinansy;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class OtchetRashodyPoKategoriyam extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.otchet_rashode_po_kategoriyam);

        PieChartView pieChartView = findViewById(R.id.piechart1);

        Calendar tek_data = Calendar.getInstance();
        int tek_year = tek_data.get(Calendar.YEAR);
        int tek_month = tek_data.get(Calendar.MONTH) + 1;
        int tek_day = tek_data.get(Calendar.DAY_OF_MONTH);

        String tek_data_str = tek_day + "/" + tek_month + "/" + tek_year;

        EditText data_filtr = findViewById(R.id.editTextDate3);
        data_filtr.setText(tek_data_str);

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("LichnyeFinansy.db", MODE_PRIVATE, null);
        Cursor zapros = db.rawQuery("SELECT kategoriya, SUM(summa) FROM dvizheniyaSredstv WHERE vidDvizheniya='Расход' AND dataDvizheniya = ? GROUP BY kategoriya", new String[]{tek_data_str});

        List<SliceValue> pieData = new ArrayList<>();

        //Random rnd = new Random();

        while(zapros.moveToNext()) {
            Float summa = (float) zapros.getDouble(1);
            String zagolovok = zapros.getString(0) + " (" + Float.toString(summa) + ")";
            pieData.add(new SliceValue(summa, ChartUtils.pickColor()).setLabel(zagolovok));
        }


        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true);
        pieChartView.setPieChartData(pieChartData);

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                month = month + 1;
                String data_string = dayOfMonth + "/" + month + "/" + year;
                EditText data = findViewById(R.id.editTextDate3);

                data.setText(data_string);

                Cursor zapros = db.rawQuery("SELECT kategoriya, SUM(summa) FROM dvizheniyaSredstv WHERE vidDvizheniya='Расход' GROUP BY kategoriya", null);

                if(!data_string.equals("")){
                    zapros = db.rawQuery("SELECT kategoriya, SUM(summa) FROM dvizheniyaSredstv WHERE vidDvizheniya='Расход' AND dataDvizheniya = ? GROUP BY kategoriya", new String[]{data_string});
                }

                List<SliceValue> pieData = new ArrayList<>();

                Random rnd = new Random();

                while(zapros.moveToNext()) {
                    Float summa = (float) zapros.getDouble(1);
                    String zagolovok = zapros.getString(0) + " (" + Float.toString(summa) + ")";
                    pieData.add(new SliceValue(summa, Color.argb(255,
                            rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))).setLabel(zagolovok));
                }

                PieChartData pieChartData = new PieChartData(pieData);
                pieChartData.setHasLabels(true);
                pieChartView.setPieChartData(pieChartData);



            }
        };

    }

    public void ustanovit_datu(View view){

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this,
                android.R.style.Theme_Holo_Dialog_MinWidth,
                mDateSetListener, year, month, day);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    public void sbrosit_filtr(View view){

        EditText data = findViewById(R.id.editTextDate3);
        data.setText("");

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("LichnyeFinansy.db", MODE_PRIVATE, null);
        Cursor zapros = db.rawQuery("SELECT kategoriya, SUM(summa) FROM dvizheniyaSredstv WHERE vidDvizheniya='Расход' GROUP BY kategoriya", null);

        List<SliceValue> pieData = new ArrayList<>();

        Random rnd = new Random();

        while(zapros.moveToNext()) {
            Float summa = (float) zapros.getDouble(1);
            String zagolovok = zapros.getString(0) + " (" + Float.toString(summa) + ")";
            pieData.add(new SliceValue(summa, Color.argb(255,
                    rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))).setLabel(zagolovok));
        }

        PieChartView pieChartView = findViewById(R.id.piechart1);

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true);
        pieChartView.setPieChartData(pieChartData);

    }

    public void perehod_na_nachalny_ekran(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
