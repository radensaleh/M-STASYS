package com.proyek1.mstasys.siswa;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.proyek1.mstasys.R;
import com.proyek1.mstasys.adapter.AdapterKerjakanSoal;
import com.proyek1.mstasys.adapter.AdapterSoalSiswa;
import com.proyek1.mstasys.api.RetrofitClient;
import com.proyek1.mstasys.entity.Siswa;
import com.proyek1.mstasys.response.DetailSoal;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KerjakanSoalActivity extends AppCompatActivity {

    private Context mContext;
    private String waktu, nis, date;
    private TextView tvDeskripsi, tvWaktu, tvCreate, tvMapel, tvJenis, tvDeadline;
    private Button btnSelesai;
    private ProgressBar pb;

    private CountDownTimer countDownTimer;
    private long timerMillis;

    private List<DetailSoal> detailSoalList;
    private AdapterKerjakanSoal adapterKerjakanSoal;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private Siswa siswa = Siswa.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kerjakan_soal);

        mContext = this;
        tvDeskripsi = findViewById(R.id.tv_deskripsi);
        tvWaktu     = findViewById(R.id.tv_waktu);
        tvCreate    = findViewById(R.id.tv_create);
        tvMapel     = findViewById(R.id.tv_mapel);
        tvJenis     = findViewById(R.id.tv_jenisMapel);
        tvDeadline  = findViewById(R.id.tv_batasWaktu);
        btnSelesai  = findViewById(R.id.btnSelesai);
        recyclerView= findViewById(R.id.recyclerKerjakanSoal);

        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        pb = findViewById(R.id.pb);
        Sprite spkit = new CubeGrid();
        pb.setIndeterminateDrawable(spkit);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        nis = siswa.getNis();

        waktu = (String) b.get("waktu");
        date  = (String) b.get("date");

        tvDeskripsi.setText((String) b.get("deskripsi"));
        tvWaktu.setText("Waktu Pengerjaan : " + waktu + " menit");
        tvCreate.setText("Diunggah pada : " + date);
        tvMapel.setText("Pelajaran : " + b.get("mapel"));
        tvJenis.setText("Kategori  : " + b.get("jenis_mapel"));
        setTitle((String) b.get("mapel"));

        btnSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert();
            }
        });

        soalnya();

    }

    public void soalnya(){
        pb.setVisibility(View.VISIBLE);

        Runnable rn = new Runnable() {
            @Override
            public void run() {
                Call<List<DetailSoal>> call = RetrofitClient
                        .getInstance()
                        .baseAPI()
                        .getLihatSoal(date);

                call.enqueue(new Callback<List<DetailSoal>>() {
                    @Override
                    public void onResponse(Call<List<DetailSoal>> call, Response<List<DetailSoal>> response) {
                        if(response.body().size() == 0){
                            pb.setVisibility(View.GONE);
                            Toast.makeText(mContext, "Kosong", Toast.LENGTH_SHORT).show();
                        }else{
                            pb.setVisibility(View.GONE);
                            detailSoalList = response.body();
                            adapterKerjakanSoal = new AdapterKerjakanSoal(mContext, detailSoalList, siswa, date);
                            recyclerView.setAdapter(adapterKerjakanSoal);
                            adapterKerjakanSoal.notifyDataSetChanged();
                            setDeadline();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<DetailSoal>> call, Throwable t) {

                    }
                });
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(rn, 1500);
    }

    public void setDeadline(){
        timerMillis = Integer.parseInt(waktu) * 60000;
        countDownTimer = new CountDownTimer(timerMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerMillis = millisUntilFinished;

                int hours   = (int) TimeUnit.MILLISECONDS.toHours(timerMillis) % 60;
                int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(timerMillis) % 60;
                int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(timerMillis) % 60;

                String deadline = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
                tvDeadline.setText("Batas Waktu : " + deadline);
            }

            @Override
            public void onFinish() {
                Toast.makeText(mContext, "Waktu Selesaii", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    public void alert(){
        new AlertDialog.Builder(mContext)
                .setIcon(R.drawable.warning)
                .setTitle("Peringatan")
                .setMessage("Anda yakin ingin mengunggah jawaban anda ? Harap periksa kembali jawaban anda")
                .setCancelable(false)
                .setNegativeButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hasil();
                    }
                }).setPositiveButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    public void hasil(){
        siswa.jawaban(mContext);
    }


    @Override
    protected void onPause() {
        super.onPause();
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Toast.makeText(mContext, "Back", Toast.LENGTH_SHORT).show();
        }else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            Toast.makeText(mContext, "VOL Down", Toast.LENGTH_SHORT).show();
        }else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            Toast.makeText(mContext, "VOL UP", Toast.LENGTH_SHORT).show();
        }
        return super.onKeyDown(keyCode, event);
    }

}
