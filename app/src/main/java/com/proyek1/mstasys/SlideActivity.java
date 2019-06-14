package com.proyek1.mstasys;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.proyek1.mstasys.adapter.SlideAdapter;

public class SlideActivity extends AppCompatActivity {

    private ViewPager mSlideVP;
    private LinearLayout mDotLayout;
    private SlideAdapter slideAdapter;
    private TextView[] mDots;

    private Button btnSelesai;
    private Button btnLewati;
    private int mCurrentPage;

    private Context mContext;
    private Dialog alertDialog;
    private Button btnYa, btnTidak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);

        mContext = this;
        alertDialog = new Dialog(mContext);

        mSlideVP   = findViewById(R.id.slideVP);
        mDotLayout = findViewById(R.id.dotsLayout);
        btnLewati  = findViewById(R.id.btnLewati);
        btnSelesai = findViewById(R.id.btnSelesai);

        btnLewati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        slideAdapter = new SlideAdapter(mContext);

        mSlideVP.setAdapter(slideAdapter);

        addDots(0);

        mSlideVP.addOnPageChangeListener(viewListener);

    }

    public void addDots(int position){

        mDots = new TextView[5];
        mDotLayout.removeAllViews();

        for(int i = 0; i < mDots.length; i++){

            mDots[i] = new TextView(mContext);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(45);
            mDots[i].setTextColor(getResources().getColor(R.color.colorGray));

            mDotLayout.addView(mDots[i]);
        }

        if(mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);

            mCurrentPage = position;

            if(position == 0) {
                btnSelesai.setVisibility(View.INVISIBLE);
                btnLewati.setVisibility(View.VISIBLE);
                btnLewati.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(mContext, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
            }else if(position == mDots.length -1){

                btnSelesai.setVisibility(View.VISIBLE);
                btnLewati.setVisibility(View.INVISIBLE);
                btnSelesai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(mContext, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                });

            }else{
                btnSelesai.setVisibility(View.INVISIBLE);
                btnLewati.setVisibility(View.VISIBLE);
                btnLewati.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(mContext, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

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
