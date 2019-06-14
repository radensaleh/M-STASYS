package com.proyek1.mstasys;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.proyek1.mstasys.entity.Guru;
import com.proyek1.mstasys.entity.Siswa;
import com.proyek1.mstasys.sqlLite.GuruDAO;
import com.proyek1.mstasys.sqlLite.SiswaDAO;
import com.proyek1.mstasys.sqlLite.SlideDAO;

public class MainActivity extends AppCompatActivity {

    SlideDAO slide;
    Context mContext;
    SiswaDAO siswa;
    GuruDAO guru;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Runnable rn;

        mContext = this;
        slide = new SlideDAO(mContext);
        siswa = new SiswaDAO(mContext);
        guru  = new GuruDAO(mContext);

        if(slide.getSlide() == 0){
            rn = new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(MainActivity.this, SlideActivity.class);
                    startActivity(i);
                    finish();
                }
            };
        }else{
            rn = new Runnable() {
                @Override
                public void run() {
                    if(siswa.getUser() != null){
                        Siswa siswaIns = Siswa.getInstance();
                        siswaIns.loginSiswa(siswa.getUser().get(0), siswa.getUser().get(1), mContext);
                    }else if(guru.getUser() != null){
                        Guru guruIns = Guru.getInstance();
                        guruIns.loginGuru(guru.getUser().get(0), guru.getUser().get(1), mContext);
                    }else{
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            };
        }

        Handler h = new Handler();
        h.postDelayed(rn, 2000);


    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
