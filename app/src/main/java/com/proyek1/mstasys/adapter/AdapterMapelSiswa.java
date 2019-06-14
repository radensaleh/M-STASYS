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

import com.proyek1.mstasys.R;
import com.proyek1.mstasys.response.MapelSiswa;
import com.proyek1.mstasys.siswa.NilaiSiswaActivity;

import java.util.List;

public class AdapterMapelSiswa extends RecyclerView.Adapter<AdapterMapelSiswa.ViewHolder> {

    private List<MapelSiswa> mapelSiswaList;
    private Context mContext;
    private String nis;

    public AdapterMapelSiswa(String nis, List<MapelSiswa> mapelSiswaList, Context mContext) {
        this.nis = nis;
        this.mapelSiswaList = mapelSiswaList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_mapelsiswa, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        if(mapelSiswaList.get(i).getNama_mapel().equals("Fisika")){
            viewHolder.tvMapel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mapel_fisika, 0, 0, 0);
        }else if(mapelSiswaList.get(i).getNama_mapel().equals("Kimia")){
            viewHolder.tvMapel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mapel_kimia, 0, 0, 0);
        }else if(mapelSiswaList.get(i).getNama_mapel().equals("Biologi")){
            viewHolder.tvMapel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mapel_biologi, 0, 0, 0);
        }else if(mapelSiswaList.get(i).getNama_mapel().equals("Matematika")){
            viewHolder.tvMapel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mapel_matematika, 0, 0, 0);
        }else if(mapelSiswaList.get(i).getNama_mapel().equals("Ekonomi")){
            viewHolder.tvMapel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mapel_ekonomi, 0, 0, 0);
        }else if(mapelSiswaList.get(i).getNama_mapel().equals("Sejarah")){
            viewHolder.tvMapel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mapel_sejarah, 0, 0, 0);
        }else if(mapelSiswaList.get(i).getNama_mapel().equals("Sosiologi")){
            viewHolder.tvMapel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mapel_sosiologi, 0, 0, 0);
        }else if(mapelSiswaList.get(i).getNama_mapel().equals("Geografi")){
            viewHolder.tvMapel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mapel_geografi, 0, 0, 0);
        }
        viewHolder.tvMapel.setText(" "+mapelSiswaList.get(i).getNama_mapel());
        viewHolder.tvJenisMapel.setText("Kategori : " + mapelSiswaList.get(i).getKategori_mapel());
        viewHolder.tvNamaGuru.setText(mapelSiswaList.get(i).getNama());
        viewHolder.tvNIP.setText("NIP. " + mapelSiswaList.get(i).getNip());
        viewHolder.cvMapel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, mapelSiswaList.get(i).getId_ampu(), Toast.LENGTH_SHORT).show();
                String id_ampu = mapelSiswaList.get(i).getId_ampu();
                String nip     = mapelSiswaList.get(i).getNip();
                String nama    = mapelSiswaList.get(i).getNama();
                String mapel   = mapelSiswaList.get(i).getNama_mapel();

                Intent i = new Intent(mContext, NilaiSiswaActivity.class);
                i.putExtra("id_ampu", id_ampu);
                i.putExtra("nip", nip);
                i.putExtra("nama", nama);
                i.putExtra("mapel", mapel);
                i.putExtra("nis", nis);
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mapelSiswaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMapel, tvJenisMapel, tvNamaGuru, tvNIP;
        CardView cvMapel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMapel      = itemView.findViewById(R.id.tv_mapel);
            tvJenisMapel = itemView.findViewById(R.id.tv_jenisMapel);
            tvNamaGuru   = itemView.findViewById(R.id.tv_nama);
            tvNIP        = itemView.findViewById(R.id.tv_nip);
            cvMapel      = itemView.findViewById(R.id.cvMapelSiswa);

        }
    }
}
