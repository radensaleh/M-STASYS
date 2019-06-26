package com.proyek1.mstasys.guru;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class TambahNilaiActivity extends AppCompatActivity {

    private Context mContext;
    private TextView tvNama, tvNIS, tvMapel, tvJenisMapel;
    private String nip, nis;

    private Button btnTambah;
    private EditText etNilai;
    private Spinner spDetailNilai;
    private String id_detail = null;

    private ProgressDialog pd;

    private Guru guru = Guru.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_nilai);

        mContext = this;
        pd = new ProgressDialog(mContext);

        tvNIS    = findViewById(R.id.tv_nis);
        tvNama   = findViewById(R.id.tv_nama);
        tvMapel  = findViewById(R.id.tv_mapel);
        etNilai  = findViewById(R.id.etNilaiSiswa);
        tvJenisMapel = findViewById(R.id.tv_jenisMapel);
        btnTambah    = findViewById(R.id.btnTambahNilai);

        spDetailNilai = findViewById(R.id.spDetailNilai);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        nip = guru.getNip();
        nis  = (String) b.get("nis");
        String nama = (String) b.get("nama");
        final String id_mapel = (String) b.get("id_mapel");
        String mapel    = (String) b.get("nama_mapel");
        final String id_kelas = (String) b.get("id_kelas");
        String kelas    = (String) b.get("nama_kelas");
        final String id_smstr = (String) b.get("id_semester");
        String semester = (String) b.get("semester");

        tvNama.setText(nama);
        tvNIS.setText("NIS. " + nis + "  |  " + kelas + "  |  Semester " + semester);

        tvMapel.setText("Pelajaran  : " + mapel);

        getJenisMapel(id_mapel, id_kelas, id_smstr);
        getJenisNilai();

        btnTambah.setOnClickListener(new View.OnClickListener() {
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
                    tambahNilai(nis, nip, id_mapel, id_kelas, id_smstr, id_detail, nilai);
                }
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

    public void tambahNilai(final String nis, final String nip, final String id_mapel,
                            final String id_kelas, final String id_semester, final String id_detail,
                            final String nilai){
        pd.setIcon(R.drawable.loading);
        pd.setTitle("Tambah Data");
        pd.setMessage("Harap Menunggu. . .");
        pd.setCancelable(false);
        pd.show();

        Runnable rn = new Runnable() {
            @Override
            public void run() {
                Call<com.proyek1.mstasys.response.Response> call = RetrofitClient
                        .getInstance()
                        .baseAPI()
                        .tambahNilai(nis, nip, id_mapel, id_kelas, id_semester, id_detail, nilai);

                call.enqueue(new Callback<com.proyek1.mstasys.response.Response>() {
                    @Override
                    public void onResponse(Call<com.proyek1.mstasys.response.Response> call, Response<com.proyek1.mstasys.response.Response> response) {
                        String error = response.body().getError();
                        if(response.message().equals("OK")){
                        if(error.equals("0")){
                            pd.dismiss();
                            new AlertDialog.Builder(mContext)
                                    .setIcon(R.drawable.success)
                                    .setTitle("Berhasil")
                                    .setMessage("Data Ditambahkan")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            etNilai.getText().clear();
                                            etNilai.requestFocus();
                                            spDetailNilai.setSelection(0);
                                        }
                                    }).show();
                        }else{
                            pd.dismiss();
                            new AlertDialog.Builder(mContext)
                                    .setIcon(R.drawable.failed)
                                    .setTitle("Gagagl")
                                    .setMessage("Gagal Ditambahkan")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();
                        }
                    }else{
                            pd.dismiss();
                            new AlertDialog.Builder(mContext)
                                    .setIcon(R.drawable.failed)
                                    .setTitle("Error")
                                    .setMessage(response.message()+"-Terjadi Kesalahan")
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
                        pd.dismiss();
                    }
                });
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(rn, 2000);

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
        Intent i = new Intent(mContext, MainGuruActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return i;
    }
}
