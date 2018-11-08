package com.example.fulanoeciclano.nerdzone.Votacao.Adapter_resultado;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fulanoeciclano.nerdzone.Helper.CircleProgressDrawable;
import com.example.fulanoeciclano.nerdzone.R;
import com.example.fulanoeciclano.nerdzone.Votacao.model_votacao.Categoria_Pessoa_masc;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import me.ithebk.barchart.BarChart;
import me.ithebk.barchart.BarChartModel;

public class Adapter_resultado_digital_masc extends RecyclerView.Adapter<Adapter_resultado_digital_masc.MyViewHolder> {

    private ArrayList<Categoria_Pessoa_masc> lista_digital;
    private Context context;

    public Adapter_resultado_digital_masc(Context c, ArrayList<Categoria_Pessoa_masc> categora){
        this.context=c;
        this.lista_digital=categora;
    }

    public List<Categoria_Pessoa_masc> getcategoria(){
        return this.lista_digital;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_categoria_resultado,parent,false);

        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Categoria_Pessoa_masc categoria_pessoa = lista_digital.get(position);

        if(categoria_pessoa.getNome()!=null){
            holder.titulo.setText(categoria_pessoa.getNome());
        }
        holder.grafico.setBarMaxValue(1000);
        BarChartModel barChartModel = new BarChartModel();
        barChartModel.setBarValue(categoria_pessoa.getVotos());
       // barChartModel.setBarColor(Color.parseColor("#9C27B0"));
        barChartModel.setBarTag(null); //You can set your own tag to bar model
        barChartModel.setBarText(String.valueOf(categoria_pessoa.getVotos()));
        holder.grafico.addBar(barChartModel);

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
        private BarChart grafico;

        public MyViewHolder(View itemView) {
            super(itemView);

            grafico = itemView.findViewById(R.id.resultado_grafico_horizontal);
            capa= itemView.findViewById(R.id.capacategoria__resultado);
            titulo = itemView.findViewById(R.id.texttitulo_categoria_resultado);
        }
    }
}
