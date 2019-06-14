package com.proyek1.mstasys.entity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.proyek1.mstasys.R;
import com.proyek1.mstasys.adapter.AdapterNilaiSiswa;
import com.proyek1.mstasys.api.RetrofitClient;
import com.proyek1.mstasys.response.NilaiSiswa;
import com.proyek1.mstasys.response.Response;
import com.proyek1.mstasys.siswa.MainSiswaActivity;
import com.proyek1.mstasys.sqlLite.SiswaDAO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Siswa extends Person {
    private Siswa(){}
    private String nis;
    private String nisn;
    private String no_ijasah_smp;
    private String no_un;
    private String id_kelas;
    private String tingkat;
    private String jurusan;
    private String rombel;

    private static Siswa siswa;
    public static Siswa getInstance(){
        if(siswa==null){
            siswa=new Siswa();
        }
        return siswa;
    }

    public String getTingkat() {
        return tingkat;
    }

    public void setTingkat(String tingkat) {
        this.tingkat = tingkat;
    }

    public String getJurusan() {
        return jurusan;
    }

    public void setJurusan(String jurusan) {
        this.jurusan = jurusan;
    }

    public String getRombel() {
        return rombel;
    }

    public void setRombel(String rombel) {
        this.rombel = rombel;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getNisn() {
        return nisn;
    }

    public void setNisn(String nisn) {
        this.nisn = nisn;
    }

    public String getNo_ijasah_smp() {
        return no_ijasah_smp;
    }

    public void setNo_ijasah_smp(String no_ijasah_smp) {
        this.no_ijasah_smp = no_ijasah_smp;
    }

    public String getNo_un() {
        return no_un;
    }

    public void setNo_un(String no_un) {
        this.no_un = no_un;
    }

    public String getId_kelas() {
        return id_kelas;
    }

    public void setId_kelas(String id_kelas) {
        this.id_kelas = id_kelas;
    }

    public void loginSiswa(final String nis, final String password, final Context mContext){
        //Toast.makeText(mContext, nis + " " + password, Toast.LENGTH_SHORT).show();
        Runnable rn = new Runnable() {
            @Override
            public void run() {
                Call<Response> res = RetrofitClient
                        .getInstance()
                        .baseAPI()
                        .loginSiswa(nis, password);

                res.enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                        String error   = response.body().getError();
                        String[] message = response.body().getMessage();
                        String pesan = "";
                        for(int i = 0; i < message.length; i++){
                            pesan = pesan + message[i] + "\n";
                        }

                        if(error.equals("0")){
                            Siswa siswa = Siswa.getInstance();
                            siswa.setNis(nis);
                            siswa.setPassword(password);

                            SiswaDAO siswaDAO = new SiswaDAO(mContext);
                            if(siswaDAO.getUser()==null){
                                siswaDAO.setUser(siswa.getNis(), siswa.getPassword(), "1");

                                Intent i = new Intent(mContext, MainSiswaActivity.class);
                                mContext.startActivity(i);
                            }else{
                                Intent i = new Intent(mContext, MainSiswaActivity.class);
                                mContext.startActivity(i);
                            }

                        }else{
                           Toast.makeText(mContext, pesan, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {
                        //Log.e("ERRRRRRRRRR : ", t.getMessage());
                        Toast.makeText(mContext, "Gagal, harap periksa kembali koneksi anda", Toast.LENGTH_LONG).show();
                    }
                });
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(rn, 2000);

    }

    public void loginSiswa(final String nis, String password, final ProgressDialog pd, final Context mContext){
        this.nis = nis;
        this.setPassword(password);

        Call<Response> res = RetrofitClient
                .getInstance()
                .baseAPI()
                .loginSiswa(nis, password);

        res.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                String error   = response.body().getError();
                String[] message = response.body().getMessage();
                String pesan = "";
                for(int i = 0; i < message.length; i++){
                    pesan = pesan + message[i] + "\n";
                }

                Siswa siswa = Siswa.getInstance();

                if(error.equals("0")){
                    SiswaDAO siswaDAO = new SiswaDAO(mContext);
                    if(siswaDAO.getUser()==null){
                        pd.dismiss();
                        //Toast.makeText(mContext, siswa.getNis() + " " + siswa.getPassword(), Toast.LENGTH_SHORT).show();
                        siswaDAO.setUser(siswa.getNis(), siswa.getPassword(), "1");

                        new AlertDialog.Builder(mContext)
                                .setIcon(R.drawable.success)
                                .setTitle("Berhasil")
                                .setMessage(pesan)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(mContext, MainSiswaActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        mContext.startActivity(i);
                                    }
                                }).show();
                    }else{
                        pd.dismiss();
                        Intent i = new Intent(mContext, MainSiswaActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("nis", nis);
                        mContext.startActivity(i);
                    }

                }else{
                    pd.dismiss();
                    new AlertDialog.Builder(mContext)
                            .setIcon(R.drawable.failed)
                            .setTitle("Gagal")
                            .setMessage(pesan)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                pd.dismiss();
                //Log.e("ERRRRRRRRRR : ", t.getMessage());
                Toast.makeText(mContext, "Gagal, harap periksa kembali koneksi anda", Toast.LENGTH_LONG).show();
            }
        });
    }

    private List<String> jawabanNo = null;
    private List<String> jawaban   = null;
    private Context mContext;
    private String date;
    private int totalSoal;

    public void setjawaban(List<String> jawabanNo, List<String> jawaban, Context mContext, String date, int totalSoal){
        this.jawabanNo = jawabanNo;
        this.jawaban   = jawaban;
        this.mContext  = mContext;
        this.date      = date;
        this.totalSoal = totalSoal;
    }

    public void jawaban(Context context){
//        for(int x = 0; x < jawabanNo.size(); x++){
//            Toast.makeText(mContext, jawabanNo.get(x) + " " + jawaban.get(x) + " | " + String.valueOf(jawabanNo.size()), Toast.LENGTH_SHORT).show();
//        }

        if(jawabanNo != null){
            int n = jawabanNo.size();
            final Integer[] no = new Integer[n];
            final String[] jawab = new String[n];

            for(int i = 0; i < n; i++){
                no[i] = Integer.parseInt(jawabanNo.get(i));
                jawab[i] = jawaban.get(i);
            }

            Call<Response> call = RetrofitClient
                    .getInstance()
                    .baseAPI()
                    .hasilSoal(no, jawab, date);

            call.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
//                    Toast.makeText(mContext, response.body().getError(), Toast.LENGTH_SHORT).show();
//
//                    String[] message = response.body().getMessage();
//                    String pesan = "";
//                    for(int i = 0; i < message.length; i++){
//                        pesan = pesan + message[i] + " ";
//                    }

                    //Toast.makeText(mContext, pesan + " " + siswa.getNis() + " " + date, Toast.LENGTH_SHORT).show();

                    String benar = response.body().getBenar();
                    int hitungsalah = totalSoal - Integer.parseInt(benar);
                    String salah = String.valueOf(hitungsalah);

                    int total = (Integer.parseInt(benar)*100)/totalSoal;
                    String nilai = String.valueOf(total);

                    //alertHasil(benar,salah,nilai);
                    if(!((Activity) mContext).isFinishing()) {
                        insertHasil(siswa.getNis(), benar, salah, nilai, date);
                    }else{
                        Toast.makeText(mContext, "Anda belum mengisi jawaban apapun!", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(context, "Anda belum mengisi jawaban apapun!", Toast.LENGTH_SHORT).show();
        }

    }

    public void alertHasil(String benar, String salah, String nilai){
        Dialog alertDialog;
        Button btnOK;
        TextView tvBenar, tvSalah, tvNilai;
        alertDialog = new Dialog(mContext);

        alertDialog.setContentView(R.layout.alert_hasilsoal);
        btnOK    = alertDialog.findViewById(R.id.btnOk);
        tvBenar  = alertDialog.findViewById(R.id.tvBenar);
        tvSalah  = alertDialog.findViewById(R.id.tvSalah);
        tvNilai  = alertDialog.findViewById(R.id.tvNilai);

        tvBenar.setText("Jawaban Benar : " + benar);
        tvSalah.setText("Jawaban Salah : " + salah);
        tvNilai.setText("Nilai Anda : " + nilai);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, MainSiswaActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mContext.startActivity(i);
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void insertHasil(String nis, final String benar, final String salah, final String nilai, String date){
        Call<Response> call = RetrofitClient
                .getInstance()
                .baseAPI()
                .insertHasil(nis, benar, salah, nilai, date);

        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if(response.body().getError().equals("0")){
                    alertHasil(benar,salah,nilai);
                }else{

                    if(!((Activity) mContext).isFinishing())
                    {
                        //show dialog
                        new AlertDialog.Builder(mContext)
                                .setIcon(R.drawable.warning)
                                .setTitle("Peringatan")
                                .setMessage("Anda sudah mengerjakan soal ini, anda hanya dapat mengerjakan soal ini 1 kali saja.")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(mContext, MainSiswaActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        mContext.startActivity(i);
                                    }
                                }).show();
                    }else{
                        Toast.makeText(mContext, "Anda belum mengisi jawaban apapun!", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
