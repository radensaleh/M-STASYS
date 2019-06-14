package com.proyek1.mstasys.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.proyek1.mstasys.BuildConfig;
import com.proyek1.mstasys.R;
import com.proyek1.mstasys.entity.Siswa;
import com.proyek1.mstasys.guru.LihatNilaiActivity;
import com.proyek1.mstasys.guru.TambahNilaiActivity;

import java.util.List;

public class AdapterSiswa extends RecyclerView.Adapter<AdapterSiswa.ViewHolder> {

    private List<Siswa> siswaList;
    private Context mContext;
    private String nip, id_mapel, nama_mapel, id_kelas, nama_kelas, id_semester, semester;

    public AdapterSiswa(List<Siswa> siswaList, Context mContext, String nip, String id_mapel, String nama_mapel, String id_kelas, String nama_kelas, String id_semester, String semester) {
        this.siswaList = siswaList;
        this.mContext = mContext;
        this.nip = nip;
        this.id_mapel = id_mapel;
        this.nama_mapel = nama_mapel;
        this.id_kelas = id_kelas;
        this.nama_kelas = nama_kelas;
        this.id_semester = id_semester;
        this.semester = semester;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_siswa, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.tvNama.setText(siswaList.get(i).getNama());
        viewHolder.tvNISN.setText("NISN. "+siswaList.get(i).getNisn());
        viewHolder.tvNIS.setText("NIS. "+siswaList.get(i).getNis());

        Glide.with(mContext)
                .load(BuildConfig.IMG_SISWA+siswaList.get(i).getFoto())
                .into(viewHolder.imgFoto);

        final String nis  = siswaList.get(i).getNis();
        final String nama = siswaList.get(i).getNama();

        viewHolder.btnLihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, LihatNilaiActivity.class);
                i.putExtra("nis", nis);
                i.putExtra("nama", nama);
                i.putExtra("nip", nip);
                i.putExtra("id_mapel", id_mapel);
                i.putExtra("nama_mapel", nama_mapel);
                i.putExtra("id_kelas", id_kelas);
                i.putExtra("nama_kelas", nama_kelas);
                i.putExtra("id_semester", id_semester);
                i.putExtra("semester", semester);
                mContext.startActivity(i);
            }
        });

        viewHolder.btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, TambahNilaiActivity.class);
                i.putExtra("nis", nis);
                i.putExtra("nama", nama);
                i.putExtra("nip", nip);
                i.putExtra("id_mapel", id_mapel);
                i.putExtra("nama_mapel", nama_mapel);
                i.putExtra("id_kelas", id_kelas);
                i.putExtra("nama_kelas", nama_kelas);
                i.putExtra("id_semester", id_semester);
                i.putExtra("semester", semester);
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return siswaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNama, tvNISN, tvNIS;
        Button btnTambah, btnLihat;
        ImageView imgFoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNama = itemView.findViewById(R.id.tvNama);
            tvNISN = itemView.findViewById(R.id.tvNISN);
            tvNIS  = itemView.findViewById(R.id.tvNIS);

            btnTambah = itemView.findViewById(R.id.btnTambahNilai);
            btnLihat  = itemView.findViewById(R.id.btnLihatNilai);

            imgFoto      = itemView.findViewById(R.id.img_photo);

        }
    }
}
