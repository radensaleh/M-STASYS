package com.proyek1.mstasys.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.proyek1.mstasys.R;
import com.proyek1.mstasys.response.DetailSoal;

import java.util.List;

public class AdapterDetailSoal extends RecyclerView.Adapter<AdapterDetailSoal.ViewHolder> {

    private List<DetailSoal> detailSoalList;
    private Context mContext;

    public AdapterDetailSoal(List<DetailSoal> detailSoalList, Context mContext) {
        this.detailSoalList = detailSoalList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_lihatsoal, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvNo.setText("No. " + detailSoalList.get(i).getNomer());
        viewHolder.tvSoal.setText(detailSoalList.get(i).getSoal());
        viewHolder.tvA.setText("a. " + detailSoalList.get(i).getA());
        viewHolder.tvB.setText("b. " + detailSoalList.get(i).getB());
        viewHolder.tvC.setText("c. " + detailSoalList.get(i).getC());
        viewHolder.tvD.setText("d. " + detailSoalList.get(i).getD());
        viewHolder.tvJawaban.setText("Jawabannya adalah (" + detailSoalList.get(i).getJawaban() + ")");
    }

    @Override
    public int getItemCount() {
        return detailSoalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNo, tvSoal, tvA, tvB, tvC, tvD, tvJawaban;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNo    = itemView.findViewById(R.id.tv_no);
            tvSoal  = itemView.findViewById(R.id.tv_soal);
            tvA     = itemView.findViewById(R.id.tv_a);
            tvB     = itemView.findViewById(R.id.tv_b);
            tvC     = itemView.findViewById(R.id.tv_c);
            tvD     = itemView.findViewById(R.id.tv_d);
            tvJawaban= itemView.findViewById(R.id.tv_jawaban);

        }
    }
}
