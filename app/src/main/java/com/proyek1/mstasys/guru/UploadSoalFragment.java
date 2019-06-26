package com.proyek1.mstasys.guru;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.proyek1.mstasys.R;
import com.proyek1.mstasys.adapter.AdapterSoal;
import com.proyek1.mstasys.api.RetrofitClient;
import com.proyek1.mstasys.entity.Guru;
import com.proyek1.mstasys.response.Response;
import com.proyek1.mstasys.response.Soal;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class UploadSoalFragment extends Fragment {

    private Button btnPilihFile, btnUnggahFile;
    private TextView tvNamaFile, tvKosong;
    private Context mContext;
    private int fileEmpty = 0;

    public static final int PERMISSIONS_REQUEST_CODE = 0;
    public static final int FILE_PICKER_REQUEST_CODE = 1;
    private String path;

    private ProgressDialog pd;
    private ProgressBar pb;
    private String nip;
    private int status = 1;

    private List<Soal> soalList;
    private AdapterSoal adapterSoal;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private Guru guru = Guru.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upload_soal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();
        fileEmpty = 0;

        pd = new ProgressDialog(mContext);
        pb = view.findViewById(R.id.pb);

        Sprite spkit = new CubeGrid();
        pb.setIndeterminateDrawable(spkit);

        nip = guru.getNip();

        btnPilihFile  = view.findViewById(R.id.btnPilihFile);
        btnUnggahFile = view.findViewById(R.id.btnUnggahFile);
        tvNamaFile    = view.findViewById(R.id.tvNamaFile);
        recyclerView  = view.findViewById(R.id.recyclerSoal);
        tvKosong      = view.findViewById(R.id.tvKosong);

        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        getSoal();

        btnPilihFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionsAndOpenFilePicker();
            }
        });

        btnUnggahFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fileEmpty == 0){
                    new AlertDialog.Builder(mContext)
                            .setIcon(R.drawable.warning)
                            .setTitle("Peringatan")
                            .setMessage("Harap pilih File yang akan diunggah")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }else{
                    File file = new File(path);
                    RequestBody mFile = RequestBody.create(MediaType.parse("excel"), file);
                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
                   // RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

                    unggahSoal(fileToUpload);
                }
            }
        });

    }

    private void checkPermissionsAndOpenFilePicker() {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
                Toast.makeText(getContext(), "Izinkan membaca penyimpanan eksternal", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, PERMISSIONS_REQUEST_CODE);
            }
        } else {
            openFilePicker();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openFilePicker();
                } else {
                    Toast.makeText(getContext(), "Izinkan membaca penyimpanan eksternal", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void openFilePicker(){
        new MaterialFilePicker()
                .withSupportFragment(this)
                .withRequestCode(FILE_PICKER_REQUEST_CODE)
                .withHiddenFiles(true)
                .withFilter(Pattern.compile(".*\\.(xls|xlsx)$"))
                .withTitle("Pilih Excel File")
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);

            if (path != null) {
//                Toast.makeText(getContext(), "Picked file in fragment: " + path, Toast.LENGTH_LONG).show();
                tvNamaFile.setText("# Lokasi file : " + path);
                fileEmpty = 1;

            }else{
                tvNamaFile.setText("# Gagal ambil file");
            }
        }
    }

    public void unggahSoal(final MultipartBody.Part fileToUpload){
        pd.setIcon(R.drawable.loading);
        pd.setTitle("Unggah File");
        pd.setMessage("Harap Menunggu. . .");
        pd.setCancelable(false);
        pd.show();

        Runnable rn = new Runnable() {
            @Override
            public void run() {
                Call<Response> call = RetrofitClient
                        .getInstance()
                        .baseAPI()
                        .unggahSoal(fileToUpload);

                call.enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                        Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                        if(response.body() == null){
                            pd.dismiss();
                            new AlertDialog.Builder(mContext)
                                    .setIcon(R.drawable.warning)
                                    .setTitle("Peringatan")
                                    .setMessage("Isi file tidak sesuai ketentuan")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();
                        }else{
                            String error   = response.body().getError();
                            if(error.equals("0")){
                                pd.dismiss();
                                new AlertDialog.Builder(mContext)
                                        .setIcon(R.drawable.success)
                                        .setTitle("Berhasil")
                                        .setMessage("File berhasil diunggah")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                fileEmpty = 0;
                                                tvNamaFile.setText("# Lokasi File : belum ada file dipilih");
                                                if(soalList != null){
                                                    soalList.clear();
                                                    adapterSoal.notifyDataSetChanged();
                                                }
                                                getSoal();
                                            }
                                        }).show();
                            }else{
                                pd.dismiss();
                                new AlertDialog.Builder(mContext)
                                        .setIcon(R.drawable.warning)
                                        .setTitle("Peringatan")
                                        .setMessage("Format file salah")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {

                    }
                });
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(rn, 2000);

    }

    public void getSoal(){
        tvKosong.setVisibility(View.INVISIBLE);
        pb.setVisibility(View.VISIBLE);

        Runnable rn = new Runnable() {
            @Override
            public void run() {
                Call<List<Soal>> call = RetrofitClient
                        .getInstance()
                        .baseAPI()
                        .getSoal(nip);

                call.enqueue(new Callback<List<Soal>>() {
                    @Override
                    public void onResponse(Call<List<Soal>> call, retrofit2.Response<List<Soal>> response) {
                        if(response.body().size() == 0){
                            pb.setVisibility(View.GONE);
                            tvKosong.setVisibility(View.VISIBLE);
                        }else{
                            pb.setVisibility(View.GONE);
                            tvKosong.setVisibility(View.INVISIBLE);
                            soalList = response.body();
                            adapterSoal = new AdapterSoal(soalList, mContext, nip);
                            recyclerView.setAdapter(adapterSoal);
                            adapterSoal.notifyDataSetChanged();
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

    @Override
    public void onResume() {
        super.onResume();
        if(soalList != null && fileEmpty == 0){
            soalList.clear();
            adapterSoal.notifyDataSetChanged();
            getSoal();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        fileEmpty = 0;
        tvNamaFile.setText("# Lokasi File : belum ada file dipilih");
    }

}
