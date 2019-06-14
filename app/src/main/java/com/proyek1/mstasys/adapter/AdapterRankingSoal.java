package com.proyek1.mstasys.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.proyek1.mstasys.R;
import com.proyek1.mstasys.response.HasilSoal;

import java.util.List;

public class AdapterRankingSoal extends RecyclerView.Adapter<AdapterRankingSoal.ViewHolder> {

    private Context mContext;
    private List<HasilSoal> hasilSoals;

    public AdapterRankingSoal(Context mContext, List<HasilSoal> hasilSoals) {
        this.mContext = mContext;
        this.hasilSoals = hasilSoals;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_rankingsoal, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if(i == 0){
            Glide.with(mContext)
                    .load(R.drawable.trophy1st)
                    .into(viewHolder.img);
            viewHolder.nama.setText(hasilSoals.get(i).getNama());
            viewHolder.jawaban.setText("Benar (" + hasilSoals.get(i).getBenar() + ") || Salah ("+ hasilSoals.get(i).getSalah() +") || Nilai ("+ hasilSoals.get(i).getNilai() +")");
            viewHolder.selesai.setText("Selesai pada : " + hasilSoals.get(i).getCreated_at());
        }else if(i == 1){
            Glide.with(mContext)
                    .load(R.drawable.trophy2nd)
                    .into(viewHolder.img);
            viewHolder.nama.setText(hasilSoals.get(i).getNama());
            viewHolder.jawaban.setText("Benar (" + hasilSoals.get(i).getBenar() + ") || Salah ("+ hasilSoals.get(i).getSalah() +") || Nilai ("+ hasilSoals.get(i).getNilai() +")");
            viewHolder.selesai.setText("Selesai pada : " + hasilSoals.get(i).getCreated_at());
        }else if(i == 2){
            Glide.with(mContext)
                    .load(R.drawable.trophy3rd)
                    .into(viewHolder.img);
            viewHolder.nama.setText(hasilSoals.get(i).getNama());
            viewHolder.jawaban.setText("Benar (" + hasilSoals.get(i).getBenar() + ") || Salah ("+ hasilSoals.get(i).getSalah() +") || Nilai ("+ hasilSoals.get(i).getNilai() +")");
            viewHolder.selesai.setText("Selesai pada : " + hasilSoals.get(i).getCreated_at());
        }else if(i >= 3){
            Glide.with(mContext)
                    .load(R.drawable.trophy)
                    .into(viewHolder.img);
            viewHolder.img.getLayoutParams().width = 96;
            viewHolder.img.getLayoutParams().height = 100;
            viewHolder.img.requestLayout();

            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(96,100); //Parent Params
            params.setMargins(5,10,0,0);
            viewHolder.img.setLayoutParams(params);

            viewHolder.nama.setText(hasilSoals.get(i).getNama());
            viewHolder.jawaban.setText("Benar (" + hasilSoals.get(i).getBenar() + ") || Salah ("+ hasilSoals.get(i).getSalah() +") || Nilai ("+ hasilSoals.get(i).getNilai() +")");
            viewHolder.selesai.setText("Selesai pada : " + hasilSoals.get(i).getCreated_at());
        }
    }

    @Override
    public int getItemCount() {
        return hasilSoals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView nama, jawaban, selesai;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img     = itemView.findViewById(R.id.imgPhoto);
            nama    = itemView.findViewById(R.id.tvNama);
            jawaban = itemView.findViewById(R.id.tvJawaban);
            selesai = itemView.findViewById(R.id.tvSelesai);

        }
    }
}
