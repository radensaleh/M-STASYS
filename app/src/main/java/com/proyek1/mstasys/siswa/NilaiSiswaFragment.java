package com.proyek1.mstasys.siswa;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.github.ybq.android.spinkit.style.Wave;
import com.proyek1.mstasys.R;
import com.proyek1.mstasys.adapter.AdapterMapelSiswa;
import com.proyek1.mstasys.api.RetrofitClient;
import com.proyek1.mstasys.entity.Siswa;
import com.proyek1.mstasys.response.MapelSiswa;
import com.proyek1.mstasys.response.Semester;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NilaiSiswaFragment extends Fragment {

    private Context mContext;
    private Spinner spSemester;
    private TextView tvKosong, tvPilihSemester, tvPeringkat;
    private String id_semester = null;
    private String id_kelas;

    private ProgressBar pb;

    private List<MapelSiswa> mapelSiswaList;
    private AdapterMapelSiswa adapterMapelSiswa;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private String nis;

    private Siswa siswa = Siswa.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nilai_siswa2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();

        nis = siswa.getNis();

        spSemester = view.findViewById(R.id.spSemester);
        tvKosong   = view.findViewById(R.id.tvKosong);
        tvPilihSemester = view.findViewById(R.id.tvPilih);
        recyclerView    = view.findViewById(R.id.recyclerPelajaran);

        pb = view.findViewById(R.id.pb);

        Sprite spkit = new CubeGrid();
        pb.setIndeterminateDrawable(spkit);

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
//                if(response.body().getTingkat().equals("X")){
//                    smstX();
//                    id_kelas = response.body().getId_kelas();
//                }else if(response.body().getTingkat().equals("XI")){
//                    smstXI();
//                    id_kelas = response.body().getId_kelas();
//                }else if(response.body().getTingkat().equals("XII")){
//                    smstXII();
//                    id_kelas = response.body().getId_kelas();
//                }
                  id_kelas = response.body().getId_kelas();
                  getSemester();
            }

            @Override
            public void onFailure(Call<Siswa> call, Throwable t) {
                Log.e("ERRRRRR : ", t.getMessage());
            }
        });

    }

    public void smstX(){
        Call<List<Semester>> call = RetrofitClient
                .getInstance()
                .baseAPI()
                .getSemester();

        call.enqueue(new Callback<List<Semester>>() {
            @Override
            public void onResponse(Call<List<Semester>> call, Response<List<Semester>> response) {
                final List<Semester> allSemester = response.body();
                final List<String> smstr = new ArrayList<>();
                smstr.add(0, "Pilih");

                for(int i = 0 ; i <= 1; i++){
                    smstr.add(allSemester.get(i).getSemester());
                }

                ArrayAdapter<String> adapterSmstr = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, smstr);
                adapterSmstr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spSemester.setAdapter(adapterSmstr);

                spSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        for(int i = 0; i <= 1; i++){
                            if(position == 0){
                                id_semester = null;
                            }else if(position == i+1){
                                id_semester = allSemester.get(i).getId_semester();
                            }
                        }
                        getMapel(id_kelas,id_semester);
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

    public void smstXI(){
        Call<List<Semester>> call = RetrofitClient
                .getInstance()
                .baseAPI()
                .getSemester();

        call.enqueue(new Callback<List<Semester>>() {
            @Override
            public void onResponse(Call<List<Semester>> call, Response<List<Semester>> response) {
                final List<Semester> allSemester = response.body();
                final List<String> smstr = new ArrayList<>();
                smstr.add(0, "Pilih");

                for(int i = 0 ; i <= 3; i++){
                    smstr.add(allSemester.get(i).getSemester());
                }

                ArrayAdapter<String> adapterSmstr = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, smstr);
                adapterSmstr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spSemester.setAdapter(adapterSmstr);

                spSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        for(int i = 0; i <= 3; i++){
                            if(position == 0){
                                id_semester = null;
                            }else if(position == i+1){
                                id_semester = allSemester.get(i).getId_semester();
                            }
                        }
                        getMapel(id_kelas,id_semester);
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

    public void smstXII(){
        Call<List<Semester>> call = RetrofitClient
                .getInstance()
                .baseAPI()
                .getSemester();

        call.enqueue(new Callback<List<Semester>>() {
            @Override
            public void onResponse(Call<List<Semester>> call, Response<List<Semester>> response) {
                final List<Semester> allSemester = response.body();
                final List<String> smstr = new ArrayList<>();
                smstr.add(0, "Pilih");

                for(int i = 0 ; i <= 5; i++){
                    smstr.add(allSemester.get(i).getSemester());
                }

                ArrayAdapter<String> adapterSmstr = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, smstr);
                adapterSmstr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spSemester.setAdapter(adapterSmstr);

                spSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        for(int i = 0; i <= 5; i++){
                            if(position == 0){
                                id_semester = null;
                            }else if(position == i+1){
                                id_semester = allSemester.get(i).getId_semester();
                            }
                        }
                        getMapel(id_kelas,id_semester);
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
                        getMapel(id_kelas,id_semester);
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

    public void getMapel(final String id_kelas, final String id_semester){
        if(id_semester == null){
            if(mapelSiswaList != null){
                mapelSiswaList.clear();
                adapterMapelSiswa.notifyDataSetChanged();
                tvPilihSemester.setVisibility(View.VISIBLE);
                tvKosong.setVisibility(View.INVISIBLE);
            }
            tvPilihSemester.setVisibility(View.VISIBLE);
            tvKosong.setVisibility(View.INVISIBLE);
        }else{
            if(mapelSiswaList != null){
                mapelSiswaList.clear();
                adapterMapelSiswa.notifyDataSetChanged();
            }

            tvPilihSemester.setVisibility(View.INVISIBLE);
            tvKosong.setVisibility(View.INVISIBLE);
            pb.setVisibility(View.VISIBLE);

            Runnable rn = new Runnable() {
                @Override
                public void run() {

                    Call<List<MapelSiswa>> call = RetrofitClient
                            .getInstance()
                            .baseAPI()
                            .getMapelSiswa(id_kelas, id_semester);

                    call.enqueue(new Callback<List<MapelSiswa>>() {
                        @Override
                        public void onResponse(Call<List<MapelSiswa>> call, Response<List<MapelSiswa>> response) {
                            pb.setVisibility(View.GONE);
                            if(response.body().size() == 0){
                                if(mapelSiswaList != null){
                                    mapelSiswaList.clear();
                                    adapterMapelSiswa.notifyDataSetChanged();
                                    tvKosong.setVisibility(View.VISIBLE);
                                }
                                tvKosong.setVisibility(View.VISIBLE);
                            }else{
                                mapelSiswaList = response.body();
                                adapterMapelSiswa = new AdapterMapelSiswa(nis, mapelSiswaList, mContext);
                                recyclerView.setAdapter(adapterMapelSiswa);
                                adapterMapelSiswa.notifyDataSetChanged();
                                tvKosong.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<MapelSiswa>> call, Throwable t) {

                        }
                    });
                }
            };

            Handler handler = new Handler();
            handler.postDelayed(rn, 2000);

        }
    }
}
