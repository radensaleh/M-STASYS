package com.proyek1.mstasys.guru;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.proyek1.mstasys.R;
import com.proyek1.mstasys.adapter.AdapterNilaiSiswa;
import com.proyek1.mstasys.api.RetrofitClient;
import com.proyek1.mstasys.entity.Guru;
import com.proyek1.mstasys.response.NilaiSiswa;
import com.proyek1.mstasys.siswa.MainSiswaActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LihatNilaiActivity extends AppCompatActivity {

    private Context mContext;
    private String nip,nis, nama, mapel, kelas, semester, id_mapel, id_kelas, id_smstr;
    private TextView tvNis, tvNama, tvRata, tvKriteria, tvKosong;

    private List<NilaiSiswa> nilaiSiswaList;
    private AdapterNilaiSiswa adapterNilaiSiswa;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar pb;
    private int status = 1;

    private Guru guru = Guru.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_nilai);

        mContext = this;
        tvNis    = findViewById(R.id.tv_nis);
        tvNama   = findViewById(R.id.tv_nama);
        tvRata   = findViewById(R.id.tv_rata);
        tvKosong = findViewById(R.id.tvKosong);
        tvKriteria   = findViewById(R.id.tv_kriteria);
        recyclerView = findViewById(R.id.recyclerNilaiSiswa);

        pb = findViewById(R.id.pb);
        Sprite spkit = new ThreeBounce();
        pb.setIndeterminateDrawable(spkit);

        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        nip = guru.getNip();
        nis  = (String) b.get("nis");
        nama = (String) b.get("nama");
        id_mapel = (String) b.get("id_mapel");
        mapel    = (String) b.get("nama_mapel");
        id_kelas = (String) b.get("id_kelas");
        kelas    = (String) b.get("nama_kelas");
        id_smstr = (String) b.get("id_semester");
        semester = (String) b.get("semester");

        tvNis.setText("NIS. " + nis + "  |  " + kelas + "  |  Semester " + semester);
        tvNama.setText(nama);

        setTitle(mapel);

        getNilai(nis, nip, id_mapel, id_kelas, id_smstr);

    }

    public void getNilai(final String nis, final String nip, final String id_mapel, final String id_kelas, final String id_semester){
        pb.setVisibility(View.VISIBLE);
        tvKosong.setVisibility(View.INVISIBLE);

        Runnable rn  = new Runnable() {
            @Override
            public void run() {
                Call<List<NilaiSiswa>> call = RetrofitClient
                        .getInstance()
                        .baseAPI()
                        .getLihatNilai(nis, nip, id_mapel, id_kelas, id_semester);

                call.enqueue(new Callback<List<NilaiSiswa>>() {
                    @Override
                    public void onResponse(Call<List<NilaiSiswa>> call, Response<List<NilaiSiswa>> response) {
                        if(response.body().size() == 0){
                            pb.setVisibility(View.GONE);
                            tvKosong.setVisibility(View.VISIBLE);
                        }else{
                            pb.setVisibility(View.GONE);
                            nilaiSiswaList = response.body();
                            adapterNilaiSiswa = new AdapterNilaiSiswa(nilaiSiswaList, mContext, status);
                            adapterNilaiSiswa.setData(nip, nis, nama, mapel, kelas, semester, id_mapel, id_kelas, id_semester);
                            recyclerView.setAdapter(adapterNilaiSiswa);
                            adapterNilaiSiswa.notifyDataSetChanged();
                            tvKosong.setVisibility(View.INVISIBLE);

                            int total = 0;
                            for(int i = 0; i < nilaiSiswaList.size(); i++){
                                total += Integer.parseInt(nilaiSiswaList.get(i).getNilai());
                            }

                            int rata = total/nilaiSiswaList.size();
                            tvRata.setText("Rata - Rata : " + String.valueOf(rata));

                            if(rata <= 100 && rata >= 92){
                                tvKriteria.setText("Kriteria : A");
                            }else if(rata <= 91 && rata >= 83){
                                tvKriteria.setText("Kriteria : B");
                            }else if(rata <= 82 && rata >= 75){
                                tvKriteria.setText("Kriteria : C");
                            }else{
                                tvKriteria.setText("Kriteria : D");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<NilaiSiswa>> call, Throwable t) {

                    }
                });
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(rn, 1000);
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
