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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.proyek1.mstasys.R;
import com.proyek1.mstasys.adapter.AdapterSiswa;
import com.proyek1.mstasys.api.RetrofitClient;
import com.proyek1.mstasys.entity.Guru;
import com.proyek1.mstasys.entity.Siswa;
import com.proyek1.mstasys.response.Kelas;
import com.proyek1.mstasys.response.Mapel;
import com.proyek1.mstasys.response.Semester;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NilaiSiswaFragment extends Fragment {

    private Context mContext;
    private Spinner spMapel, spKelas, spSemester;
    private String nip;
    private String
            id_mapel = null,
            id_semester = null,
            id_kelas = null,
            nama_mapel = null,
            nama_kelas = null,
            semester   = null;
    private TextView tvKosong;
    private ProgressBar pb;

    private List<Siswa> siswaList;
    private AdapterSiswa adapterSiswa;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private Guru guru = Guru.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nilai_siswa, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();

        spMapel    = view.findViewById(R.id.spMapel);
        spKelas    = view.findViewById(R.id.spKelas);
        spSemester = view.findViewById(R.id.spSemester);
        recyclerView = view.findViewById(R.id.recyclerSiswa);

        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        tvKosong = view.findViewById(R.id.tvKosong);
        pb = view.findViewById(R.id.pb);

        Sprite spkit = new CubeGrid();
        pb.setIndeterminateDrawable(spkit);

        nip = guru.getNip();

        spinnerSet();

    }

    public void spinnerSet(){
        Call<List<Mapel>> call = RetrofitClient
                .getInstance()
                .baseAPI()
                .getMapelGuru(nip);

        call.enqueue(new Callback<List<Mapel>>() {
            @Override
            public void onResponse(Call<List<Mapel>> call, Response<List<Mapel>> response) {
                final List<Mapel> allMapel = response.body();
                final List<String> mapel = new ArrayList<>();
                mapel.add(0, "Pilih");

                for(int i = 0; i < allMapel.size(); i++){
                    mapel.add(allMapel.get(i).getNama_mapel());
                }

                ArrayAdapter<String> adapterMapel = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, mapel);
                adapterMapel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spMapel.setAdapter(adapterMapel);

                spMapel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        for(int i = 0; i <= allMapel.size(); i++){
                            if(position == 0){
                                id_mapel = null;
                                id_semester = null;
                                if(siswaList != null){
                                    if(id_semester == null){
                                        siswaList.clear();
                                        adapterSiswa.notifyDataSetChanged();
                                    }
                                }
                                tvKosong.setVisibility(View.VISIBLE);
                            }else if(position == i+1){
                                id_mapel = allMapel.get(i).getId_mapel();
                                nama_mapel = allMapel.get(i).getNama_mapel();
                            }
                        }

                        if(id_mapel != null){
                            getKelas(id_mapel);
                        }else{
                            spSemester.setAdapter(null);
                            spKelas.setAdapter(null);
                            if(siswaList != null){
                                if(id_semester == null){
                                    siswaList.clear();
                                    adapterSiswa.notifyDataSetChanged();
                                }
                            }
                            tvKosong.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<Mapel>> call, Throwable t) {

            }
        });
    }

    public void getKelas(String id_mapel){
            Call<List<Kelas>> call = RetrofitClient
                    .getInstance()
                    .baseAPI()
                    .getKelasGuru(nip, id_mapel);

            call.enqueue(new Callback<List<Kelas>>() {
                @Override
                public void onResponse(Call<List<Kelas>> call, Response<List<Kelas>> response) {
                    final List<Kelas> allKelas = response.body();
                    final List<String> kelas = new ArrayList<>();
                    kelas.add(0,"Pilih");

                    for(int i = 0; i < allKelas.size(); i++){
                        kelas.add(allKelas.get(i).getTingkat()+ " " +allKelas.get(i).getJurusan()+ " " +allKelas.get(i).getRombel());
                    }

                    ArrayAdapter<String> adapterKelas = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, kelas);
                    adapterKelas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spKelas.setAdapter(adapterKelas);

                    spKelas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            for(int i = 0; i <= allKelas.size(); i++){
                                if(position == 0){
                                    id_kelas = null;
                                    id_semester = null;
                                    if(siswaList != null){
                                        if(id_semester == null){
                                            siswaList.clear();
                                            adapterSiswa.notifyDataSetChanged();
                                        }
                                    }
                                    tvKosong.setVisibility(View.VISIBLE);
                                }else if(position == i+1){
                                    id_kelas = allKelas.get(i).getId_kelas();
                                    nama_kelas = allKelas.get(i).getTingkat() + " " + allKelas.get(i).getJurusan() + " " + allKelas.get(i).getRombel();
                                }
                            }

                            if(id_kelas != null){
                                getSemester(id_kelas);
                            }else{
                                spSemester.setAdapter(null);
                                if(siswaList != null){
                                    if(id_semester == null){
                                        siswaList.clear();
                                        adapterSiswa.notifyDataSetChanged();
                                    }
                                }
                                tvKosong.setVisibility(View.VISIBLE);
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                @Override
                public void onFailure(Call<List<Kelas>> call, Throwable t) {

                }
            });
    }

    public void getSemester(final String id_kelas){
            Call<List<Semester>> call = RetrofitClient
                    .getInstance()
                    .baseAPI()
                    .getSemesterGuru(nip, id_kelas, id_mapel);

            call.enqueue(new Callback<List<Semester>>() {
                @Override
                public void onResponse(Call<List<Semester>> call, Response<List<Semester>> response) {
                    final List<Semester> allSemester = response.body();
                    final List<String> smstr = new ArrayList<>();
                    smstr.add(0, "Pilih");

                    for(int i = 0; i < allSemester.size(); i++){
                        smstr.add(allSemester.get(i).getThn_ajaran()+'-'+allSemester.get(i).getSemester());
                    }

                    ArrayAdapter<String> adapterSemester = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, smstr);
                    adapterSemester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spSemester.setAdapter(adapterSemester);

                    spSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            for(int i = 0; i <= allSemester.size(); i++){
                                if(position == 0){
                                    id_semester = null;
                                    if(siswaList != null){
                                        if(id_semester == null){
                                            siswaList.clear();
                                            adapterSiswa.notifyDataSetChanged();
                                        }
                                    }
                                }else if(position == i+1){
                                    id_semester = allSemester.get(i).getId_semester();
                                    semester = allSemester.get(i).getSemester();
                                }
                            }
                            if(id_semester != null){
                                if(siswaList != null){
                                    siswaList.clear();
                                    adapterSiswa.notifyDataSetChanged();
                                    tvKosong.setVisibility(View.INVISIBLE);
                                    getSiswa(nip, id_mapel, id_kelas, id_semester);
                                }
                                tvKosong.setVisibility(View.INVISIBLE);
                                getSiswa(nip, id_mapel, id_kelas, id_semester);
                            }else{
                                if(siswaList != null){
                                    if(id_semester == null){
                                        siswaList.clear();
                                        adapterSiswa.notifyDataSetChanged();
                                    }
                                }
                                tvKosong.setVisibility(View.VISIBLE);
                            }
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

    public void getSiswa(final String nip, final String id_mapel, final String id_kelas, final String id_semester){
        pb.setVisibility(View.VISIBLE);
        tvKosong.setVisibility(View.INVISIBLE);

        Runnable rn = new Runnable() {
            @Override
            public void run() {
                Call<List<Siswa>> call = RetrofitClient
                        .getInstance()
                        .baseAPI()
                        .getSiswa(nip, id_mapel, id_kelas, id_semester);

                call.enqueue(new Callback<List<Siswa>>() {
                    @Override
                    public void onResponse(Call<List<Siswa>> call, Response<List<Siswa>> response) {

                        if(response.body().size() == 0){
                            pb.setVisibility(View.GONE);
                            tvKosong.setVisibility(View.VISIBLE);
                        }else{
                            pb.setVisibility(View.GONE);
                            tvKosong.setVisibility(View.INVISIBLE);

                            siswaList = response.body();
                            adapterSiswa = new AdapterSiswa(siswaList, mContext, nip, id_mapel, nama_mapel, id_kelas, nama_kelas, id_semester, semester);
                            recyclerView.setAdapter(adapterSiswa);
                            adapterSiswa.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onFailure(Call<List<Siswa>> call, Throwable t) {

                    }
                });
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(rn, 2000);

    }
}
