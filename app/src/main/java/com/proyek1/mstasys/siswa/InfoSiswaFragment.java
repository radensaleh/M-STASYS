package com.proyek1.mstasys.siswa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.proyek1.mstasys.BuildConfig;
import com.proyek1.mstasys.R;
import com.proyek1.mstasys.api.RetrofitClient;
import com.proyek1.mstasys.entity.Siswa;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoSiswaFragment extends Fragment {

    private Context mContext;
    private TextView tvNISN, tvNIS, tvIjasah, tvUN, tvKelas, tvJK, tvNama, tvTtl, tvAgama, tvAlamat;
    private ImageView imgProfile;
    private ProgressDialog pd;
    private String nis;
    private Siswa siswa = Siswa.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info_siswa, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();

        pd = new ProgressDialog(mContext);

        tvNISN   = view.findViewById(R.id.tvNISN);
        tvNIS    = view.findViewById(R.id.tvNIS);
        tvIjasah = view.findViewById(R.id.tvIjasah);
        tvUN     = view.findViewById(R.id.tvUN);
        tvKelas  = view.findViewById(R.id.tvKelas);
        tvJK     = view.findViewById(R.id.tvJK);
        tvNama   = view.findViewById(R.id.tvNama);
        tvTtl    = view.findViewById(R.id.tvTtl);
        tvAgama  = view.findViewById(R.id.tvAgama);
        tvAlamat = view.findViewById(R.id.tvAlamat);
        imgProfile = view.findViewById(R.id.imgProfile);

        nis = siswa.getNis();

        load_info();

    }

    public void load_info(){
        pd.setIcon(R.drawable.loading);
        pd.setTitle("Memuat Data");
        pd.setMessage("Harap Menunggu. . .");
        pd.setCancelable(false);
        pd.show();

        Runnable rn = new Runnable() {
            @Override
            public void run() {
                Call<Siswa> res = RetrofitClient
                        .getInstance()
                        .baseAPI()
                        .dataSiswa(nis);

                res.enqueue(new Callback<Siswa>() {
                    @Override
                    public void onResponse(Call<Siswa> call, Response<Siswa> response) {
                        tvNISN.setVisibility(View.VISIBLE);
                        tvNIS.setVisibility(View.VISIBLE);
                        tvIjasah.setVisibility(View.VISIBLE);
                        tvUN.setVisibility(View.VISIBLE);
                        tvKelas.setVisibility(View.VISIBLE);
                        tvJK.setVisibility(View.VISIBLE);
                        tvNama.setVisibility(View.VISIBLE);
                        tvTtl.setVisibility(View.VISIBLE);
                        tvAgama.setVisibility(View.VISIBLE);
                        tvAlamat.setVisibility(View.VISIBLE);
                        imgProfile.setVisibility(View.VISIBLE);

                        tvNISN.setText("NISN. " + response.body().getNisn());
                        tvNIS.setText(response.body().getNis());
                        tvIjasah.setText("No Ijasah. " + response.body().getNo_ijasah_smp());
                        tvUN.setText("No UN. " + response.body().getNo_un());
                        tvKelas.setText(response.body().getTingkat()+" "+response.body().getJurusan()+" "+response.body().getRombel());
                        tvJK.setText(response.body().getJenis_kelamin());
                        tvNama.setText(response.body().getNama());
                        tvTtl.setText(response.body().getTempat_lahir() + ", " + response.body().getTgl_lahir());
                        tvAgama.setText(response.body().getAgama());
                        tvAlamat.setText(response.body().getAlamat());

                        RequestOptions ro = new RequestOptions()
                                .placeholder(R.drawable.noimage).dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true);

                        Glide.with(mContext)
                                .load(BuildConfig.IMG_SISWA+response.body().getFoto())
                                .apply(ro)
                                .into(imgProfile);

                        pd.dismiss();

                    }

                    @Override
                    public void onFailure(Call<Siswa> call, Throwable t) {
                        pd.dismiss();
                        Log.e("ERRRRRR : ", t.getMessage());
                    }
                });
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(rn, 500);
    }
}
