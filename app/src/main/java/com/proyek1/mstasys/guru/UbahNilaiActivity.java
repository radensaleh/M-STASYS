package com.proyek1.mstasys.guru;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.proyek1.mstasys.R;
import com.proyek1.mstasys.api.RetrofitClient;
import com.proyek1.mstasys.entity.Guru;
import com.proyek1.mstasys.response.DetailNilai;
import com.proyek1.mstasys.response.Mapel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UbahNilaiActivity extends AppCompatActivity {

    private Context mContext;
    private TextView tvNama, tvNIS, tvMapel, tvJenisMapel;
    private EditText etNilai;
    private String nip, id_detail, nis, nama, id_mapel, mapel, id_kelas, kelas, id_smstr, semester;
    private Spinner spDetailNilai;
    private Button btnUbah, btnHapus,btnYa, btnTidak;
    private ProgressDialog pd;

    private Dialog alertDialog;

    private Guru guru = Guru.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_nilai);

        mContext    = this;
        pd          = new ProgressDialog(mContext);
        alertDialog = new Dialog(mContext);

        tvNIS    = findViewById(R.id.tv_nis);
        tvNama   = findViewById(R.id.tv_nama);
        tvMapel  = findViewById(R.id.tv_mapel);
        etNilai  = findViewById(R.id.etNilaiSiswa);
        tvJenisMapel = findViewById(R.id.tv_jenisMapel);
        btnUbah  = findViewById(R.id.btnUbahNilai);
        btnHapus = findViewById(R.id.btnHapusNilai);

        spDetailNilai = findViewById(R.id.spDetailNilai);
        spDetailNilai.setClickable(false);
        spDetailNilai.setEnabled(false);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        nip         = guru.getNip();
        id_detail   = (String) b.get("id_detail");
        nis  = (String) b.get("nis");
        nama = (String) b.get("nama");

        id_mapel = (String) b.get("id_mapel");
        id_kelas = (String) b.get("id_kelas");
        id_smstr = (String) b.get("id_semester");
        final String id_nilai = (String) b.get("id_nilai");

        mapel    = (String) b.get("nama_mapel");
        kelas    = (String) b.get("nama_kelas");
        semester = (String) b.get("semester");
        String nilai    = (String) b.get("nilai");

        etNilai.setText(nilai);

        tvNama.setText(nama);
        tvNIS.setText("NIS. " + nis + "  |  " + kelas + "  |  Semester " + semester);

        tvMapel.setText("Pelajaran  : " + mapel);

        getJenisMapel(id_mapel, id_kelas, id_smstr);
        getJenisNilai();

        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nilai = etNilai.getText().toString();

                int max_nilai = 0;
                if(!nilai.isEmpty()){
                    max_nilai= Integer.parseInt(nilai);
                }

                if(nilai.isEmpty()){
                    etNilai.setError("Nilai Kosong");
                }else if(max_nilai > 100 || max_nilai < 0 || nilai.length() > 3){
                    new AlertDialog.Builder(mContext)
                            .setIcon(R.drawable.warning)
                            .setTitle("Peringatan")
                            .setMessage("Hanya berisikan maksimal 3 Digit. Masukan nilai dari 0 - 100")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }else if(id_detail == null){
                    new AlertDialog.Builder(mContext)
                            .setIcon(R.drawable.warning)
                            .setTitle("Peringatan")
                            .setMessage("Harap Memilih Kategori")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }else{
                    ubahNilai(id_nilai, nilai, id_detail);
                }
            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.setContentView(R.layout.alert_hapus);
                btnTidak = alertDialog.findViewById(R.id.btnTidak);
                btnYa    = alertDialog.findViewById(R.id.btnYa);

                btnYa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hapusNilai(id_nilai, id_detail);
                    }
                });

                btnTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        });

    }

    public void getJenisMapel(final String id_mapel, final String id_kelas, final String id_semester){
        Call<Mapel> call = RetrofitClient
                .getInstance()
                .baseAPI()
                .getKategoriMapel(id_mapel, id_kelas, id_semester);

        call.enqueue(new Callback<Mapel>() {
            @Override
            public void onResponse(Call<Mapel> call, Response<Mapel> response) {
                String kategori = response.body().getKategori_mapel();
                tvJenisMapel.setText("Jenis Mapel : " + kategori);
            }

            @Override
            public void onFailure(Call<Mapel> call, Throwable t) {

            }
        });

    }

    public void getJenisNilai(){
        Call<List<DetailNilai>> call = RetrofitClient
                .getInstance()
                .baseAPI()
                .getJenisNilai();

        call.enqueue(new Callback<List<DetailNilai>>() {
            @Override
            public void onResponse(Call<List<DetailNilai>> call, Response<List<DetailNilai>> response) {
                final List<DetailNilai> detailNilaiList = response.body();
                final List<String> nilai = new ArrayList<>();

                nilai.add(0, "  Pilih ");

                for(int i = 0; i < detailNilaiList.size(); i++){
                    nilai.add(detailNilaiList.get(i).getJenis_nilai());
                }

                ArrayAdapter<String> adapterDetailNilai = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, nilai);
                adapterDetailNilai.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spDetailNilai.setAdapter(adapterDetailNilai);

                for(int i = 0; i < detailNilaiList.size(); i++){
                    if(id_detail.equals(detailNilaiList.get(i).getId_detail())){
                        spDetailNilai.setSelection(i+1);
                    }
                }

                spDetailNilai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        for(int i = 0; i < detailNilaiList.size(); i++){
                            if(position == 0){
                                id_detail = null;
                            }else if(position == i+1){
                                id_detail = detailNilaiList.get(i).getId_detail();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onFailure(Call<List<DetailNilai>> call, Throwable t) {

            }
        });
    }

    public void ubahNilai(final String id_nilai, final String nilai, final String id_detail){
        pd.setIcon(R.drawable.edit);
        pd.setTitle("Perbaharui Data");
        pd.setMessage("Harap Menunggu. . .");
        pd.setCancelable(false);
        pd.show();

        Runnable rn = new Runnable() {
            @Override
            public void run() {
                Call<com.proyek1.mstasys.response.Response> call = RetrofitClient
                        .getInstance()
                        .baseAPI()
                        .ubahNilai(id_nilai, nilai, id_detail);

                call.enqueue(new Callback<com.proyek1.mstasys.response.Response>() {
                    @Override
                    public void onResponse(Call<com.proyek1.mstasys.response.Response> call, Response<com.proyek1.mstasys.response.Response> response) {
                        String error = response.body().getError();

                        if(error.equals("0")){
                            pd.dismiss();
                            new AlertDialog.Builder(mContext)
                                    .setIcon(R.drawable.success)
                                    .setTitle("Berhasil")
                                    .setMessage("Data Diperbaharui")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent i = new Intent(mContext, UbahNilaiActivity.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            i.putExtra("nis", nis);
                                            i.putExtra("nama", nama);
                                            i.putExtra("id_mapel", id_mapel);
                                            i.putExtra("nama_mapel", mapel);
                                            i.putExtra("id_kelas", id_kelas);
                                            i.putExtra("nama_kelas", kelas);
                                            i.putExtra("id_semester", id_smstr);
                                            i.putExtra("semester", semester);
                                            i.putExtra("nilai", nilai);
                                            i.putExtra("id_detail", id_detail);
                                            i.putExtra("id_nilai", id_nilai);
                                            startActivity(i);
                                        }
                                    }).show();
                        }else{
                            pd.dismiss();
                            new AlertDialog.Builder(mContext)
                                    .setIcon(R.drawable.failed)
                                    .setTitle("Gagagl")
                                    .setMessage("Gagal Diperbaharui")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<com.proyek1.mstasys.response.Response> call, Throwable t) {
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(rn, 2000);
    }

    public void hapusNilai(final String id_nilai, final String id_detail){
        alertDialog.dismiss();
        pd.setIcon(R.drawable.loading);
        pd.setTitle("Menghapus Data");
        pd.setMessage("Harap Menunggu. . .");
        pd.setCancelable(false);
        pd.show();

        Runnable rn = new Runnable() {
            @Override
            public void run() {
                Call<com.proyek1.mstasys.response.Response> call = RetrofitClient
                        .getInstance()
                        .baseAPI()
                        .hapusNilai(id_nilai, id_detail);

                call.enqueue(new Callback<com.proyek1.mstasys.response.Response>() {
                    @Override
                    public void onResponse(Call<com.proyek1.mstasys.response.Response> call, Response<com.proyek1.mstasys.response.Response> response) {
                        String error = response.body().getError();

                        if(error.equals("0")){
                            pd.dismiss();
                            new AlertDialog.Builder(mContext)
                                    .setIcon(R.drawable.success)
                                    .setTitle("Berhasil")
                                    .setMessage("Data Telah Dihapus")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent i = new Intent(mContext, LihatNilaiActivity.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            i.putExtra("nis", nis);
                                            i.putExtra("nama", nama);
                                            i.putExtra("id_mapel", id_mapel);
                                            i.putExtra("nama_mapel", mapel);
                                            i.putExtra("id_kelas", id_kelas);
                                            i.putExtra("nama_kelas", kelas);
                                            i.putExtra("id_semester", id_smstr);
                                            i.putExtra("semester", semester);
                                            startActivity(i);
                                        }
                                    }).show();
                        }else{
                            pd.dismiss();
                            new AlertDialog.Builder(mContext)
                                    .setIcon(R.drawable.failed)
                                    .setTitle("Gagagl")
                                    .setMessage("Gagal Dihapus")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<com.proyek1.mstasys.response.Response> call, Throwable t) {

                    }
                });
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(rn , 2000);
    }

    @Override
    public Intent getSupportParentActivityIntent() {
        return getParentActivityIntentImpl();
    }

    @Override
    public Intent getParentActivityIntent() {
        return getParentActivityIntentImpl();
    }

    private Intent getParentActivityIntentImpl() {
        Intent i = new Intent(mContext, LihatNilaiActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        i.putExtra("nis", nis);
        i.putExtra("nama", nama);
        i.putExtra("id_mapel", id_mapel);
        i.putExtra("nama_mapel", mapel);
        i.putExtra("id_kelas", id_kelas);
        i.putExtra("nama_kelas", kelas);
        i.putExtra("id_semester", id_smstr);
        i.putExtra("semester", semester);
        return i;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(mContext, LihatNilaiActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        i.putExtra("nis", nis);
        i.putExtra("nama", nama);
        i.putExtra("id_mapel", id_mapel);
        i.putExtra("nama_mapel", mapel);
        i.putExtra("id_kelas", id_kelas);
        i.putExtra("nama_kelas", kelas);
        i.putExtra("id_semester", id_smstr);
        i.putExtra("semester", semester);
        startActivity(i);
    }
}
