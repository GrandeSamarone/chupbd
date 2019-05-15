package com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_MinhasPublicacoes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.wegeekteste.fulanoeciclano.nerdzone.Activits.Minhas_Publicacoes;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.FanArts.Detalhe_FarArts_Activity;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.FanArts;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Usuario;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.List;

public class Adapter_Minhas_arts extends RecyclerView.Adapter<Adapter_Minhas_arts.MyviewHolder> {

    private Context context;
    private List<FanArts> fanArtsList;
    private  String identificadorUsuario =  UsuarioFirebase.getIdentificadorUsuario();
    private Usuario user;
    public Adapter_Minhas_arts(Context c,List<FanArts> fanArts){
        this.context=c;
        this.fanArtsList=fanArts;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_minhas_arts, parent,
                false);

        return new MyviewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        final FanArts fanArts = fanArtsList.get(position);

     holder.nome_art.setText(fanArts.getLegenda());
     holder.n_add_colecao.setText(String.valueOf(fanArts.getQuantcolecao()));
     holder.n_curtida.setText(String.valueOf(fanArts.getLikecount()));

      String uri = fanArts.getArtfoto();

      if(uri!=null) {
          ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri))
                  .setProgressiveRenderingEnabled(true)
                  .build();
          RoundingParams roundingParams = RoundingParams.fromCornersRadius(7f);
          holder.icone_art.setHierarchy(new GenericDraweeHierarchyBuilder(context.getResources())
                  .setRoundingParams(roundingParams)
                  .build());
          DraweeController controller = Fresco.newDraweeControllerBuilder()
                  .setTapToRetryEnabled(true)

                  .setImageRequest(request)
                  .setOldController(holder.icone_art.getController())

                  .build();


          DatabaseReference eventoscurtidas = ConfiguracaoFirebase.getFirebaseDatabase()
                  .child("usuarios")
                  .child(identificadorUsuario);
          eventoscurtidas.addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                  user = dataSnapshot.getValue(Usuario.class);

              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
          });


          holder.icone_art.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent it = new Intent(context, Detalhe_FarArts_Activity.class);
                  it.putExtra("id", fanArts.getId());
                  context.startActivity(it);
              }
          });
          holder.icone_art.setController(controller);
      }
        holder.deletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder msgbox = new AlertDialog.Builder(context);
                //configurando o titulo
                msgbox.setTitle("Excluir");
                // configurando a mensagem
                msgbox.setMessage("Deseja Realmente excluir  "+fanArts.getLegenda()+" ?");
                // Botao negativo

                msgbox.setPositiveButton("Sim",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int wich) {
                                fanArts.remover();
                                int qtdArt = user.getArts() - 1;
                                user.setArts(qtdArt);
                                user.atualizarQtdFanArts();
                                Intent it = new Intent(context, Minhas_Publicacoes.class);
                                context.startActivity(it);
                                Toast toast = Toast.makeText(context, "Deletado com sucesso!", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                                toast.show();  }

                        });


                msgbox.setNegativeButton("Não",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int wich) {

                            }
                        });
                msgbox.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return fanArtsList.size();
    }



    public class MyviewHolder extends RecyclerView.ViewHolder {

        private ImageView colecao,curtir;
        private TextView nome_art,n_curtida,n_add_colecao,deletar;
        private SimpleDraweeView icone_art;

        public MyviewHolder(View itemView) {
            super(itemView);

        n_curtida=itemView.findViewById(R.id.minhas_arts_num_curt);
        deletar = itemView.findViewById(R.id.excluirart);
        n_add_colecao=itemView.findViewById(R.id.minhas_arts_num_colecao);
        colecao=itemView.findViewById(R.id.botao_add_art_colecao);
        curtir= itemView.findViewById(R.id.botaocurtirart);
        icone_art = itemView.findViewById(R.id.iconeart);
        nome_art= itemView.findViewById(R.id.nome_art);
        }
    }
}
