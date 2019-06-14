package com.proyek1.mstasys.siswa;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.proyek1.mstasys.guru.InfoGuruFragment;
import com.proyek1.mstasys.guru.UploadSoalFragment;
import com.proyek1.mstasys.sqlLite.SiswaDAO;

public class MainSiswaActivity extends AppCompatActivity {

    private InfoSiswaFragment infoSiswaFragment;
    private NilaiSiswaFragment nilaiSiswaFragment;
    private KerjakanSoalFragment kerjakanSoalFragment;

    private int navTab = 1;
    private boolean bar;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private int finish;

    private Context mContext;
    private Dialog alertDialog;
    private Button btnYa, btnTidak;

    private SiswaDAO siswaDAO;

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
                case R.id.navigation_nilai:
                    setTitle(R.string.nilaiSiswa);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                    getSupportActionBar().setIcon(R.drawable.ic_assignment_turned_in_white_24dp);
                    navTab = 2;
                    bar = true;
                    break;
                case R.id.navigation_kerjakan:
                    setTitle(R.string.kerjakanSoal);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                    getSupportActionBar().setIcon(R.drawable.ic_create_white_24dp);
                    navTab = 3;
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
        setContentView(R.layout.activity_main_siswa);
        setTitle(R.string.info);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_account_circle_white_24dp);

        mContext = this;
        alertDialog = new Dialog(mContext);

        siswaDAO = new SiswaDAO(mContext);

        bar = true;
        finish = 0;

        infoSiswaFragment    = new InfoSiswaFragment();
        nilaiSiswaFragment   = new NilaiSiswaFragment();
        kerjakanSoalFragment = new KerjakanSoalFragment();
        mFragmentManager   = getSupportFragmentManager();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState != null) {
            Fragment f = getSupportFragmentManager().getFragment(savedInstanceState, "fragment");
            if (f instanceof InfoSiswaFragment) {
                infoSiswaFragment = (InfoSiswaFragment) getSupportFragmentManager().getFragment(savedInstanceState, "fragment");
                navTab = 1;
            }else if(f instanceof NilaiSiswaFragment){
                nilaiSiswaFragment = (NilaiSiswaFragment) getSupportFragmentManager().getFragment(savedInstanceState, "fragment");
            }else if(f instanceof KerjakanSoalFragment){
                kerjakanSoalFragment = (KerjakanSoalFragment) getSupportFragmentManager().getFragment(savedInstanceState, "fragment");
            }
        }

        loadFragment();


    }

    public void loadFragment(){
        mFragmentTransaction = mFragmentManager.beginTransaction();
        switch (navTab) {
            case 1:
                mFragmentTransaction.replace(R.id.fl_container, infoSiswaFragment, InfoSiswaFragment.class.getSimpleName());
                break;
            case 2:
                mFragmentTransaction.replace(R.id.fl_container, nilaiSiswaFragment, NilaiSiswaFragment.class.getSimpleName());
                break;
            case 3:
                mFragmentTransaction.replace(R.id.fl_container, kerjakanSoalFragment, KerjakanSoalFragment.class.getSimpleName());
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
//                if (item.getItemId() == R.id.action_edit) {
//                    Toast.makeText(mContext, "Edit", Toast.LENGTH_LONG).show();
//                } else
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
                getSupportFragmentManager().putFragment(outState, "fragment", infoSiswaFragment);
                break;
            case 2:
                getSupportFragmentManager().putFragment(outState, "fragment", nilaiSiswaFragment);
                break;
            case 3:
                getSupportFragmentManager().putFragment(outState, "fragment", kerjakanSoalFragment);
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
                siswaDAO.deleteUser();
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
