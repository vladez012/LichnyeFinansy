package com.example.lichnyefinansy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.nio.channels.Channel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("LichnyeFinansy.db", MODE_PRIVATE, null);


        db.execSQL("CREATE TABLE IF NOT EXISTS dvizheniyaSredstv(vidDvizheniya TEXT, dataDvizheniya TEXT, kategoriya TEXT, summa REAL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS kategorii(nazvanie TEXT, UNIQUE(nazvanie))");

        db.execSQL("INSERT OR IGNORE INTO kategorii(nazvanie) VALUES ('Зарплата'), ('Продукты'), ('Обучение'), ('ЖКХ')");

        String tek_data_zagolovok = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());

        TextView zagolovok = findViewById(R.id.textView8);

        String text_zagolovka = zagolovok.getText().toString();
        zagolovok.setText(text_zagolovka + tek_data_zagolovok);

        String tek_data_zapros = new SimpleDateFormat("dd/M/yyyy").format(Calendar.getInstance().getTime());

        Cursor zapros_dohod = db.rawQuery("SELECT summa FROM dvizheniyaSredstv WHERE vidDvizheniya='Приход' AND dataDvizheniya=?", new String[]{tek_data_zapros});
        Cursor zapros_rashod = db.rawQuery("SELECT summa FROM dvizheniyaSredstv WHERE vidDvizheniya='Расход' AND dataDvizheniya=?", new String[]{tek_data_zapros});

        Double summa_dohod = 0.0;
        Double summa_rashod = 0.0;


        while(zapros_dohod.moveToNext()){
            summa_dohod = summa_dohod + zapros_dohod.getDouble(0);
        }

        while(zapros_rashod.moveToNext()){
            summa_rashod = summa_rashod + zapros_rashod.getDouble(0);
        }

        TextView tw_dohod = findViewById(R.id.textView);
        TextView tw_rashod = findViewById(R.id.textView3);

        String text_dohod = "Доход: " + Double.toString(summa_dohod);
        Spannable span_dohod = new SpannableString(text_dohod);
        span_dohod.setSpan(new ForegroundColorSpan(Color.rgb(0,100,0)), 7, (text_dohod).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tw_dohod.setText(span_dohod, TextView.BufferType.SPANNABLE);

        String text_rashod = "Расход: " + Double.toString(summa_rashod);
        Spannable span_rashod = new SpannableString(text_rashod);
        span_rashod.setSpan(new ForegroundColorSpan(Color.RED), 8, (text_rashod).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tw_rashod.setText(span_rashod, TextView.BufferType.SPANNABLE);


        String[] quotes = new String[30];
        quotes[0]="Деньги любят тишину";
        quotes[1]="Деньги потеряют свою ценность, если они просто лежат где-то без дела";
        quotes[2]="Финансовая свобода - это не тратить деньги на вещи, которые вам не нужны";
        quotes[3]="Деньги не приносят счастья, но они делают жизнь более комфортной";
        quotes[4]="Если хочешь понять жизнь, изучи не ее философию, а бухгалтерию";
        quotes[5]="Деньги, которые ты зарабатываешь, это мера твоей ценности на рынке";
        quotes[6]="Деньги - это не плохо, пока они работают на вас, а не вы на них";
        quotes[7]="Финансовое образование - это ключ к финансовой независимости";
        quotes[8]="Лучший способ увеличить доход - это не экономия, а увеличение дохода";
        quotes[9]="Деньги - это не конечная цель, а средство достижения своих целей";
        quotes[10]="Деньги - это не корень всех зол. Их отсутствие - корень многих проблем";
        quotes[11]="Зная, как управлять деньгами, можно быть уверенным в своем финансовом будущем";
        quotes[12]="Деньги не умеют разговаривать, но они многое говорят о человеке";
        quotes[13]="Финансовая независимость - это та точка, когда деньги начинают работать на вас";
        quotes[14]="Сначала научитесь управлять своими деньгами, а потом позволяйте им работать на вас.";
        quotes[15]="Важно не сколько зарабатывать, а как правильно распоряжаться заработанными деньгами";
        quotes[16]="Денежные инвестиции - это инвестиции в своё будущее";
        quotes[17]="Не доверяйте деньгам, лучше доверьтесь знаниям и умению их умножать";
        quotes[18]="Деньги могут создать проблемы, если не понимать их природу";
        quotes[19]="Правильно распределенные деньги - это залог финансовой стабильности";
        quotes[20]="Деньги - это не только средство, но и результат труда";
        quotes[21]="Инвестировать следует не только в активы, но и в собственное знание";
        quotes[22]="Не все деньги составляют счастье, но верное использование денег - это одно из условий счастья";
        quotes[23]="Без целей деньги не теряют смысл, но теряют направление";
        quotes[24]="Денежки любят тех, кто обращается с ними грамотно.";
        quotes[25]="Контролируйте свои расходы, иначе они начнут контролировать вас";
        quotes[26]="Деньги быстро уходят из рук тех, кто не умеет ими управлять";
        quotes[27]="Деньги - это не мера успеха, а результат правильной работы";
        quotes[28]="В финансах важно не сколько зарабатывать, а сколько оставлять";
        quotes[29]="Лучший способ предсказать будущее – создать его";

        Random rnd = new Random();

        TextView citata = findViewById(R.id.textView18);

        citata.setText(quotes[rnd.nextInt(30)]);

    }

    public void perehod_na_vvod_dohodov_rashodov(View view){
        Intent intent = new Intent(this, VvodDohodaRashoda.class);
        startActivity(intent);
    }

    public void perehod_na_otchet_vse_dvizheniya_sredstv(View view){
        Intent intent = new Intent(this, OtchetVseDvizheniyaSredstv.class);
        startActivity(intent);
    }

    public void perehod_na_otchet_rashody_po_kategoriyam(View view){
        Intent intent = new Intent(this, OtchetRashodyPoKategoriyam.class);
        startActivity(intent);
    }

    public void perehod_na_otchet_dohody_po_kategoriyam(View view){
        Intent intent = new Intent(this, OtchetDohodyPoKategoriyam.class);
        startActivity(intent);
    }

    public void perehod_na_kategorii(View view){
        Intent intent = new Intent(this, Kategorii.class);
        startActivity(intent);
    }

    public void ob_avtore(View view){
        AlertDialog.Builder alertbuilder =new AlertDialog.Builder(this);
        alertbuilder.setTitle("Об авторе");
        alertbuilder.setMessage(Html.fromHtml("<b>Финансовый университет</b><br><br>"
                                                + "Разработчик: Брылёв В.Д.<br>"
                                                + "студент группы ЗБ-ПИ21-1с<br><br>"
                                                + "2024 год"));
        alertbuilder.setCancelable(true);
        alertbuilder.setPositiveButton("ОК", null);
        AlertDialog dialog  = alertbuilder.create();
        dialog.show();
    }

}