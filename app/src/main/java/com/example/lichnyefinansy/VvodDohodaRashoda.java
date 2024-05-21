package com.example.lichnyefinansy;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class VvodDohodaRashoda extends AppCompatActivity {

    String dvizhenie_vybor = "Приход";
    String kategoriya_vybor = "Зарплата";
    String Data_vybor = "";
    Double summa_vybor = 0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.vvod_dohoda_roshoda);

        CalendarView calendar = findViewById(R.id.calendarView);
        VvodDohodaRashoda cont = this;
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth){
                String text = Integer.toString(dayOfMonth)+"/"+Integer.toString(month)+"/"+Integer.toString(year);
                Data_vybor = dayOfMonth+"/"+(month+1)+"/"+year;
            }
        });


        Data_vybor = new SimpleDateFormat("dd/M/yyyy").format(Calendar.getInstance().getTime());


        RadioButton rb_prihod = findViewById(R.id.radioButton2);
        rb_prihod.setChecked(true);

        EditText summa = findViewById(R.id.editTextNumberDecimal);
        summa.setText(Double.toString(summa_vybor));

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("LichnyeFinansy.db", MODE_PRIVATE, null);
        Cursor zapros = db.rawQuery("SELECT nazvanie FROM kategorii", null);

        String[] kategorii = new String[zapros.getCount()];

        int i = 0;
        while(zapros.moveToNext()){
            kategorii[i] = zapros.getString(0);
            i = i + 1;
        }

        Spinner kategoriya = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, kategorii);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

        kategoriya.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kategoriya_vybor = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        };

        kategoriya.setOnItemSelectedListener(itemSelectedListener);

    }

    public void onRadioButtonVyborDvizhenyaClicked(View view){
        RadioButton rb = (RadioButton) view;

        boolean checked_rb = rb.isChecked();

        dvizhenie_vybor = rb.getText().toString();

    }

    public void perehod_na_nachalny_ekran(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void zapisat_dannye_v_bd(View view){

        EditText summa = findViewById(R.id.editTextNumberDecimal);
        summa_vybor = Double.parseDouble(summa.getText().toString());

        if(summa_vybor==0){
            AlertDialog.Builder alertbuilder =new AlertDialog.Builder(this);
            alertbuilder.setTitle("Ошибка");
            alertbuilder.setMessage("Сумма не может быть равна нулю!");
            alertbuilder.setCancelable(true);
            alertbuilder.setPositiveButton("ОК", null);
            AlertDialog dialog  = alertbuilder.create();
            dialog.show();
            return;
        }

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("LichnyeFinansy.db", MODE_PRIVATE, null);

        ContentValues dannye = new ContentValues();
        dannye.put("vidDvizheniya",dvizhenie_vybor);
        dannye.put("dataDvizheniya",Data_vybor);
        dannye.put("kategoriya",kategoriya_vybor);
        dannye.put("summa",summa_vybor);

        db.insert("dvizheniyaSredstv", null, dannye);

        AlertDialog.Builder alertbuilder =new AlertDialog.Builder(this);
        alertbuilder.setTitle("Информация");
        alertbuilder.setMessage("Информация записана в базу данных");
        alertbuilder.setCancelable(true);
        alertbuilder.setPositiveButton("ОК", null);
        AlertDialog dialog  = alertbuilder.create();
        dialog.show();

    }
}
