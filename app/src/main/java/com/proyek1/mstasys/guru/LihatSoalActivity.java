package com.proyek1.mstasys.guru;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.print.PrinterId;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.proyek1.mstasys.R;
import com.proyek1.mstasys.adapter.AdapterDetailSoal;
import com.proyek1.mstasys.api.RetrofitClient;
import com.proyek1.mstasys.entity.Guru;
import com.proyek1.mstasys.response.DetailSoal;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LihatSoalActivity extends AppCompatActivity {

    private Context mContext;
    private String nip, date_create;
    private TextView tvKosong, tvDeskripsi, tvWaktu, tvCreate, tvStatus;
    private Button btnHapusSoal,btnYa, btnTidak, btnAktifkanSoal;

    private List<DetailSoal> detailSoalList;
    private AdapterDetailSoal adapterDetailSoal;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar pb;
    private ProgressDialog pd;
    private Dialog alertDialog;

    private String status = null;

    private Guru guru = Guru.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_soal);

        mContext = this;
        pd       = new ProgressDialog(mContext);
        alertDialog = new Dialog(mContext);
        Intent i = getIntent();
        Bundle b = i.getExtras();

        pb = findViewById(R.id.pb);
        recyclerView = findViewById(R.id.recyclerLihatSoal);
        tvKosong     = findViewById(R.id.tvKosong);
        tvDeskripsi  = findViewById(R.id.tv_deskripsi);
        tvWaktu      = findViewById(R.id.tv_waktu);
        tvCreate     = findViewById(R.id.tv_create);
        tvStatus     = findViewById(R.id.tv_status);
        btnHapusSoal = findViewById(R.id.btnHapusSoal);
        btnAktifkanSoal= findViewById(R.id.btnAktifSoal);

        Sprite spkit = new ThreeBounce();
        pb.setIndeterminateDrawable(spkit);

        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        nip = guru.getNip();
        date_create = (String) b.get("date_create");

        lihatSoal(date_create);

        btnHapusSoal.setVisibility(View.INVISIBLE);
        btnAktifkanSoal.setVisibility(View.INVISIBLE);


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

    public void lihatSoal(final String date_create){
        pb.setVisibility(View.VISIBLE);
        tvKosong.setVisibility(View.INVISIBLE);

        Runnable rn = new Runnable() {
            @Override
            public void run() {
                Call<List<DetailSoal>> call = RetrofitClient
                        .getInstance()
                        .baseAPI()
                        .getLihatSoal(date_create);

                call.enqueue(new Callback<List<DetailSoal>>() {
                    @Override
                    public void onResponse(Call<List<DetailSoal>> call, Response<List<DetailSoal>> response) {
                        if(response.body().size() == 0){
                            pb.setVisibility(View.GONE);
                            tvKosong.setVisibility(View.VISIBLE);
                        }else{
                            pb.setVisibility(View.GONE);
                            tvKosong.setVisibility(View.INVISIBLE);
                            btnHapusSoal.setVisibility(View.VISIBLE);
                            btnAktifkanSoal.setVisibility(View.VISIBLE);

                            btnHapusSoal.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.setContentView(R.layout.alert_hapus);
                                    btnTidak = alertDialog.findViewById(R.id.btnTidak);
                                    btnYa    = alertDialog.findViewById(R.id.btnYa);

                                    btnYa.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            hapusSoal(date_create);
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


                            detailSoalList = response.body();
                            adapterDetailSoal = new AdapterDetailSoal(detailSoalList, mContext);
                            recyclerView.setAdapter(adapterDetailSoal);
                            adapterDetailSoal.notifyDataSetChanged();

                            for(int i = 0; i < detailSoalList.size(); i++){
                                tvDeskripsi.setText(detailSoalList.get(i).getDeskripsi());
                                tvWaktu.setText("Waktu Pengerjaan : " + detailSoalList.get(i).getWaktu_pengerjaan() + " menit");
                                tvCreate.setText("Diunggah pada   : " + detailSoalList.get(i).getDate_create());
                                status = detailSoalList.get(i).getStatus();
                            }

                            if(status.equals("0")){
                                btnAktifkanSoal.setText("Aktifkan Soal");
                                tvStatus.setText("Status soal : " + "Soal Belum Aktif");

                                btnAktifkanSoal.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        new AlertDialog.Builder(mContext)
                                                .setIcon(R.drawable.warning)
                                                .setTitle("Peringatan")
                                                .setMessage("Anda yakin ingin aktifkan soal ?")
                                                .setCancelable(false)
                                                .setNegativeButton("Iya", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        aktif();
                                                    }
                                                }).setPositiveButton("Tidak", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).show();
                                    }
                                });
                            }else{
                                btnAktifkanSoal.setText("Non Aktifkan Soal");
                                tvStatus.setText("Status soal : " + "Soal Sudah Aktif");

                                btnAktifkanSoal.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        new AlertDialog.Builder(mContext)
                                                .setIcon(R.drawable.warning)
                                                .setTitle("Peringatan")
                                                .setMessage("Anda yakin ingin non-aktifkan soal ?")
                                                .setCancelable(false)
                                                .setNegativeButton("Iya", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        nonAktif();
                                                    }
                                                }).setPositiveButton("Tidak", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).show();
                                    }
                                });
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<List<DetailSoal>> call, Throwable t) {

                    }
                });
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(rn, 2000);

    }

    public void aktif(){
        pd.setIcon(R.drawable.loading);
        pd.setTitle("Aktifkan Soal");
        pd.setMessage("Harap Menunggu. . .");
        pd.setCancelable(false);
        pd.show();

        Runnable rn = new Runnable() {
            @Override
            public void run() {
                Call<com.proyek1.mstasys.response.Response> call = RetrofitClient
                        .getInstance()
                        .baseAPI()
                        .aktifSoal(date_create, "1");

                call.enqueue(new Callback<com.proyek1.mstasys.response.Response>() {
                    @Override
                    public void onResponse(Call<com.proyek1.mstasys.response.Response> call, Response<com.proyek1.mstasys.response.Response> response) {
                        if(response.body().getError().equals("0")){
                            pd.dismiss();
                            new AlertDialog.Builder(mContext)
                                    .setIcon(R.drawable.success)
                                    .setTitle("Berhasil")
                                    .setMessage("Soal Aktif dan bisa dikerjakan oleh Siswa")
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(mContext, LihatSoalActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.putExtra("date_create", date_create);
                                    startActivity(i);
                                }
                            }).show();
                        }else{
                            pd.dismiss();
                            new AlertDialog.Builder(mContext)
                                    .setIcon(R.drawable.failed)
                                    .setTitle("Gagal")
                                    .setMessage("Soal gagal diaktifkan")
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent i = new Intent(mContext, LihatSoalActivity.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            i.putExtra("date_create", date_create);
                                            startActivity(i);
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
        handler.postDelayed(rn, 1500);
    }

    public void nonAktif(){
        pd.setIcon(R.drawable.loading);
        pd.setTitle("Non Aktifkan Soal");
        pd.setMessage("Harap Menunggu. . .");
        pd.setCancelable(false);
        pd.show();

        Runnable rn = new Runnable() {
            @Override
            public void run() {
                Call<com.proyek1.mstasys.response.Response> call = RetrofitClient
                        .getInstance()
                        .baseAPI()
                        .aktifSoal(date_create, "0");

                call.enqueue(new Callback<com.proyek1.mstasys.response.Response>() {
                    @Override
                    public void onResponse(Call<com.proyek1.mstasys.response.Response> call, Response<com.proyek1.mstasys.response.Response> response) {
                        if(response.body().getError().equals("0")){
                            pd.dismiss();
                            new AlertDialog.Builder(mContext)
                                    .setIcon(R.drawable.success)
                                    .setTitle("Berhasil")
                                    .setMessage("Soal Berhasil di Non Aktifkan")
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent i = new Intent(mContext, LihatSoalActivity.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            i.putExtra("date_create", date_create);
                                            startActivity(i);
                                        }
                                    }).show();
                        }else{
                            pd.dismiss();
                            new AlertDialog.Builder(mContext)
                                    .setIcon(R.drawable.failed)
                                    .setTitle("Gagal")
                                    .setMessage("Soal gagal diaktifkan")
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent i = new Intent(mContext, LihatSoalActivity.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            i.putExtra("date_create", date_create);
                                            startActivity(i);
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
        handler.postDelayed(rn, 1500);
    }

    public void hapusSoal(final String date_create){
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
                        .hapusSoal(date_create);

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
                                            Intent i = new Intent(mContext, LihatSoalActivity.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            i.putExtra("date_create", date_create);
                                            startActivity(i);
//                                            detailSoalList.clear();
//                                            lihatSoal(date_create);
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
        handler.postDelayed(rn, 2000);
    }
}
