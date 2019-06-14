package com.proyek1.mstasys.entity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.proyek1.mstasys.R;
import com.proyek1.mstasys.api.RetrofitClient;
import com.proyek1.mstasys.guru.MainGuruActivity;
import com.proyek1.mstasys.response.Response;
import com.proyek1.mstasys.siswa.MainSiswaActivity;
import com.proyek1.mstasys.sqlLite.GuruDAO;

import retrofit2.Call;
import retrofit2.Callback;

public class Guru extends Person{
    private Guru(){}
    private String nip;
    private String walikelas;

    private static Guru guru;
    public static Guru getInstance(){
        if(guru==null){
            guru = new Guru();
        }
        return guru;
    }

    public String getNip() {
        return nip;
    }
    public void setNip(String nip) {
        this.nip = nip;
    }
    public String getWalikelas() {
        return walikelas;
    }
    public void setWalikelas(String walikelas) {
        this.walikelas = walikelas;
    }

    public void loginGuru(final String nip, final String password, final Context mContext){
        //Toast.makeText(mContext, nip + " " + password, Toast.LENGTH_SHORT).show();
        Call<Response> res = RetrofitClient
                .getInstance()
                .baseAPI()
                .loginGuru(nip, password);

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
                    Guru guru = Guru.getInstance();
                    guru.setNip(nip);
                    guru.setPassword(password);

                    GuruDAO guruDAO = new GuruDAO(mContext);
                    if(guruDAO.getUser()==null){
                        guruDAO.setUser(guru.getNip(), guru.getPassword(), "1");

                        Intent i = new Intent(mContext, MainGuruActivity.class);
                        mContext.startActivity(i);
                    }else{
                        Intent i = new Intent(mContext, MainGuruActivity.class);
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

    public void loginGuru(final String nip, String password, final ProgressDialog pd, final Context mContext){
        this.nip = nip;
        this.setPassword(password);

        Call<Response> res = RetrofitClient
                .getInstance()
                .baseAPI()
                .loginGuru(nip, password);

        res.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                String error   = response.body().getError();
                String[] message = response.body().getMessage();
                String pesan = "";
                for(int i = 0; i < message.length; i++){
                    pesan = pesan + message[i] + "\n";
                }

                Guru guru = Guru.getInstance();

                if(error.equals("0")){
                    GuruDAO guruDAO = new GuruDAO(mContext);
                    if(guruDAO.getUser() == null){
                        pd.dismiss();
                        //Toast.makeText(mContext, guru.getNip() + " " + guru.getPassword(), Toast.LENGTH_SHORT).show();
                        guruDAO.setUser(guru.getNip(), guru.getPassword(), "1");

                        new AlertDialog.Builder(mContext)
                                .setIcon(R.drawable.success)
                                .setTitle("Berhasil")
                                .setMessage(pesan)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(mContext, MainGuruActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        mContext.startActivity(i);
                                    }
                                }).show();
                    }else{
                        pd.dismiss();
                        Intent i = new Intent(mContext, MainGuruActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
}
