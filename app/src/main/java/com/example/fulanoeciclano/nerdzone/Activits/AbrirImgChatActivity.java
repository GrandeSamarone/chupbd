package com.example.fulanoeciclano.nerdzone.Activits;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;

import me.relex.photodraweeview.PhotoDraweeView;

public class AbrirImgChatActivity extends AppCompatActivity {

    private PhotoDraweeView imgdochat;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abrir_img_chat);

        toolbar = findViewById(R.id.toolbarAbrirImg);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

          imgdochat= findViewById(R.id.imgdoChat);
        if(getIntent().hasExtra("imgchat")&& getIntent().hasExtra("nomeUserChat")){
            String imageUrl = getIntent().getStringExtra("imgchat");
            String nomeUsuario = getIntent().getStringExtra("nomeUserChat");
            Log.i("Pesquisa4",nomeUsuario);

            toolbar.setTitle(nomeUsuario);

            Uri uri = Uri.parse(imageUrl);

            PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
            controller.setUri(uri);
            controller.setOldController(imgdochat.getController());
            controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                    super.onFinalImageSet(id, imageInfo, animatable);
                    if (imageInfo == null || imgdochat == null) {
                        return;
                    }
                    imgdochat.update(imageInfo.getWidth(), imageInfo.getHeight());
                }
            });
            imgdochat.setController(controller.build());
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    //Botao Voltar
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                finish();
            default:break;
        }

        return true;
    }

}
