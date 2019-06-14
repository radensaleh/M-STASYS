package com.proyek1.mstasys.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.proyek1.mstasys.R;
import com.proyek1.mstasys.guru.LihatSoalActivity;
import com.proyek1.mstasys.response.Soal;
import com.proyek1.mstasys.siswa.HasilSoalActivity;

import java.sql.Time;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AdapterSoal extends RecyclerView.Adapter<AdapterSoal.ViewHolder> {

    private List<Soal> soalList;
    private Context mContext;
    private String nip;

    public AdapterSoal(List<Soal> soalList, Context mContext, String nip) {
        this.soalList = soalList;
        this.mContext = mContext;
        this.nip = nip;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_soal, viewGroup, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.tvDesc.setText(soalList.get(i).getDeskripsi());
        viewHolder.tvMapel.setText("Pelajaran : " + soalList.get(i).getNama_mapel());
        viewHolder.tvKategori.setText("Kategori : " + soalList.get(i).getKategori_mapel());
        viewHolder.tvDate.setText("Diunggah pada : " + soalList.get(i).getDate_create());
        viewHolder.tvWaktu.setText("Waktu Pengerjaan : " + soalList.get(i).getWaktu_pengerjaan() + " menit");

        viewHolder.tvKelas.setText("Kelas " + soalList.get(i).getTingkat() + " " + soalList.get(i).getJurusan() + " " + soalList.get(i).getRombel());
        viewHolder.tvSemester.setText("Semester " + soalList.get(i).getSemester() + " Tahun Ajaran " + soalList.get(i).getThn_ajaran());

        final String date_create = soalList.get(i).getDate_create();
        viewHolder.btnLihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, LihatSoalActivity.class);
                i.putExtra("nip", nip);
                i.putExtra("date_create", date_create);
                mContext.startActivity(i); }
        });

        viewHolder.btnHasil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, HasilSoalActivity.class);
                i.putExtra("status", 1);
                i.putExtra("date", date_create);
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return soalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDesc, tvMapel, tvKategori, tvDate, tvWaktu, tvKelas, tvSemester;
        Button btnLihat, btnHasil;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDesc    = itemView.findViewById(R.id.tv_deskripsi);
            tvMapel   = itemView.findViewById(R.id.tv_mapel);
            tvKategori= itemView.findViewById(R.id.tv_jenisMapel);
            tvDate    = itemView.findViewById(R.id.tv_create);
            tvWaktu   = itemView.findViewById(R.id.tv_waktu);
            tvKelas   = itemView.findViewById(R.id.tv_kelas);
            tvSemester= itemView.findViewById(R.id.tv_semester);
            btnLihat  = itemView.findViewById(R.id.btnLihatSoal);
            btnHasil  = itemView.findViewById(R.id.btnHasil);

        }
    }
}
