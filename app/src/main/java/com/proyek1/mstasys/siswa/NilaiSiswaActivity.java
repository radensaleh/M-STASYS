package com.proyek1.mstasys.siswa;

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
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.github.ybq.android.spinkit.style.Wave;
import com.proyek1.mstasys.R;
import com.proyek1.mstasys.adapter.AdapterNilaiSiswa;
import com.proyek1.mstasys.api.RetrofitClient;
import com.proyek1.mstasys.entity.Siswa;
import com.proyek1.mstasys.response.NilaiSiswa;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NilaiSiswaActivity extends AppCompatActivity {

    private Context mContext;
    private List<NilaiSiswa> nilaiSiswaList;
    private AdapterNilaiSiswa adapterNilaiSiswa;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private TextView tvNama, tvNip, tvKosong, tvNotif, tvRata, tvKriteria;
    private String nis;

    private ProgressBar pb;
    private int status = 0;

    private Siswa siswa = Siswa.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nilai_siswa);

        mContext = this;
        recyclerView = findViewById(R.id.recyclerNilaiSiswa);
        tvNama   = findViewById(R.id.tv_nama);
        tvNip    = findViewById(R.id.tv_nip);
        tvKosong = findViewById(R.id.tvKosong);
        tvNotif  = findViewById(R.id.tvNotif);
        tvRata   = findViewById(R.id.tv_rata);
        tvKriteria = findViewById(R.id.tv_kriteria);

        pb = findViewById(R.id.pb);
        Sprite spkit = new ThreeBounce();
        pb.setIndeterminateDrawable(spkit);

        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        String id_ampu = (String) b.get("id_ampu");
        String nip     = (String) b.get("nip");
        String nama    = (String) b.get("nama");
        String mapel   = (String) b.get("mapel");
        nis     = siswa.getNis();

        setTitle(mapel);

        tvNama.setText(nama);
        tvNip.setText("NIP. " + nip);

        getNilai(nis, id_ampu);

    }

    public void getNilai(final String nis, final String id_ampu){
        pb.setVisibility(View.VISIBLE);
        tvKosong.setVisibility(View.INVISIBLE);
        tvNotif.setVisibility(View.INVISIBLE);

        Runnable rn = new Runnable() {
            @Override
            public void run() {
                Call<List<NilaiSiswa>> call = RetrofitClient
                        .getInstance()
                        .baseAPI()
                        .getNilaiSiswa(nis, id_ampu);

                call.enqueue(new Callback<List<NilaiSiswa>>() {
                    @Override
                    public void onResponse(Call<List<NilaiSiswa>> call, Response<List<NilaiSiswa>> response) {
                        if(response.body().size() == 0){
                            pb.setVisibility(View.GONE);
                            tvKosong.setVisibility(View.VISIBLE);
                            tvNotif.setVisibility(View.VISIBLE);
                        }else{
                            pb.setVisibility(View.GONE);
                            nilaiSiswaList = response.body();
                            adapterNilaiSiswa = new AdapterNilaiSiswa(nilaiSiswaList, mContext, status);
                            recyclerView.setAdapter(adapterNilaiSiswa);
                            adapterNilaiSiswa.notifyDataSetChanged();
                            tvKosong.setVisibility(View.INVISIBLE);
                            tvNotif.setVisibility(View.INVISIBLE);

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
        Intent i = new Intent(mContext,MainSiswaActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return i;
    }

}
