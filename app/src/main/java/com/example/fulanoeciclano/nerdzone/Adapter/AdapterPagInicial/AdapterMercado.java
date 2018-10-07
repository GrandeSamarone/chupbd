package com.example.fulanoeciclano.nerdzone.Adapter.AdapterPagInicial;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fulanoeciclano.nerdzone.Model.Comercio;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

/**
 * Created by fulanoeciclano on 30/08/2018.
 */

public class AdapterMercado extends RecyclerView.Adapter<AdapterMercado.MyviewHolder> {
    private Context context;
    private List<Comercio> comercios;


    public AdapterMercado(List<Comercio> merc, Context cx){
        this.context = cx;
        this.comercios = merc;
    }
    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapterevento,parent,false);

        return new AdapterMercado.MyviewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {

        Comercio comercio = comercios.get(position);
        if(comercio.getTitulo()!=null){
            holder.mercadonome.setText(comercio.getTitulo());
        }


        List<String> urlFotos = comercio.getFotos();
        String stringcapa = urlFotos.get(0);

        if (stringcapa != null) {
            Uri uri = Uri.parse(stringcapa);

            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setProgressiveRenderingEnabled(true)

                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setTapToRetryEnabled(true)
                    .setImageRequest(request)

                    .setOldController(holder.mercadocapa.getController())
                    .build();

            /*mSimpleDraweeView.setController(controller);
            DraweeController controllerOne = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setAutoPlayAnimations(true)
                    .build();
*/
            holder.mercadocapa.setController(controller);
        }
        holder.card.setRadius(9);
    }

    @Override
    public int getItemCount() {
        return comercios.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView mercadocapa;
        TextView mercadonome;
        LinearLayout mercadolayout;
        CardView card;

        public MyviewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.cardevento);
            mercadocapa = itemView.findViewById(R.id.iconeevento);
            mercadonome = itemView.findViewById(R.id.nomeevento);

        }
    }
}
