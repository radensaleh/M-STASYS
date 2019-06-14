package com.proyek1.mstasys.guru;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.proyek1.mstasys.LoginActivity;
import com.proyek1.mstasys.R;
import com.proyek1.mstasys.sqlLite.GuruDAO;

public class MainGuruActivity extends AppCompatActivity {

    private InfoGuruFragment infoGuruFragment;
    private NilaiSiswaFragment nilaiSiswaFragment;
    private UploadSoalFragment uploadSoalFragment;
    private DaftarAmpuFragment daftarAmpuFragment;

    private int navTab = 1;
    private boolean bar;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private int finish;

    private Context mContext;
    private Dialog alertDialog;
    private Button btnYa, btnTidak;

    private GuruDAO guruDAO;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_info:
                    setTitle(R.string.info);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                    getSupportActionBar().setIcon(R.drawable.ic_account_circle_white_24dp);
                    navTab = 1;
                    //bar = false;
                    bar = true;
                    break;
                case R.id.navigation_ampu:
                    setTitle(R.string.ampu);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                    getSupportActionBar().setIcon(R.drawable.ic_featured_play_list_white_24dp);
                    navTab = 2;
                    bar = true;
                    break;
                case R.id.navigation_nilai:
                    setTitle(R.string.nilai);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                    getSupportActionBar().setIcon(R.drawable.ic_assignment_turned_in_white_24dp);
                    navTab = 3;
                    bar = true;
                    break;
                case R.id.navigation_upload:
                    setTitle(R.string.soal);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                    getSupportActionBar().setIcon(R.drawable.ic_cloud_upload_white_24dp);
                    navTab = 4;
                    bar = true;
                    break;
            }
            loadFragment();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_guru);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_account_circle_white_24dp);

        mContext = this;
        alertDialog = new Dialog(mContext);

        guruDAO = new GuruDAO(mContext);

        bar = true;
        finish = 0;

        infoGuruFragment   = new InfoGuruFragment();
        daftarAmpuFragment = new DaftarAmpuFragment();
        nilaiSiswaFragment = new NilaiSiswaFragment();
        uploadSoalFragment = new UploadSoalFragment();
        mFragmentManager   = getSupportFragmentManager();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState != null) {
            Fragment f = getSupportFragmentManager().getFragment(savedInstanceState, "fragment");
            if (f instanceof InfoGuruFragment) {
                infoGuruFragment = (InfoGuruFragment) getSupportFragmentManager().getFragment(savedInstanceState, "fragment");
                navTab = 1;
            }else if(f instanceof  DaftarAmpuFragment){
                daftarAmpuFragment = (DaftarAmpuFragment) getSupportFragmentManager().getFragment(savedInstanceState, "fragment");
            }else if(f instanceof  NilaiSiswaFragment){
                nilaiSiswaFragment = (NilaiSiswaFragment) getSupportFragmentManager().getFragment(savedInstanceState, "fragment");
            }else if(f instanceof  UploadSoalFragment){
                uploadSoalFragment = (UploadSoalFragment) getSupportFragmentManager().getFragment(savedInstanceState, "fragment");
            }
        }

        loadFragment();

    }

    private void loadFragment(){
        mFragmentTransaction = mFragmentManager.beginTransaction();
        switch (navTab) {
            case 1:
                mFragmentTransaction.replace(R.id.fl_container, infoGuruFragment, InfoGuruFragment.class.getSimpleName());
                break;
            case 2:
                mFragmentTransaction.replace(R.id.fl_container, daftarAmpuFragment, DaftarAmpuFragment.class.getSimpleName());
                break;
            case 3:
                mFragmentTransaction.replace(R.id.fl_container, nilaiSiswaFragment, NilaiSiswaFragment.class.getSimpleName());
                break;
            case 4:
                mFragmentTransaction.replace(R.id.fl_container, uploadSoalFragment, UploadSoalFragment.class.getSimpleName());
                break;
        }
        mFragmentTransaction.commit();
    }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_bar, menu);

            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (navTab) {
                case 1:
//                    if (item.getItemId() == R.id.action_edit) {
//                        Toast.makeText(mContext, "Edit", Toast.LENGTH_LONG).show();
//                    } else
                        if (item.getItemId() == R.id.action_logout) {
                        alert();
                    }
                    break;
                case 2:
                    if (item.getItemId() == R.id.action_logout) {
                        alert();
                    }
                    break;
                case 3:
                    if (item.getItemId() == R.id.action_logout) {
                        alert();
                    }
                    break;
                case 4:
                    if (item.getItemId() == R.id.action_logout) {
                        alert();
                    }
                    break;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public boolean onPrepareOptionsMenu(Menu menu) {
            MenuItem item = menu.findItem(R.id.action_edit);

            if(bar){
                item.setVisible(false);
            }else{
                item.setVisible(true);
            }

            return true;
        }

        @Override
        protected void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            switch (navTab) {
                case 1:
                    getSupportFragmentManager().putFragment(outState, "fragment", infoGuruFragment);
                    break;
                case 2:
                    getSupportFragmentManager().putFragment(outState, "fragment", daftarAmpuFragment);
                    break;
                case 3:
                    getSupportFragmentManager().putFragment(outState, "fragment", nilaiSiswaFragment);
                    break;
                case 4:
                    getSupportFragmentManager().putFragment(outState, "fragment", uploadSoalFragment);
                    break;
            }
        }

        @Override
        public void onBackPressed() {
            if(finish == 0){
                Toast.makeText(mContext, "Ketuk 1 kali lagi untuk keluar", Toast.LENGTH_SHORT).show();
                finish++;
            }else{
                finish();
            }
        }

        public void alert(){
        alertDialog.setContentView(R.layout.alert_logout);
        btnTidak = alertDialog.findViewById(R.id.btnTidak);
        btnYa    = alertDialog.findViewById(R.id.btnYa);

        btnYa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guruDAO.deleteUser();
                Intent i = new Intent(mContext, LoginActivity.class);
                startActivity(i);
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

    public void exit(){
        alertDialog.setContentView(R.layout.alert_logout);
        btnTidak = alertDialog.findViewById(R.id.btnTidak);
        btnYa    = alertDialog.findViewById(R.id.btnYa);

        btnYa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
