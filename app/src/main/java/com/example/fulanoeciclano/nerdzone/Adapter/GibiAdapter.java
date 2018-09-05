package com.example.fulanoeciclano.nerdzone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fulanoeciclano.nerdzone.Activits.DescricaoGibiActivity;
import com.example.fulanoeciclano.nerdzone.Model.Gibi;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

/**
 * Created by fulanoeciclano on 19/05/2018.
 */

public class GibiAdapter extends RecyclerView.Adapter<GibiAdapter.MyViewHolder> {

  private Context c;
  private List<Gibi> gibis;

    public GibiAdapter(List<Gibi> ListaGibi, Context context) {
        this.gibis =ListaGibi;
        this.c = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adaptergibi,parent,false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Gibi gibi  = gibis.get(position);
        holder.nome.setText(gibi.getNome());




        ControllerListener listener = new BaseControllerListener(){

            @Override
            public void onFinalImageSet(String id, @Nullable Object imageInfo, @Nullable Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                Log.i("log","onFinalImageSet");

            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
                Log.i("log","onFailure");
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable Object imageInfo) {
                super.onIntermediateImageSet(id, imageInfo);
                Log.i("log","onIntermediateImageSet");
            }

            @Override
            public void onIntermediateImageFailed(String id, Throwable throwable) {
                super.onIntermediateImageFailed(id, throwable);
                Log.i("log","onIntermediateImageFailed");
            }

            @Override
            public void onRelease(String id) {
                super.onRelease(id);
                Log.i("log","onRelease");
            }

            @Override
            public void onSubmit(String id, Object callerContext) {
                super.onSubmit(id, callerContext);
                Log.i("log","onSubmit");
            }
        };


        if(gibi.getIconegibi() !=null){

            Uri uri = Uri.parse(gibi.getIconegibi());
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setProgressiveRenderingEnabled(true)
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setControllerListener(listener)
                    .setTapToRetryEnabled(true)
                    .setImageRequest(request)
                    .setOldController(holder.foto.getController())
                    .build();
            /*mSimpleDraweeView.setController(controller);
            DraweeController controllerOne = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setAutoPlayAnimations(true)
                    .build();
*/
            holder.foto.setController(controller);
//            holder.foto.getHierarchy().setRetryImage(R.drawable.emoji_google_1f3ca_1f3fb);



       }else{
           holder.foto.setImageResource(R.drawable.carregando);
        }
        holder.gibiLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent it = new Intent(c, DescricaoGibiActivity.class);
               it.putExtra("imagem_url",gibis.get(position).getIconegibi());
               it.putExtra("nome",gibis.get(position).getNome());
               it.putExtra("descricao",gibis.get(position).getDescricao());
               it.putExtra("saga",gibis.get(position).getSaga());
               it.putExtra("url",gibis.get(position).getUrl());
               it.putExtra("icone",gibis.get(position).getIconegibi());
               c.startActivity(it);
            }
        });



    }

    @Override
    public int getItemCount() {
        return gibis.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{

        SimpleDraweeView foto;
        TextView nome;
        LinearLayout gibiLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
//            foto.getHierarchy().setProgressBarImage(new ProgressBarDrawable());
            foto = (SimpleDraweeView)itemView.findViewById(R.id.iconegibi);
            nome = itemView.findViewById(R.id.nomegibi);
            gibiLayout=itemView.findViewById(R.id.layoutgibi);
        }
    }
}
