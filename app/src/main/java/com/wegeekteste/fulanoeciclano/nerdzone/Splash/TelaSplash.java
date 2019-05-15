package com.wegeekteste.fulanoeciclano.nerdzone.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.wegeekteste.fulanoeciclano.nerdzone.Activits.MainActivity;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

public class TelaSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_splash);

        delaysplash();
    }
    public void delaysplash(){
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(TelaSplash.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }


    @Override
    protected void onRestart() {
        delaysplash();
        super.onRestart();
    }

}
