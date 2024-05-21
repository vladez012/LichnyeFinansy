package com.example.lichnyefinansy;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapterRecycleView extends RecyclerView.Adapter<CustomAdapterRecycleView.RowViewHolder> {

    private final List<DannyeDvizheniyModel> dannye_list;

    public CustomAdapterRecycleView(List<DannyeDvizheniyModel> dannye_list){
        this.dannye_list = dannye_list;
    }

    @NonNull
    @Override
    public RowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_list_item, parent, false);
        return  new RowViewHolder(itemView);
    }

    private void setHeaderBg(View view){
        view.setBackgroundResource(R.drawable.table_header_cell_bg);
    }

    private void setContentBg(View view){
        view.setBackgroundResource(R.drawable.table_content_cell_bg);
    }

    @Override
    public void onBindViewHolder(@NonNull RowViewHolder holder, int position){
        int rowPos = holder.getAdapterPosition();

        if(rowPos == 0){

            Spannable span1 = new SpannableString("Дата");
            span1.setSpan(new ForegroundColorSpan(Color.rgb(255,255,255)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            Spannable span2 = new SpannableString("Вид движения");
            span2.setSpan(new ForegroundColorSpan(Color.rgb(255,255,255)), 0, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            Spannable span3 = new SpannableString("Категория");
            span3.setSpan(new ForegroundColorSpan(Color.rgb(255,255,255)), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            Spannable span4 = new SpannableString("Сумма");
            span4.setSpan(new ForegroundColorSpan(Color.rgb(255,255,255)), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.txtDataDvizheniya.setText(span1, TextView.BufferType.SPANNABLE);
            holder.txtVidDvizheniya.setText(span2, TextView.BufferType.SPANNABLE);
            holder.txtKategoriya.setText(span3, TextView.BufferType.SPANNABLE);
            holder.txtSumma.setText(span4, TextView.BufferType.SPANNABLE);

            setHeaderBg(holder.txtDataDvizheniya);
            setHeaderBg(holder.txtVidDvizheniya);
            setHeaderBg(holder.txtKategoriya);
            setHeaderBg(holder.txtSumma);

        } else{
            DannyeDvizheniyModel model = dannye_list.get(rowPos-1);

            holder.txtDataDvizheniya.setText(String.valueOf(model.dataDvizheniya));
            holder.txtVidDvizheniya.setText(String.valueOf(model.vidDvizheniya));
            holder.txtKategoriya.setText(String.valueOf(model.kategoriya));
            holder.txtSumma.setText(String.valueOf(model.summa));

            setContentBg(holder.txtDataDvizheniya);
            setContentBg(holder.txtVidDvizheniya);
            setContentBg(holder.txtKategoriya);
            setContentBg(holder.txtSumma);
        }
    }

    @Override
    public int getItemCount() {
        return dannye_list.size() + 1;
    }

    static class RowViewHolder extends RecyclerView.ViewHolder{
        TextView txtDataDvizheniya;
        TextView txtVidDvizheniya;
        TextView txtKategoriya;
        TextView txtSumma;

        RowViewHolder(@NonNull View itemView){
            super(itemView);
            txtDataDvizheniya = itemView.findViewById(R.id.txtDataDvizheniya);
            txtVidDvizheniya = itemView.findViewById(R.id.txtVidDvizheniya);
            txtKategoriya = itemView.findViewById(R.id.txtKategoriya);
            txtSumma = itemView.findViewById(R.id.txtSumma);
        }

    }

}
