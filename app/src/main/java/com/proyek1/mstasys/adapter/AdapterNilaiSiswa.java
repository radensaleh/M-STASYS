package com.proyek1.mstasys.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.proyek1.mstasys.R;
import com.proyek1.mstasys.guru.UbahNilaiActivity;
import com.proyek1.mstasys.response.NilaiSiswa;

import java.util.List;

public class AdapterNilaiSiswa extends RecyclerView.Adapter<AdapterNilaiSiswa.ViewHolder> {

    private List<NilaiSiswa> nilaiSiswaList;
    private Context mContext;
    private int status;

    private String nip, nis, nama, nama_mapel, nama_kelas, semester, id_mapel, id_kelas, id_semester;

    public AdapterNilaiSiswa(List<NilaiSiswa> nilaiSiswaList, Context mContext, int status) {
        this.nilaiSiswaList = nilaiSiswaList;
        this.mContext = mContext;
        this.status = status;
    }

    public void setData(String nip, String nis, String nama, String mapel, String kelas, String semester, String id_mapel, String id_kelas, String id_semester){
        this.nip    = nip;
        this.nis    = nis;
        this.nama   = nama;
        this.nama_mapel = mapel;
        this.nama_kelas = kelas;
        this.semester   = semester;
        this.id_mapel   = id_mapel;
        this.id_kelas   = id_kelas;
        this.id_semester= id_semester;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_nilaisiswa, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.tvDetail.setText(" "+nilaiSiswaList.get(i).getJenis_nilai());
        viewHolder.tvNilai.setText("Nilai : " + nilaiSiswaList.get(i).getNilai());
        if(Integer.parseInt(nilaiSiswaList.get(i).getNilai()) <= 100 && Integer.parseInt(nilaiSiswaList.get(i).getNilai()) >= 92){
            viewHolder.tvIndex.setText("Kriteria : A");
        }else if(Integer.parseInt(nilaiSiswaList.get(i).getNilai()) <= 91 && Integer.parseInt(nilaiSiswaList.get(i).getNilai()) >= 83){
            viewHolder.tvIndex.setText("Kriteria : B");
        }else if(Integer.parseInt(nilaiSiswaList.get(i).getNilai()) <= 82 && Integer.parseInt(nilaiSiswaList.get(i).getNilai()) >= 75){
            viewHolder.tvIndex.setText("Kriteria : C");
        }else{
            viewHolder.tvIndex.setText("Kriteria : D");
        }
        viewHolder.tvCreate.setText("Dibuat            : " + nilaiSiswaList.get(i).getDate_create());
        viewHolder.tvUpdate.setText("Diperbaharui : " + nilaiSiswaList.get(i).getDate_update());

        final String nilai     = nilaiSiswaList.get(i).getNilai();
        final String id_detail = nilaiSiswaList.get(i).getId_detail();
        final String id_nilai  = nilaiSiswaList.get(i).getId_nilai();

        if(status == 1){
            viewHolder.cvNilai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, UbahNilaiActivity.class);
                    i.putExtra("nis", nis);
                    i.putExtra("nama", nama);
                    i.putExtra("nip", nip);
                    i.putExtra("id_mapel", id_mapel);
                    i.putExtra("nama_mapel", nama_mapel);
                    i.putExtra("id_kelas", id_kelas);
                    i.putExtra("nama_kelas", nama_kelas);
                    i.putExtra("id_semester", id_semester);
                    i.putExtra("semester", semester);
                    i.putExtra("nilai", nilai);
                    i.putExtra("id_detail", id_detail);
                    i.putExtra("id_nilai", id_nilai);
                    mContext.startActivity(i);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return nilaiSiswaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDetail, tvNilai, tvIndex, tvCreate, tvUpdate;
        CardView cvNilai;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cvNilai  = itemView.findViewById(R.id.cvNilaiSiswa);
            tvDetail = itemView.findViewById(R.id.tv_detailnilai);
            tvNilai  = itemView.findViewById(R.id.tv_nilai);
            tvIndex  = itemView.findViewById(R.id.tv_index);
            tvCreate = itemView.findViewById(R.id.tv_create);
            tvUpdate = itemView.findViewById(R.id.tv_update);
        }
    }
}
