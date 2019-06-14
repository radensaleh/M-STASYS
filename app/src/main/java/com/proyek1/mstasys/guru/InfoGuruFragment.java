package com.proyek1.mstasys.guru;

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
import com.proyek1.mstasys.entity.Guru;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InfoGuruFragment extends Fragment {

    private Context mContext;
    private TextView tvNIP, tvNama, tvWalikelas, tvJK, tvAgama, tvTtl, tvAlamat;
    private ProgressDialog pd;
    private String nip;
    private ImageView imgProfile;

    private Guru guru = Guru.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info_guru, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();
        pd       = new ProgressDialog(mContext);
        tvNIP    = view.findViewById(R.id.tvNIP);
        tvNama   = view.findViewById(R.id.tvNama);
        tvJK     = view.findViewById(R.id.tvJK);
        tvAgama  = view.findViewById(R.id.tvAgama);
        tvAlamat = view.findViewById(R.id.tvAlamat);
        tvTtl    = view.findViewById(R.id.tvTtl);
        tvWalikelas = view.findViewById(R.id.tvWalikelas);
        imgProfile  = view.findViewById(R.id.imgProfile);

        nip = guru.getNip();

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
                Call<Guru> res = RetrofitClient
                        .getInstance()
                        .baseAPI()
                        .dataGuru(nip);

                res.enqueue(new Callback<Guru>() {
                    @Override
                    public void onResponse(Call<Guru> call, Response<Guru> response) {
                        pd.dismiss();

                        tvNIP.setVisibility(View.VISIBLE);
                        tvNama.setVisibility(View.VISIBLE);
                        tvJK.setVisibility(View.VISIBLE);
                        tvAgama.setVisibility(View.VISIBLE);
                        tvAlamat.setVisibility(View.VISIBLE);
                        tvWalikelas.setVisibility(View.VISIBLE);
                        tvTtl.setVisibility(View.VISIBLE);
                        imgProfile.setVisibility(View.VISIBLE);

                        tvNIP.setText("NIP. " + response.body().getNip());
                        tvNama.setText(response.body().getNama());
                        tvJK.setText(response.body().getJenis_kelamin());
                        tvAgama.setText(response.body().getAgama());
                        tvAlamat.setText(response.body().getAlamat());
                        tvWalikelas.setText(response.body().getWalikelas());
                        tvTtl.setText(response.body().getTempat_lahir() + ", " + response.body().getTgl_lahir());

                        RequestOptions ro = new RequestOptions()
                                .placeholder(R.drawable.noimage).dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true);

                        Glide.with(mContext)
                                .load(BuildConfig.IMG_GURU+response.body().getFoto())
                                .apply(ro)
                                .into(imgProfile);
                    }

                    @Override
                    public void onFailure(Call<Guru> call, Throwable t) {
                        Log.e("ERRRRRR : ", t.getMessage());
                    }
                });
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(rn, 500);

    }

}
