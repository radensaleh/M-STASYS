package com.proyek1.mstasys.guru;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.proyek1.mstasys.R;
import com.proyek1.mstasys.adapter.AdapterAmpuMapel;
import com.proyek1.mstasys.api.RetrofitClient;
import com.proyek1.mstasys.entity.Guru;
import com.proyek1.mstasys.response.AmpuMapel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarAmpuFragment extends Fragment {

    private Context mContext;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView tvKosong;
    private ProgressBar pb;

    private String nip;
    private Guru guru = Guru.getInstance();

    private List<AmpuMapel> ampuMapels;
    private AdapterAmpuMapel adapterAmpuMapel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daftar_ampu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();

        tvKosong = view.findViewById(R.id.tvKosong);
        recyclerView = view.findViewById(R.id.recyclerAmpu);
        pb = view.findViewById(R.id.pb);

        Sprite spkit = new CubeGrid();
        pb.setIndeterminateDrawable(spkit);

        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        nip = guru.getNip();

        getAmpuMapel();

    }

    public void getAmpuMapel(){
        tvKosong.setVisibility(View.INVISIBLE);
        pb.setVisibility(View.VISIBLE);

        Runnable rn = new Runnable() {
            @Override
            public void run() {
                Call<List<AmpuMapel>> call = RetrofitClient
                        .getInstance()
                        .baseAPI()
                        .ampuMapel(nip);

                call.enqueue(new Callback<List<AmpuMapel>>() {
                    @Override
                    public void onResponse(Call<List<AmpuMapel>> call, Response<List<AmpuMapel>> response) {
                        if(response.body().size() == 0){
                            pb.setVisibility(View.GONE);
                            tvKosong.setVisibility(View.VISIBLE);
                        }else{
                            pb.setVisibility(View.GONE);
                            tvKosong.setVisibility(View.INVISIBLE);

                            ampuMapels = response.body();
                            adapterAmpuMapel = new AdapterAmpuMapel(mContext, ampuMapels);
                            recyclerView.setAdapter(adapterAmpuMapel);
                            adapterAmpuMapel.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AmpuMapel>> call, Throwable t) {
                        pb.setVisibility(View.GONE);
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(rn, 2000);
    }
}
