package com.example.lichnyefinansy;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Column;

public class Kategorii extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.kategorii);

        ListView spisok_kategoriy = findViewById(R.id.Listview_kategorii);

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("LichnyeFinansy.db", MODE_PRIVATE, null);
        Cursor zapros = db.rawQuery("SELECT nazvanie FROM kategorii", null);

        String[] massiv_kategoriy = new String[zapros.getCount()];
        int i = 0;
        while(zapros.moveToNext()) {

            String nazvanie = zapros.getString(0);
            massiv_kategoriy[i] = nazvanie;
            i++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, massiv_kategoriy);
        spisok_kategoriy.setAdapter(adapter);

    }

    public void dobavit_kategoriyu(View view){

        EditText pole_kategorii = findViewById(R.id.editTextText2);

        String nazvanie_kategorii = pole_kategorii.getText().toString();

        if(nazvanie_kategorii.equals("")){
            AlertDialog.Builder alertbuilder =new AlertDialog.Builder(this);
            alertbuilder.setTitle("Ошибка");
            alertbuilder.setMessage("Не заполнено наименование категории!");
            alertbuilder.setCancelable(true);
            alertbuilder.setPositiveButton("ОК", null);
            AlertDialog dialog  = alertbuilder.create();
            dialog.show();
            return;
        }

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("LichnyeFinansy.db", MODE_PRIVATE, null);
        Cursor zapros_kategotiya_est = db.rawQuery("SELECT nazvanie FROM kategorii WHERE UPPER(nazvanie) = ?",
                                                                    new String[]{nazvanie_kategorii.toUpperCase()});

        if(zapros_kategotiya_est.getCount() > 0){
            AlertDialog.Builder alertbuilder =new AlertDialog.Builder(this);
            alertbuilder.setTitle("Ошибка");
            alertbuilder.setMessage("Такая категория уже существует! Добавление невозможно!");
            alertbuilder.setCancelable(true);
            alertbuilder.setPositiveButton("ОК", null);
            AlertDialog dialog  = alertbuilder.create();
            dialog.show();
            return;
        }

        db.execSQL("INSERT INTO kategorii(nazvanie) VALUES (?)", new String[]{nazvanie_kategorii});

        Cursor zapros = db.rawQuery("SELECT nazvanie FROM kategorii", null);

        String[] massiv_kategoriy = new String[zapros.getCount()];
        int i = 0;
        while(zapros.moveToNext()) {

            String nazvanie = zapros.getString(0);
            massiv_kategoriy[i] = nazvanie;
            i++;
        }

        ListView spisok_kategoriy = findViewById(R.id.Listview_kategorii);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, massiv_kategoriy);
        spisok_kategoriy.setAdapter(adapter);

        AlertDialog.Builder alertbuilder =new AlertDialog.Builder(this);
        alertbuilder.setTitle("Инфо");
        alertbuilder.setMessage("Категория добавлена!");
        alertbuilder.setCancelable(true);
        alertbuilder.setPositiveButton("ОК", null);
        AlertDialog dialog  = alertbuilder.create();
        dialog.show();

    }

    public void udalit_kategoriyu(View view){

        EditText pole_kategorii = findViewById(R.id.editTextText2);

        String nazvanie_kategorii = pole_kategorii.getText().toString();

        if(nazvanie_kategorii.equals("")){
            AlertDialog.Builder alertbuilder =new AlertDialog.Builder(this);
            alertbuilder.setTitle("Ошибка");
            alertbuilder.setMessage("Не заполнено наименование категории!");
            alertbuilder.setCancelable(true);
            alertbuilder.setPositiveButton("ОК", null);
            AlertDialog dialog  = alertbuilder.create();
            dialog.show();
            return;
        }

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("LichnyeFinansy.db", MODE_PRIVATE, null);
        Cursor zapros_kategotiya_est = db.rawQuery("SELECT nazvanie FROM kategorii WHERE UPPER(nazvanie) = ?",
                new String[]{nazvanie_kategorii.toUpperCase()});

        if(zapros_kategotiya_est.getCount() == 0){
            AlertDialog.Builder alertbuilder =new AlertDialog.Builder(this);
            alertbuilder.setTitle("Ошибка");
            alertbuilder.setMessage("Такой категории не существует! Удаление невозможно!");
            alertbuilder.setCancelable(true);
            alertbuilder.setPositiveButton("ОК", null);
            AlertDialog dialog  = alertbuilder.create();
            dialog.show();
            return;
        }

        db.execSQL("DELETE FROM kategorii WHERE  UPPER(nazvanie) = ?", new String[]{nazvanie_kategorii.toUpperCase()});

        Cursor zapros = db.rawQuery("SELECT nazvanie FROM kategorii", null);

        String[] massiv_kategoriy = new String[zapros.getCount()];
        int i = 0;
        while(zapros.moveToNext()) {

            String nazvanie = zapros.getString(0);
            massiv_kategoriy[i] = nazvanie;
            i++;
        }

        ListView spisok_kategoriy = findViewById(R.id.Listview_kategorii);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, massiv_kategoriy);
        spisok_kategoriy.setAdapter(adapter);

        AlertDialog.Builder alertbuilder =new AlertDialog.Builder(this);
        alertbuilder.setTitle("Инфо");
        alertbuilder.setMessage("Категория удалена!");
        alertbuilder.setCancelable(true);
        alertbuilder.setPositiveButton("ОК", null);
        AlertDialog dialog  = alertbuilder.create();
        dialog.show();

    }

    public void perehod_na_nachalny_ekran(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
