package com.proyek1.mstasys.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.proyek1.mstasys.R;
import com.proyek1.mstasys.response.AmpuMapel;

import java.util.List;

public class AdapterAmpuMapel extends RecyclerView.Adapter<AdapterAmpuMapel.ViewHolder> {

    private Context mContext;
    private List<AmpuMapel> ampuMapels;

    public AdapterAmpuMapel(Context mContext, List<AmpuMapel> ampuMapels) {
        this.mContext = mContext;
        this.ampuMapels = ampuMapels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_ampumapel, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if(ampuMapels.get(i).getNama_mapel().equals("Fisika")){
            viewHolder.nama_mapel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mapel_fisika, 0, 0, 0);
        }else if(ampuMapels.get(i).getNama_mapel().equals("Kimia")){
            viewHolder.nama_mapel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mapel_kimia, 0, 0, 0);
        }else if(ampuMapels.get(i).getNama_mapel().equals("Biologi")){
            viewHolder.nama_mapel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mapel_biologi, 0, 0, 0);
        }else if(ampuMapels.get(i).getNama_mapel().equals("Matematika")){
            viewHolder.nama_mapel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mapel_matematika, 0, 0, 0);
        }else if(ampuMapels.get(i).getNama_mapel().equals("Ekonomi")){
            viewHolder.nama_mapel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mapel_ekonomi, 0, 0, 0);
        }else if(ampuMapels.get(i).getNama_mapel().equals("Sejarah")){
            viewHolder.nama_mapel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mapel_sejarah, 0, 0, 0);
        }else if(ampuMapels.get(i).getNama_mapel().equals("Sosiologi")){
            viewHolder.nama_mapel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mapel_sosiologi, 0, 0, 0);
        }else if(ampuMapels.get(i).getNama_mapel().equals("Geografi")){
            viewHolder.nama_mapel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mapel_geografi, 0, 0, 0);
        }

        viewHolder.nama_mapel.setText(" " + ampuMapels.get(i).getNama_mapel() + " [" + ampuMapels.get(i).getId_mapel() + "]");
        viewHolder.kategori_mapel.setText("Kategori : " + ampuMapels.get(i).getKategori_mapel() + " | ID Ampu : " + ampuMapels.get(i).getId_ampu());
        viewHolder.kelas.setText("Kelas " + ampuMapels.get(i).getTingkat() + " " + ampuMapels.get(i).getJurusan() + " " + ampuMapels.get(i).getRombel());
        viewHolder.semester.setText("Semester " + ampuMapels.get(i).getSemester() + " Tahun Ajaran " + ampuMapels.get(i).getThn_ajaran());

    }

    @Override
    public int getItemCount() {
        return ampuMapels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama_mapel, kategori_mapel, kelas, semester;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama_mapel = itemView.findViewById(R.id.tv_mapel);
            kategori_mapel = itemView.findViewById(R.id.tv_jenisMapel);
            kelas    = itemView.findViewById(R.id.tv_kelas);
            semester = itemView.findViewById(R.id.tv_semester);
        }
    }
}
