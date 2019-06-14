package com.proyek1.mstasys.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.proyek1.mstasys.R;
import com.proyek1.mstasys.entity.Siswa;
import com.proyek1.mstasys.response.DetailSoal;

import java.util.ArrayList;
import java.util.List;

public class AdapterKerjakanSoal extends RecyclerView.Adapter<AdapterKerjakanSoal.ViewHolder> {

    private Context mContext;
    private List<DetailSoal> detailSoalList;

    private List<String> jawabanNo = new ArrayList<>();
    private List<String> jawaban  = new ArrayList<>();
    private Siswa siswa;
    private String date;

    public AdapterKerjakanSoal(Context mContext, List<DetailSoal> detailSoalList, Siswa siswa, String date) {
        this.mContext = mContext;
        this.detailSoalList = detailSoalList;
        this.siswa = siswa;
        this.date = date;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_kerjakansoal, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.tvNo.setText("No. " + detailSoalList.get(i).getNomer());
        viewHolder.tvSoal.setText(detailSoalList.get(i).getSoal());
        viewHolder.rbA.setText(detailSoalList.get(i).getA());
        viewHolder.rbB.setText(detailSoalList.get(i).getB());
        viewHolder.rbC.setText(detailSoalList.get(i).getC());
        viewHolder.rbD.setText(detailSoalList.get(i).getD());

        final int totalSoal = detailSoalList.size();

        viewHolder.rgJawaban.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(RadioGroup group, int checkedId) {
               switch (checkedId){
                   case (R.id.rb_a):
                       int a = 0;
                       if(jawabanNo.size() == 0){
                           jawabanNo.add(detailSoalList.get(i).getNomer());
//                           jawaban.add(viewHolder.rbA.getText().toString());
                           jawaban.add("a");
                           siswa.setjawaban(jawabanNo, jawaban, mContext, date, totalSoal);
                       }else{
                           for(int y = 0; y < jawabanNo.size(); y++){
                               if(!jawabanNo.get(y).equals(detailSoalList.get(i).getNomer())){
                                   a++;
                               }else{
                                   a = 0;
                                   jawabanNo.remove(jawabanNo.get(y));
                                   jawaban.remove(jawaban.get(y));
                               }
                           }

                           if(a > 0){
                               jawabanNo.add(detailSoalList.get(i).getNomer());
//                               jawaban.add(viewHolder.rbA.getText().toString());
                               jawaban.add("a");
                               siswa.setjawaban(jawabanNo, jawaban, mContext, date, totalSoal);


                           }else{
                               jawabanNo.add(detailSoalList.get(i).getNomer());
//                               jawaban.add(viewHolder.rbA.getText().toString());
                               jawaban.add("a");
                               siswa.setjawaban(jawabanNo, jawaban, mContext, date, totalSoal);
                           }
                       }
                       break;
                   case (R.id.rb_b):
                       int b = 0;
                       if(jawabanNo.size() == 0){
                           jawabanNo.add(detailSoalList.get(i).getNomer());
//                           jawaban.add(viewHolder.rbB.getText().toString());
                           jawaban.add("b");
                           siswa.setjawaban(jawabanNo, jawaban, mContext, date, totalSoal);
                       }else{
                           for(int y = 0; y < jawabanNo.size(); y++){
                               if(!jawabanNo.get(y).equals(detailSoalList.get(i).getNomer())){
                                   b++;
                               }else{
                                   b = 0;
                                   jawabanNo.remove(jawabanNo.get(y));
                                   jawaban.remove(jawaban.get(y));
                               }
                           }

                           if(b > 0){
                               jawabanNo.add(detailSoalList.get(i).getNomer());
//                               jawaban.add(viewHolder.rbB.getText().toString());
                               jawaban.add("b");
                               siswa.setjawaban(jawabanNo, jawaban, mContext, date, totalSoal);
                           }else{
                               jawabanNo.add(detailSoalList.get(i).getNomer());
//                               jawaban.add(viewHolder.rbB.getText().toString());
                               jawaban.add("b");
                               siswa.setjawaban(jawabanNo, jawaban, mContext, date, totalSoal);
                           }
                       }
                       break;
                   case (R.id.rb_c):
                       int c = 0;
                       if(jawabanNo.size() == 0){
                           jawabanNo.add(detailSoalList.get(i).getNomer());
//                           jawaban.add(viewHolder.rbC.getText().toString());
                           jawaban.add("c");
                           siswa.setjawaban(jawabanNo, jawaban, mContext, date, totalSoal);
                       }else{
                           for(int y = 0; y < jawabanNo.size(); y++){
                               if(!jawabanNo.get(y).equals(detailSoalList.get(i).getNomer())){
                                   c++;
                               }else{
                                   c = 0;
                                   jawabanNo.remove(jawabanNo.get(y));
                                   jawaban.remove(jawaban.get(y));
                               }
                           }

                           if(c > 0){
                               jawabanNo.add(detailSoalList.get(i).getNomer());
//                               jawaban.add(viewHolder.rbC.getText().toString());
                               jawaban.add("c");
                               siswa.setjawaban(jawabanNo, jawaban, mContext, date, totalSoal);
                           }else{
                               jawabanNo.add(detailSoalList.get(i).getNomer());
//                               jawaban.add(viewHolder.rbC.getText().toString());
                               jawaban.add("c");
                               siswa.setjawaban(jawabanNo, jawaban, mContext, date, totalSoal);
                           }

                       }
                       break;
                   case (R.id.rb_d):
                       int d = 0;
                       if(jawabanNo.size() == 0){
                           jawabanNo.add(detailSoalList.get(i).getNomer());
//                           jawaban.add(viewHolder.rbD.getText().toString());
                           jawaban.add("d");
                           siswa.setjawaban(jawabanNo, jawaban, mContext, date, totalSoal);
                       }else{
                           for(int y = 0; y < jawabanNo.size(); y++){
                               if(!jawabanNo.get(y).equals(detailSoalList.get(i).getNomer())){
                                   d++;
                               }else{
                                   d = 0;
                                   jawabanNo.remove(jawabanNo.get(y));
                                   jawaban.remove(jawaban.get(y));
                               }
                           }

                           if(d > 0){
                               jawabanNo.add(detailSoalList.get(i).getNomer());
//                               jawaban.add(viewHolder.rbD.getText().toString());
                               jawaban.add("d");
                               siswa.setjawaban(jawabanNo, jawaban, mContext, date, totalSoal);
                           }else{
                               jawabanNo.add(detailSoalList.get(i).getNomer());
//                               jawaban.add(viewHolder.rbD.getText().toString());
                               jawaban.add("d");
                               siswa.setjawaban(jawabanNo, jawaban, mContext, date, totalSoal);
                           }
                       }
                       break;
               }
           }
       });

    }

    @Override
    public int getItemCount() {
        return detailSoalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNo, tvSoal;
        RadioButton rbA, rbB, rbC, rbD;
        RadioGroup rgJawaban;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNo     = itemView.findViewById(R.id.tv_no);
            tvSoal   = itemView.findViewById(R.id.tv_soal);
            rgJawaban= itemView.findViewById(R.id.rg_jawaban);

            rbA = itemView.findViewById(R.id.rb_a);
            rbB = itemView.findViewById(R.id.rb_b);
            rbC = itemView.findViewById(R.id.rb_c);
            rbD = itemView.findViewById(R.id.rb_d);

        }
    }
}
