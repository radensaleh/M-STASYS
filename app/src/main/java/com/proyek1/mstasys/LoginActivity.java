package com.proyek1.mstasys;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.proyek1.mstasys.entity.Guru;
import com.proyek1.mstasys.entity.Siswa;
import com.proyek1.mstasys.guru.InfoGuruFragment;
import com.proyek1.mstasys.guru.MainGuruActivity;
import com.proyek1.mstasys.sqlLite.SlideDAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private Context mContext;
    private Dialog alertDialog;
    private Button btnYa, btnTidak;
    private Spinner spStatus;
    private EditText etId, etPassword;
    private int status = 0;
    private ProgressDialog pd;
    private SlideDAO slide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;
        alertDialog = new Dialog(mContext);

        slide = new SlideDAO(mContext);
        slide.ubahSlide();

        pd = new ProgressDialog(mContext);

        etId = findViewById(R.id.etId);
        etPassword = findViewById(R.id.etPassword);

        spStatus = findViewById(R.id.spStatus);

        List<String> actors = new ArrayList<>();
        actors.add(0,"-- Status --");
        actors.add(1,"Siswa");
        actors.add(2,"Guru");
//        actors.add(3,"Walikelas");


        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, actors);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spStatus.setAdapter(spinnerAdapter);

        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    status = 0;
                }else if(position == 1){
                    status = 1;
                }else if(position == 2){
                    status = 2;
                }else if(position == 3){
                    status = 3;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id   = etId.getText().toString();
                String pass = etPassword.getText().toString();

                if(id.isEmpty()){
                    etId.setError("NIP/NIS Kosong");
                }else if(pass.isEmpty()){
                    etPassword.setError("Password Kosong");
                }else if(status == 0){
                    new AlertDialog.Builder(mContext)
                            .setIcon(R.drawable.warning)
                            .setTitle("Peringatan")
                            .setMessage("Harap Memilih Status")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }else{
                    if(status == 1){
                        loginSiswa();
                    }else if(status == 2){
                        loginGuru();
                    }else if(status == 3){

                    }
                }
            }
        });

    }

    public void loginGuru(){
        pd.setIcon(R.drawable.login);
        pd.setTitle("Masuk");
        pd.setMessage("Harap Menunggu. . .");
        pd.setCancelable(false);
        pd.show();

        Runnable rn = new Runnable() {
            @Override
            public void run() {
                String nip = etId.getText().toString();
                String pas = etPassword.getText().toString();

                Guru guru = Guru.getInstance();
                guru.loginGuru(nip, pas, pd, mContext);
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(rn, 3000);

    }

    public void loginSiswa(){
        pd.setIcon(R.drawable.login);
        pd.setTitle("Masuk");
        pd.setMessage("Harap Menunggu. . .");
        pd.setCancelable(false);
        pd.show();

        Runnable rn = new Runnable() {
            @Override
            public void run() {
                String nis = etId.getText().toString();
                String pas = etPassword.getText().toString();

                Siswa siswa = Siswa.getInstance();
                siswa.loginSiswa(nis, pas, pd, mContext);
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(rn, 3000);
    }

    @Override
    public void onBackPressed() {
        alertDialog.setContentView(R.layout.alert_exit);
        btnTidak = alertDialog.findViewById(R.id.btnTidak);
        btnYa    = alertDialog.findViewById(R.id.btnYa);

        btnYa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnTidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}
