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
import com.proyek1.mstasys.R;
import com.proyek1.mstasys.adapter.AdapterRankingSoal;
import com.proyek1.mstasys.api.RetrofitClient;
import com.proyek1.mstasys.guru.MainGuruActivity;
import com.proyek1.mstasys.response.HasilSoal;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HasilSoalActivity extends AppCompatActivity {

    private TextView tvKosong;
    private ProgressBar pb;
    private Context mContext;

    private RecyclerView recyclerView;
    private AdapterRankingSoal adapterRankingSoal;
    private RecyclerView.LayoutManager layoutManager;
    private List<HasilSoal> hasilSoalList;

    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_soal);

        mContext = this;
        tvKosong = findViewById(R.id.tvKosong);

        recyclerView = findViewById(R.id.recyclerRanking);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        pb = findViewById(R.id.pb);
        Sprite spkit = new CubeGrid();
        pb.setIndeterminateDrawable(spkit);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        status = (int) b.get("status");

        String date = (String) b.get("date");
        hasilSoal(date);

    }

    public void hasilSoal(final String date){
        pb.setVisibility(View.VISIBLE);
        tvKosong.setVisibility(View.INVISIBLE);

        Runnable rn = new Runnable() {
            @Override
            public void run() {
                Call<List<HasilSoal>> call = RetrofitClient
                        .getInstance()
                        .baseAPI()
                        .ranking(date);

                call.enqueue(new Callback<List<HasilSoal>>() {
                    @Override
                    public void onResponse(Call<List<HasilSoal>> call, Response<List<HasilSoal>> response) {
                        if(response.body().size() != 0){
                            pb.setVisibility(View.GONE);
                            tvKosong.setVisibility(View.INVISIBLE);

                            hasilSoalList = response.body();
                            adapterRankingSoal = new AdapterRankingSoal(mContext, hasilSoalList);
                            recyclerView.setAdapter(adapterRankingSoal);
                            adapterRankingSoal.notifyDataSetChanged();

                        }else{
                            pb.setVisibility(View.GONE);
                            tvKosong.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<HasilSoal>> call, Throwable t) {
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        };

        Handler handler = new Handler();
        handler.postDelayed(rn, 1500);

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
        Intent a = null;
        if(status == 0){
            Intent i = new Intent(mContext,MainSiswaActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            a = i;
        }else if(status == 1){
            Intent i = new Intent(mContext, MainGuruActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            a = i;
        }
        return a;
    }
}
