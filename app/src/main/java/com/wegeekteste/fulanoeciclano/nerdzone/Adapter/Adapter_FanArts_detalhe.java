package com.wegeekteste.fulanoeciclano.nerdzone.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.CircleProgressDrawable;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.FanArts;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Usuario;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.List;

public class Adapter_FanArts_detalhe extends RecyclerView.Adapter<Adapter_FanArts_detalhe.MyViewHolder> {

private List<FanArts> listarts;
private Context context;
Usuario usuariologado = UsuarioFirebase.getDadosUsuarioLogado();

public Adapter_FanArts_detalhe(List<FanArts> arts,Context  context){
        this.listarts=arts;
        this.context=context;

        }

@NonNull
@Override
public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_fanarts_pag_inicial, parent, false);
        return new MyViewHolder(itemLista);
        }

@Override
public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
final FanArts fanArts = listarts.get(position);


String url = fanArts.getArtfoto();
if(url!=null){
     ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
             .setLocalThumbnailPreviewsEnabled(true)
             .setProgressiveRenderingEnabled(true)
             .build();

     DraweeController controller = Fresco.newDraweeControllerBuilder()
             .setImageRequest(request)
             .build();
     holder.img.setController(controller);

     GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());
     GenericDraweeHierarchy hierarchy = builder
             .setProgressBarImage(new CircleProgressDrawable())
             //  .setPlaceholderImage(context.getResources().getDrawable(R.drawable.carregando))
             .build();
     holder.img.setHierarchy(hierarchy);
 }

        }

@Override
public int getItemCount() {
        return listarts.size();
        }

public class MyViewHolder extends RecyclerView.ViewHolder {
    private SimpleDraweeView img;

    public MyViewHolder(View itemView) {
        super(itemView);
        img = itemView.findViewById(R.id.iconefanart);
    }
}
}
