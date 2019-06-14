package com.proyek1.mstasys.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.proyek1.mstasys.R;
import com.proyek1.mstasys.response.Soal;
import com.proyek1.mstasys.siswa.HasilSoalActivity;
import com.proyek1.mstasys.siswa.KerjakanSoalActivity;

import java.util.List;

public class AdapterSoalSiswa extends RecyclerView.Adapter<AdapterSoalSiswa.ViewHolder> {

    private List<Soal> soalList;
    private Context mContext;
    private String nis;

    public AdapterSoalSiswa(List<Soal> soalList, Context mContext, String nis) {
        this.soalList = soalList;
        this.mContext = mContext;
        this.nis = nis;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_soalsiswa, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvDesc.setText(soalList.get(i).getDeskripsi());
        viewHolder.tvMapel.setText("Pelajaran : " + soalList.get(i).getNama_mapel());
        viewHolder.tvKategori.setText("Kategori : " + soalList.get(i).getKategori_mapel());
        viewHolder.tvDate.setText("Diunggah pada : " + soalList.get(i).getDate_create());
        viewHolder.tvWaktu.setText("Waktu Pengerjaan : " + soalList.get(i).getWaktu_pengerjaan() + " menit");

        final String desc = soalList.get(i).getDeskripsi();
        final String date = soalList.get(i).getDate_create();
        final String mapel= soalList.get(i).getNama_mapel();
        final String kate = soalList.get(i).getKategori_mapel();
        final String waktu= soalList.get(i).getWaktu_pengerjaan();
        final String status = soalList.get(i).getStatus();

        if(status.equals("0")){
            viewHolder.btnKerjakan.setEnabled(false);
            viewHolder.btnHasil.setEnabled(false);
//            new AlertDialog.Builder(mContext)
//                    .setIcon(R.drawable.warning)
//                    .setTitle("Peringatan")
//                    .setMessage("Soal belum bisa diakses, untuk mengaktifkan soal harap menghubungi guru yang bersangkutan")
//                    .setCancelable(false)
//                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    }).show();
        }else{
            viewHolder.btnKerjakan.setEnabled(true);
            viewHolder.btnHasil.setEnabled(true);
        }

        viewHolder.btnKerjakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext)
                        .setIcon(R.drawable.warning)
                        .setTitle("Peringatan")
                        .setMessage("Anda yakin ingin mengerjakan " + desc + " Mata Pelajaran " + mapel + " dalam waktu " + waktu + " menit ?")
                        .setCancelable(false)
                        .setNegativeButton("Kerjakan", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(mContext, KerjakanSoalActivity.class);
                                    i.putExtra("deskripsi", desc);
                                    i.putExtra("date", date);
                                    i.putExtra("mapel", mapel);
                                    i.putExtra("jenis_mapel", kate);
                                    i.putExtra("waktu", waktu);
//                                    i.putExtra("nis", nis);
                                    mContext.startActivity(i);
                            }
                        }).setPositiveButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });

        viewHolder.btnHasil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, HasilSoalActivity.class);
                i.putExtra("status", 0);
                i.putExtra("date", date);
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return soalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDesc, tvMapel, tvKategori, tvDate, tvWaktu;
        Button btnKerjakan, btnHasil;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDesc    = itemView.findViewById(R.id.tv_deskripsi);
            tvMapel   = itemView.findViewById(R.id.tv_mapel);
            tvKategori= itemView.findViewById(R.id.tv_jenisMapel);
            tvDate    = itemView.findViewById(R.id.tv_create);
            tvWaktu   = itemView.findViewById(R.id.tv_waktu);
            btnKerjakan= itemView.findViewById(R.id.btnKerjakanSoal);
            btnHasil  = itemView.findViewById(R.id.btnHasil);
        }
    }
}
