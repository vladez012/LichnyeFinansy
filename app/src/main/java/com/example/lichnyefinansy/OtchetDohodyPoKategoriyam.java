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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;

public class OtchetDohodyPoKategoriyam extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.otchet_dohody_po_kategoriyam);

        ColumnChartView columnChartView = findViewById(R.id.columnchart1);

        Calendar tek_data = Calendar.getInstance();
        int tek_year = tek_data.get(Calendar.YEAR);
        int tek_month = tek_data.get(Calendar.MONTH) + 1;
        int tek_day = tek_data.get(Calendar.DAY_OF_MONTH);

        String tek_data_str = tek_day + "/" + tek_month + "/" + tek_year;

        EditText data_filtr = findViewById(R.id.editTextDate4);
        data_filtr.setText(tek_data_str);

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("LichnyeFinansy.db", MODE_PRIVATE, null);
        Cursor zapros = db.rawQuery("SELECT kategoriya, SUM(summa) FROM dvizheniyaSredstv WHERE vidDvizheniya='Приход' AND dataDvizheniya = ? GROUP BY kategoriya", new String[]{tek_data_str});

        List<Column> columns = new ArrayList<Column>();



        while(zapros.moveToNext()) {

            Float summa = (float) zapros.getDouble(1);
            String zagolovok = zapros.getString(0) + " (" + Float.toString(summa) + ")";

            List<SubcolumnValue> values = new ArrayList<SubcolumnValue>();

            values.add(new SubcolumnValue(summa, ChartUtils.pickColor()).setLabel(zagolovok));

            Column column = new Column(values);

            column.setHasLabels(true);

            columns.add(column);

        }

        ColumnChartData data = new ColumnChartData(columns);

        Axis axisX = new Axis();
        Axis axisY = new Axis();

        axisX.setName("Категория");
        axisY.setName("Доход");

        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        columnChartView.setColumnChartData(data);

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                month = month + 1;
                String data_string = dayOfMonth + "/" + month + "/" + year;
                EditText data_vybor = findViewById(R.id.editTextDate4);

                data_vybor.setText(data_string);

                Cursor zapros = db.rawQuery("SELECT kategoriya, SUM(summa) FROM dvizheniyaSredstv WHERE vidDvizheniya='Приход' GROUP BY kategoriya", null);

                if(!data_string.equals("")){
                    zapros = db.rawQuery("SELECT kategoriya, SUM(summa) FROM dvizheniyaSredstv WHERE vidDvizheniya='Приход' AND dataDvizheniya = ? GROUP BY kategoriya", new String[]{data_string});
                }

                List<Column> columns = new ArrayList<Column>();

                while(zapros.moveToNext()) {

                    Float summa = (float) zapros.getDouble(1);
                    String zagolovok = zapros.getString(0) + " (" + Float.toString(summa) + ")";

                    List<SubcolumnValue> values = new ArrayList<SubcolumnValue>();

                    values.add(new SubcolumnValue(summa, ChartUtils.pickColor()).setLabel(zagolovok));

                    Column column = new Column(values);

                    column.setHasLabels(true);

                    columns.add(column);

                }

                ColumnChartData data = new ColumnChartData(columns);

                Axis axisX = new Axis();
                Axis axisY = new Axis();

                axisX.setName("Категория");
                axisY.setName("Доход");

                data.setAxisXBottom(axisX);
                data.setAxisYLeft(axisY);

                columnChartView.setColumnChartData(data);

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

        EditText data_vybor = findViewById(R.id.editTextDate4);
        data_vybor.setText("");

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("LichnyeFinansy.db", MODE_PRIVATE, null);
        Cursor zapros = db.rawQuery("SELECT kategoriya, SUM(summa) FROM dvizheniyaSredstv WHERE vidDvizheniya='Приход' GROUP BY kategoriya", null);

        List<Column> columns = new ArrayList<Column>();

        while(zapros.moveToNext()) {

            Float summa = (float) zapros.getDouble(1);
            String zagolovok = zapros.getString(0) + " (" + Float.toString(summa) + ")";

            List<SubcolumnValue> values = new ArrayList<SubcolumnValue>();

            values.add(new SubcolumnValue(summa, ChartUtils.pickColor()).setLabel(zagolovok));

            Column column = new Column(values);

            column.setHasLabels(true);

            columns.add(column);

        }

        ColumnChartData data = new ColumnChartData(columns);

        Axis axisX = new Axis();
        Axis axisY = new Axis();

        axisX.setName("Категория");
        axisY.setName("Доход");

        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        ColumnChartView columnChartView = findViewById(R.id.columnchart1);

        columnChartView.setColumnChartData(data);

    }


    public void perehod_na_nachalny_ekran(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
