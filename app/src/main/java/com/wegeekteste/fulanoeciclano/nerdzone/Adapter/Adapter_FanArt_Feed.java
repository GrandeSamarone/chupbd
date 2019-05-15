package com.wegeekteste.fulanoeciclano.nerdzone.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.vanniktech.emoji.EmojiTextView;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.CircleProgressDrawable;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.FanArts;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Usuario;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_FanArt_Feed extends RecyclerView.Adapter<Adapter_FanArt_Feed.MyViewHolder> {

    private List<FanArts> listarts;
    private Context context;
    private ChildEventListener ChildEventListenerperfil;
    private DatabaseReference databasesUsuario= ConfiguracaoFirebase.getFirebaseDatabase().child("usuarios");
    Usuario usuariologado = UsuarioFirebase.getDadosUsuarioLogado();
    public Adapter_FanArt_Feed(List<FanArts> arts,Context  context){
        this.listarts=arts;
        this.context=context;

    }
    public List<FanArts> getfanarts(){
        return this.listarts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_fararts_feed, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final FanArts fanArts = listarts.get(position);

        Uri uri = Uri.parse(fanArts.getArtfoto());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .setProgressiveRenderingEnabled(true)
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .build();
        holder.feed_art.setController(controller);

        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setProgressBarImage(new CircleProgressDrawable())
                //  .setPlaceholderImage(context.getResources().getDrawable(R.drawable.carregando))
                .build();
        holder.feed_art.setHierarchy(hierarchy);


        ChildEventListenerperfil=databasesUsuario.orderByChild("id").equalTo(fanArts.getIdauthor()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Usuario perfil = dataSnapshot.getValue(Usuario.class );
                assert perfil != null;

                holder.nome_user.setText(perfil.getNome());
                String icone = perfil.getFoto();
                Glide.with(context)
                        .load(icone)
                        .into(holder.icone_user);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return listarts.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView icone_user;
        private SimpleDraweeView feed_art;
        private EmojiTextView nome_user;
        public MyViewHolder(View itemView) {
            super(itemView);

            icone_user = itemView.findViewById(R.id.icone_user_feed);
            feed_art = itemView.findViewById(R.id.imgart_feed);
            nome_user = itemView.findViewById(R.id.author_art_feed);
        }
    }
}
