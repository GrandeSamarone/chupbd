package com.example.fulanoeciclano.nerdzone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fulanoeciclano.nerdzone.Mercado.Detalhe_Mercado;
import com.example.fulanoeciclano.nerdzone.Model.Mercado;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

public class Adapter_MeusComercio extends RecyclerView.Adapter<Adapter_MeusComercio.MyviewHolder> {

    private Context context;
    private List<Mercado> mercado;

    public Adapter_MeusComercio(List<Mercado> listamercado, Context c) {

        this.context = c;
        this.mercado = listamercado;
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_meus_mercado, parent,
                false);

        return new Adapter_MeusComercio.MyviewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {

        final Mercado ev = mercado.get(position);
        holder.comercionome.setText(ev.getTitulo());

        List<String> urlFotos = ev.getFotos();
        if (urlFotos != null) {
            String stringcapa = urlFotos.get(0);
            if (stringcapa != null) {

                Uri uri = Uri.parse(stringcapa);
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                        .setProgressiveRenderingEnabled(true)
                        .build();
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setTapToRetryEnabled(true)
                        .setImageRequest(request)
                        .setOldController(holder.comerciocapa.getController())
                        .build();
            /*mSimpleDraweeView.setController(controller);
            DraweeController controllerOne = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setAutoPlayAnimations(true)
                    .build();
*/
                holder.comerciocapa.setController(controller);
//            holder.foto.getHierarchy().setRetryImage(R.drawable.emoji_google_1f3ca_1f3fb);
            }
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(context, Detalhe_Mercado.class);
                    it.putExtra("id_do_evento", ev.getIdMercado());
                    it.putExtra("UR_do_evento", ev.getEstado());
                    context.startActivity(it);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mercado.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
            SimpleDraweeView comerciocapa;
            TextView comercionome;
            LinearLayout eventolayout;
            CardView card;

            public MyviewHolder(View itemView) {
                super(itemView);


                comerciocapa = itemView.findViewById(R.id.iconemercado);
                card = itemView.findViewById(R.id.cardevento);
                comercionome = itemView.findViewById(R.id.nomemercado);
        }
    }
}

