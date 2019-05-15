package com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Adapter_votacao;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.CircleProgressDrawable;
import com.wegeekteste.fulanoeciclano.nerdzone.R;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.model_votacao.Categoria_empreendedora;

import java.util.ArrayList;
import java.util.List;

public class Adapter_empreendedora_fem extends RecyclerView.Adapter<Adapter_empreendedora_fem.MyViewHolder> {

    private ArrayList<Categoria_empreendedora> lista_digital;
    private Context context;

    public Adapter_empreendedora_fem(Context c, ArrayList<Categoria_empreendedora> categora){
        this.context=c;
        this.lista_digital=categora;
    }

    public List<Categoria_empreendedora> getcategoria(){
        return this.lista_digital;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_categoria_votacao,parent,false);

        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Categoria_empreendedora categoria_pessoa = lista_digital.get(position);

        if(categoria_pessoa.getNome()!=null){
            holder.titulo.setText(categoria_pessoa.getNome());
        }
        if(categoria_pessoa.getDescricao()!=null){
            holder.descricao.setText(categoria_pessoa.getDescricao());
        }

        List<String> urlFotos = categoria_pessoa.getFotos();
        if (urlFotos != null) {
            String stringcapa = urlFotos.get(0);
            if (stringcapa != null) {
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(stringcapa))
                        .setLocalThumbnailPreviewsEnabled(true)
                        .setProgressiveRenderingEnabled(true)
                        .build();

                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .build();
                holder.capa.setController(controller);
                RoundingParams roundingParams = RoundingParams.fromCornersRadius(15f);
                GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());
                GenericDraweeHierarchy hierarchy = builder
                        .setRoundingParams(roundingParams)
                        .setProgressBarImage(new CircleProgressDrawable())
                        //  .setPlaceholderImage(context.getResources().getDrawable(R.drawable.carregando))
                        .build();
                holder.capa.setHierarchy(hierarchy);

            } else {
                Toast.makeText(context, "erro", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public int getItemCount() {
        return lista_digital.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private SimpleDraweeView capa;
        private TextView titulo,descricao;

        public MyViewHolder(View itemView) {
            super(itemView);

            capa= itemView.findViewById(R.id.capacategoria);
            titulo = itemView.findViewById(R.id.texttitulo_categoria);
            descricao = itemView.findViewById(R.id.text_desc_categoria);
        }
    }
}