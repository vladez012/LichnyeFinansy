package com.example.lichnyefinansy;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.BoringLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OtchetVseDvizheniyaSredstv extends AppCompatActivity {

    private String kategoriya_vybor = "Не установлена";
    private DatePickerDialog.OnDateSetListener mDateSetListener, mDateSetListener2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.otchet_vse_dvizheniya_sredstv);

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("LichnyeFinansy.db", MODE_PRIVATE, null);

        Cursor zapros_dannye_dvizheniy = db.rawQuery("SELECT * FROM dvizheniyaSredstv", null);

        List<DannyeDvizheniyModel> dannye_list = new ArrayList<>();

        while(zapros_dannye_dvizheniy.moveToNext()) {
            dannye_list.add(new DannyeDvizheniyModel(zapros_dannye_dvizheniy.getString(1),
                    zapros_dannye_dvizheniy.getString(0),
                    zapros_dannye_dvizheniy.getString(2),
                    Double.toString(zapros_dannye_dvizheniy.getDouble(3))));
        }

        RecyclerView table_dannye = findViewById(R.id.recyclerView);
        table_dannye.setLayoutManager(new LinearLayoutManager(this));
        table_dannye.setAdapter(new CustomAdapterRecycleView(dannye_list));

        Cursor zapros = db.rawQuery("SELECT nazvanie FROM kategorii", null);

        String[] kategorii = new String[zapros.getCount()+1];

        int i = 1;
        kategorii[0] = "Не установлена";
        while(zapros.moveToNext()){
            kategorii[i] = zapros.getString(0);
            i = i + 1;
        }

        Spinner kategoriya = findViewById(R.id.spinner2);

        ArrayAdapter<String> adapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, kategorii);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

        kategoriya.setAdapter(adapter);

        OtchetVseDvizheniyaSredstv cont = this;

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                kategoriya_vybor = (String)parent.getItemAtPosition(position);

                Cursor zapros_dannye_dvizheniy = db.rawQuery("SELECT * FROM dvizheniyaSredstv", null);

                if(!kategoriya_vybor.equals("Не установлена")){
                    zapros_dannye_dvizheniy = db.rawQuery("SELECT * FROM dvizheniyaSredstv WHERE kategoriya=?", new String[]{kategoriya_vybor});
                }

                List<DannyeDvizheniyModel> dannye_list = new ArrayList<>();

                EditText data_ot = findViewById(R.id.editTextDate);
                EditText data_po = findViewById(R.id.editTextDate2);

                String data_ot_str = data_ot.getText().toString();
                String data_po_str = data_po.getText().toString();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy", Locale.ENGLISH);
                LocalDate date_ot_vybor;
                LocalDate date_po_vybor;

                if(!data_ot_str.equals("")) {
                    date_ot_vybor = LocalDate.parse(data_ot_str, formatter);
                }else{
                    date_ot_vybor = LocalDate.parse("01/1/2024", formatter);
                }

                if(!data_po_str.equals("")) {
                    date_po_vybor = LocalDate.parse(data_po_str, formatter);
                }else{
                    date_po_vybor = LocalDate.parse("01/1/2024", formatter);
                }

                Boolean access = false;


                while(zapros_dannye_dvizheniy.moveToNext()) {

                    access = false;

                    LocalDate date_bd = LocalDate.parse(zapros_dannye_dvizheniy.getString(1), formatter);

                    if(!data_ot_str.equals("") & !data_po_str.equals("")) {
                        if (((date_bd.isAfter(date_ot_vybor) || date_bd.isEqual(date_ot_vybor)) & (date_bd.isBefore(date_po_vybor) || date_bd.isEqual(date_po_vybor)))) {
                            access = true;
                        }
                    }else if (!data_ot_str.equals("")){
                        if ((date_bd.isAfter(date_ot_vybor) || date_bd.isEqual(date_ot_vybor))) {
                            access = true;
                        }
                    } else if (!data_po_str.equals("")) {
                        if ((date_bd.isBefore(date_po_vybor) || date_bd.isEqual(date_po_vybor))) {
                            access = true;
                        }
                    }else{access = true;}

                    if(access) {
                        dannye_list.add(new DannyeDvizheniyModel(zapros_dannye_dvizheniy.getString(1),
                                zapros_dannye_dvizheniy.getString(0),
                                zapros_dannye_dvizheniy.getString(2),
                                Double.toString(zapros_dannye_dvizheniy.getDouble(3))));
                    }
                }
                RecyclerView table_dannye = findViewById(R.id.recyclerView);
                table_dannye.setLayoutManager(new LinearLayoutManager(cont));
                table_dannye.setAdapter(new CustomAdapterRecycleView(dannye_list));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        };

        kategoriya.setOnItemSelectedListener(itemSelectedListener);

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date_ot_string = dayOfMonth + "/" + month + "/" + year;
                EditText data_ot = findViewById(R.id.editTextDate);
                EditText data_po = findViewById(R.id.editTextDate2);

                data_ot.setText(date_ot_string);

                Cursor zapros_dannye_dvizheniy = db.rawQuery("SELECT * FROM dvizheniyaSredstv", null);
                String data_ot_str = data_ot.getText().toString();
                String data_po_str = data_po.getText().toString();
                if(!data_ot_str.equals("")){

                    Boolean otbor_kategoriya;

                    List<DannyeDvizheniyModel> dannye_list = new ArrayList<>();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy", Locale.ENGLISH);
                    LocalDate date_ot_vybor = LocalDate.parse(data_ot_str, formatter);
                    LocalDate date_po_vybor= LocalDate.parse(data_ot_str, formatter);
                    if(!data_po_str.equals("")){
                        date_po_vybor = LocalDate.parse(data_po_str, formatter);
                    }

                    Boolean access = false;

                    while(zapros_dannye_dvizheniy.moveToNext()) {

                        access = false;

                        otbor_kategoriya = false;
                        if(kategoriya_vybor.equals("Не установлена") || (!kategoriya_vybor.equals("Не установлена") & zapros_dannye_dvizheniy.getString(2).equals(kategoriya_vybor))){
                            otbor_kategoriya = true;
                        }

                        LocalDate date_bd = LocalDate.parse(zapros_dannye_dvizheniy.getString(1), formatter);

                        if(!data_po_str.equals("")) {
                            if (((date_bd.isAfter(date_ot_vybor) || date_bd.isEqual(date_ot_vybor)) & (date_bd.isBefore(date_po_vybor) || date_bd.isEqual(date_po_vybor))) & otbor_kategoriya) {
                                access = true;
                            }
                        }else{
                            if ((date_bd.isAfter(date_ot_vybor) || date_bd.isEqual(date_ot_vybor)) & otbor_kategoriya) {
                                access = true;
                            }
                        }

                        if(access) {
                            dannye_list.add(new DannyeDvizheniyModel(zapros_dannye_dvizheniy.getString(1),
                                    zapros_dannye_dvizheniy.getString(0),
                                    zapros_dannye_dvizheniy.getString(2),
                                    Double.toString(zapros_dannye_dvizheniy.getDouble(3))));
                        }
                    }
                    RecyclerView table_dannye = findViewById(R.id.recyclerView);
                    table_dannye.setLayoutManager(new LinearLayoutManager(cont));
                    table_dannye.setAdapter(new CustomAdapterRecycleView(dannye_list));

                }

            }
        };

        mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date_ot_string = dayOfMonth + "/" + month + "/" + year;
                EditText data_po = findViewById(R.id.editTextDate2);
                EditText data_ot = findViewById(R.id.editTextDate);
                data_po.setText(date_ot_string);

                Cursor zapros_dannye_dvizheniy = db.rawQuery("SELECT * FROM dvizheniyaSredstv", null);
                String data_ot_str = data_ot.getText().toString();
                String data_po_str = data_po.getText().toString();
                if(!data_po_str.equals("")) {

                    Boolean otbor_kategoriya;

                    List<DannyeDvizheniyModel> dannye_list = new ArrayList<>();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy", Locale.ENGLISH);
                    LocalDate date_ot_vybor = LocalDate.parse(data_po_str, formatter);
                    LocalDate date_po_vybor = LocalDate.parse(data_po_str, formatter);
                    ;
                    if (!data_ot_str.equals("")) {
                        date_ot_vybor = LocalDate.parse(data_ot_str, formatter);
                    }

                    Boolean access = false;

                    while (zapros_dannye_dvizheniy.moveToNext()) {

                        access = false;

                        otbor_kategoriya = false;
                        if (kategoriya_vybor.equals("Не установлена") || (!kategoriya_vybor.equals("Не установлена") & zapros_dannye_dvizheniy.getString(2).equals(kategoriya_vybor))) {
                            otbor_kategoriya = true;
                        }

                        LocalDate date_bd = LocalDate.parse(zapros_dannye_dvizheniy.getString(1), formatter);

                        if (!data_ot_str.equals("")) {
                            if (((date_bd.isAfter(date_ot_vybor) || date_bd.isEqual(date_ot_vybor)) & (date_bd.isBefore(date_po_vybor) || date_bd.isEqual(date_po_vybor))) & otbor_kategoriya) {
                                access = true;
                            }
                        } else {
                            if ((date_bd.isBefore(date_po_vybor) || date_bd.isEqual(date_po_vybor)) & otbor_kategoriya) {
                                access = true;
                            }
                        }

                        if (access) {
                            dannye_list.add(new DannyeDvizheniyModel(zapros_dannye_dvizheniy.getString(1),
                                    zapros_dannye_dvizheniy.getString(0),
                                    zapros_dannye_dvizheniy.getString(2),
                                    Double.toString(zapros_dannye_dvizheniy.getDouble(3))));
                        }
                    }
                    RecyclerView table_dannye = findViewById(R.id.recyclerView);
                    table_dannye.setLayoutManager(new LinearLayoutManager(cont));
                    table_dannye.setAdapter(new CustomAdapterRecycleView(dannye_list));
                }
            }
        };

    }

    public void perehod_na_nachalny_ekran(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void ustanovit_datu_ot(View view){

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

    public void ustanovit_datu_po(View view){

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this,
                android.R.style.Theme_Holo_Dialog_MinWidth,
                mDateSetListener2, year, month, day);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    public void sbrosit_filtry_po_datam(View view){

        EditText data_ot = findViewById(R.id.editTextDate);
        data_ot.setText("");
        EditText data_po = findViewById(R.id.editTextDate2);
        data_po.setText("");
        Spinner kategoriya = findViewById(R.id.spinner2);
        kategoriya.setSelection(0);

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("LichnyeFinansy.db", MODE_PRIVATE, null);
        Cursor zapros_dannye_dvizheniy = db.rawQuery("SELECT * FROM dvizheniyaSredstv", null);

        List<DannyeDvizheniyModel> dannye_list = new ArrayList<>();

        while(zapros_dannye_dvizheniy.moveToNext()) {
            dannye_list.add(new DannyeDvizheniyModel(zapros_dannye_dvizheniy.getString(1),
                    zapros_dannye_dvizheniy.getString(0),
                    zapros_dannye_dvizheniy.getString(2),
                    Double.toString(zapros_dannye_dvizheniy.getDouble(3))));
        }
        RecyclerView table_dannye = findViewById(R.id.recyclerView);
        table_dannye.setLayoutManager(new LinearLayoutManager(this));
        table_dannye.setAdapter(new CustomAdapterRecycleView(dannye_list));

    }

}
