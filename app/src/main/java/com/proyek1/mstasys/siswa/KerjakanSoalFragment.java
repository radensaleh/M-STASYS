package com.proyek1.mstasys.siswa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.proyek1.mstasys.R;
import com.proyek1.mstasys.adapter.AdapterSoalSiswa;
import com.proyek1.mstasys.api.RetrofitClient;
import com.proyek1.mstasys.entity.Siswa;
import com.proyek1.mstasys.response.Semester;
import com.proyek1.mstasys.response.Soal;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KerjakanSoalFragment extends Fragment {

    private Context mContext;
    private String nis;
    private Spinner spSemester;
    private String id_kelas, id_semester;
    private ProgressBar pb;
    private TextView tvKosong, tvPilih;

    private List<Soal> soalList;
    private AdapterSoalSiswa adapterSoalSiswa;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private Siswa siswa = Siswa.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kerjakan_soal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();

        nis = siswa.getNis();

        pb = view.findViewById(R.id.pb);

        Sprite spkit = new CubeGrid();
        pb.setIndeterminateDrawable(spkit);

        spSemester = view.findViewById(R.id.spSemester);
        tvKosong   = view.findViewById(R.id.tvKosong);
        tvPilih    = view.findViewById(R.id.tvPilih);
        recyclerView  = view.findViewById(R.id.recyclerSoal);

        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        Call<Siswa> call = RetrofitClient
                .getInstance()
                .baseAPI()
                .dataSiswa(nis);

        call.enqueue(new Callback<Siswa>() {
            @Override
            public void onResponse(Call<Siswa> call, Response<Siswa> response) {
                id_kelas = response.body().getId_kelas();
                getSemester();
            }

            @Override
            public void onFailure(Call<Siswa> call, Throwable t) {
                Log.e("ERRRRRR : ", t.getMessage());
            }
        });

    }

    public void getSemester(){
        Call<List<Semester>> call = RetrofitClient
                .getInstance()
                .baseAPI()
                .getSemesterSiswa(id_kelas);

        call.enqueue(new Callback<List<Semester>>() {
            @Override
            public void onResponse(Call<List<Semester>> call, Response<List<Semester>> response) {
                final List<Semester> allSemester = response.body();
                final List<String> smstr = new ArrayList<>();
                smstr.add(0, "Pilih");

                for(int i = 0 ; i < allSemester.size(); i++){
                    smstr.add(allSemester.get(i).getThn_ajaran()+'-'+allSemester.get(i).getSemester());
                }

                ArrayAdapter<String> adapterSmstr = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, smstr);
                adapterSmstr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spSemester.setAdapter(adapterSmstr);

                spSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        for(int i = 0; i < allSemester.size(); i++){
                            if(position == 0){
                                id_semester = null;
                            }else if(position == i+1){
                                id_semester = allSemester.get(i).getId_semester();
                            }
                        }
                        getSoal();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<Semester>> call, Throwable t) {

            }
        });
    }

    public void getSoal(){
        if(id_semester == null) {
            if(soalList != null){
                soalList.clear();
                adapterSoalSiswa.notifyDataSetChanged();
                tvPilih.setVisibility(View.VISIBLE);
                tvKosong.setVisibility(View.INVISIBLE);
            }
            tvPilih.setVisibility(View.VISIBLE);
            tvKosong.setVisibility(View.INVISIBLE);
        }else{
            if(soalList != null){
                soalList.clear();
                adapterSoalSiswa.notifyDataSetChanged();
            }

            tvKosong.setVisibility(View.INVISIBLE);
            tvPilih.setVisibility(View.INVISIBLE);
            pb.setVisibility(View.VISIBLE);

            Runnable rn = new Runnable() {
                @Override
                public void run() {
                    Call<List<Soal>> call = RetrofitClient
                            .getInstance()
                            .baseAPI()
                            .getSiswaSoal(id_kelas, id_semester);

                    call.enqueue(new Callback<List<Soal>>() {
                        @Override
                        public void onResponse(Call<List<Soal>> call, Response<List<Soal>> response) {
                            if (response.body().size() == 0) {
                                pb.setVisibility(View.GONE);
                                tvKosong.setVisibility(View.VISIBLE);
                            } else {
                                pb.setVisibility(View.GONE);
                                tvKosong.setVisibility(View.INVISIBLE);
                                soalList = response.body();
                                adapterSoalSiswa = new AdapterSoalSiswa(soalList, mContext, nis);
                                recyclerView.setAdapter(adapterSoalSiswa);
                                adapterSoalSiswa.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Soal>> call, Throwable t) {

                        }
                    });
                }
            };
            Handler handler = new Handler();
            handler.postDelayed(rn, 2000);
        }
    }
}
