package com.app.technofunda.ethiqs;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.technofunda.R;

public class Splashscreen extends Activity {

    MediaPlayer mp;
    LinearLayout liner1_bc;
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    Thread splashTread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        StartAnimations();
        // imgsplash=(ImageView)findViewById(R.id.splash);
      //  liner1_bc=(LinearLayout)findViewById(R.id.liner1_bc);
//        liner1_bc.setAlpha((float) 0.7);
    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.zoom);
        anim.reset();
        Animation anim2 = AnimationUtils.loadAnimation(this,R.anim.zoom);
        anim2.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);


        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (waited <5000) {
                        sleep(100);
                        waited += 100;
                    }
//                    mp.stop();
                    Intent intent = new Intent(Splashscreen.this,
                            LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    Splashscreen.this.finish();

                } catch (InterruptedException e) {
                } finally {
                    Splashscreen.this.finish();
                }
            }
        };
        splashTread.start();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen for landscape and portrait
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

}

