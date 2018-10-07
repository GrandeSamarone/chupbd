package com.example.fulanoeciclano.nerdzone.PerfilAmigos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fulanoeciclano.nerdzone.Activits.ChatActivity;
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Fragments.perfil.Art_Perfil_Fragment;
import com.example.fulanoeciclano.nerdzone.Fragments.perfil.Contos_Perfil_Fragment;
import com.example.fulanoeciclano.nerdzone.Fragments.perfil.Livros_Perfil_Fragment;
import com.example.fulanoeciclano.nerdzone.Fragments.perfil.Topicos_Perfil_Fragment;
import com.example.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Perfil extends AppCompatActivity implements View.OnClickListener {

    private TextView nome,fraserapida,n_seguidores;
    private CircleImageView imgperfil;
    private SimpleDraweeView capausuario;
    private ChildEventListener ChildEventListenerperfil;
    private DatabaseReference database_perfil,seguidores_ref,usuarioLogadoRef;
    private ViewPager mViewPager;
    private LinearLayout botao_voltar;
    private Button botao_seguir,botao_msg;
    private String id_do_usuario;
    private String id_usuariologado;
    private Usuario usuarioLogado,usuarioselecionado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        id_do_usuario=getIntent().getStringExtra("id");

     //Configuraçoes Originais
        id_usuariologado =  UsuarioFirebase.getIdentificadorUsuario();
    database_perfil = ConfiguracaoFirebase.getDatabase().getReference().child("usuarios");
    seguidores_ref = ConfiguracaoFirebase.getDatabase().getReference().child("seguidores");
     nome = findViewById(R.id.nomeusuario_perfil);
     n_seguidores= findViewById(R.id.n_seguidores);
     fraserapida= findViewById(R.id.fraserapida_perfil);
     capausuario = findViewById(R.id.capaperfilusuario);
     imgperfil= findViewById(R.id.circleImageViewFotoPerfilusuario);
     botao_voltar = findViewById(R.id.perfil_button_back_perfil);
     botao_voltar.setOnClickListener(this);
     botao_msg = findViewById(R.id.botaomensagemperfil);
     botao_msg.setOnClickListener(this);
     botao_seguir = findViewById(R.id.botaoseguirperfil);
     botao_seguir.setOnClickListener(this);
        //Configurar Abas
        final FragmentPagerItemAdapter adapter= new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("HISTÓRIAS", Livros_Perfil_Fragment.class )
                        // .add("Noticia",Noticia_Fragment.class)
                        .add("TOPICOS", Topicos_Perfil_Fragment.class)
                        .add("CONTOS",Contos_Perfil_Fragment.class)
                        .add("FANARTS",Art_Perfil_Fragment.class)
                        // .add("Tops", RankFragment.class)
                        .create()
        );
        SmartTabLayout ViewPageTab = findViewById(R.id.SmartTabLayoutperfilusuario);
        mViewPager = findViewById(R.id.viewPagerperfilusuario);
        mViewPager.setAdapter(adapter);
        ViewPageTab.setViewPager(mViewPager);





    }

    @Override
    protected void onStart() {
        super.onStart();
        CarregarDados_do_Usuario();
        recuperarDadosUsuarioLogado();

    }

    @Override
    protected void onStop() {
        super.onStop();
        database_perfil.removeEventListener(ChildEventListenerperfil);
    }

    @Override
    public void onClick(View v) {
        switch ( v.getId()){
            case R.id.perfil_button_back_perfil:
                finish();
                break;
            case R.id.botaomensagemperfil:
                Intent it = new Intent(Perfil.this, ChatActivity.class);
                it.putExtra("id",id_do_usuario);
                startActivity(it);
                break;
        }
    }
    private void CarregarDados_do_Usuario(){
        ChildEventListenerperfil=database_perfil.orderByChild("id")
                .equalTo(id_do_usuario).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        usuarioselecionado = dataSnapshot.getValue(Usuario.class );
                        assert usuarioselecionado != null;


                         nome.setText(usuarioselecionado.getNome());
                         n_seguidores.setText(String.valueOf(usuarioselecionado.getSeguidores()));
                        Uri  capa = Uri.parse(usuarioselecionado.getCapa());

                            DraweeController controllerOne = Fresco.newDraweeControllerBuilder()
                                    .setUri(capa)
                                    .setAutoPlayAnimations(true)
                                    .build();

                            capausuario.setController(controllerOne);


                        String fotoperfil = usuarioselecionado.getFoto();
                        Glide.with(Perfil.this)
                                .load(fotoperfil)
                                .into(imgperfil);
                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        usuarioselecionado = dataSnapshot.getValue(Usuario.class );
                        assert usuarioselecionado != null;
                        n_seguidores.setText(String.valueOf(usuarioselecionado.getSeguidores()));
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



    private void recuperarDadosUsuarioLogado(){
        usuarioLogadoRef= database_perfil.child(id_usuariologado);
        usuarioLogadoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                usuarioLogado = dataSnapshot.getValue(Usuario.class);

             /*verifica se o  usuario esta seguindo*/
                VerificaSegueUsuarioAmigo();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void VerificaSegueUsuarioAmigo(){
        DatabaseReference seguidor_ref=seguidores_ref
                .child(usuarioselecionado.getId())
                .child(id_usuariologado);
               seguidor_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                       if(dataSnapshot.exists()){
                       HabilitarBotaoSeguir(true);
                       }else{
                        HabilitarBotaoSeguir(false);
                       }
                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }
               });
    }

    private void HabilitarBotaoSeguir(Boolean segueUsuario){
        if(segueUsuario){
            botao_seguir.setText(R.string.botao_txt_seguindo);
        botao_seguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder msgbox = new AlertDialog.Builder(Perfil.this);
                //configurando o titulo
                msgbox.setTitle("Deixa de seguir");
                // configurando a mensagem
                msgbox.setMessage("Deseja Realmente Deixa de seguir "+usuarioselecionado.getNome()+" ?");
                // Botao negativo

                msgbox.setPositiveButton("Sim",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int wich) {
                                RemoverSeguidor(usuarioLogado,usuarioselecionado);
                                   }

                        });


                msgbox.setNegativeButton("Não",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int wich) {
                                dialog.dismiss();
                            }
                        });
                msgbox.show();

            }
        });

        }else{
            botao_seguir.setText(R.string.botao_txt_seguir);

            //Adiciona Evento para seguir Usuario
            botao_seguir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Salvar Seguidor
                    SalvarSeguidor(usuarioLogado,usuarioselecionado);
                }
            });
        }
    }

private void SalvarSeguidor(Usuario ulogado,Usuario uamigo){

    HashMap<String, Object> dadosUsuarioLogado = new HashMap<>();
    dadosUsuarioLogado.put("id", ulogado.getId() );
    dadosUsuarioLogado.put("nome", ulogado.getNome() );
    dadosUsuarioLogado.put("caminhoFoto", ulogado.getFoto() );

        DatabaseReference seguidorRef = seguidores_ref
               .child(uamigo.getId())
               .child(ulogado.getId());
               seguidorRef.setValue(dadosUsuarioLogado);

               botao_seguir.setText(R.string.botao_txt_seguindo);
    botao_seguir.setText(R.string.botao_txt_seguir);
    botao_seguir.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RemoverSeguidor(usuarioLogado,usuarioselecionado);
        }
    });
    Toast.makeText(this, "Agora você verá tudo que o "+uamigo.getNome()+"Postar", Toast.LENGTH_SHORT).show();

    //Incrementar seguidores do amigo
    int seguidores = uamigo.getSeguidores() + 1;
    HashMap<String, Object> dadosSeguidores = new HashMap<>();
    dadosSeguidores.put("seguidores", seguidores );
    DatabaseReference usuarioSeguidores = database_perfil
            .child( uamigo.getId() );
    usuarioSeguidores.updateChildren( dadosSeguidores );

    //Incrementar seguindo do usuário logado
    int seguindo = ulogado.getSeguindo() + 1;
    HashMap<String, Object> dadosSeguindo = new HashMap<>();
    dadosSeguindo.put("seguindo", seguindo );
    DatabaseReference usuarioSeguindo = database_perfil
            .child( ulogado.getId() );
    usuarioSeguindo.updateChildren( dadosSeguindo );

    }
private void RemoverSeguidor(Usuario ulogado,Usuario uamigo){

    DatabaseReference seguidorRef = seguidores_ref
            .child(uamigo.getId())
            .child(ulogado.getId());
    seguidorRef.removeValue();

    botao_seguir.setText(R.string.botao_txt_seguir);
    botao_seguir.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SalvarSeguidor(usuarioLogado,usuarioselecionado);
        }
    });

    Toast.makeText(this, "Não verá mais atualização  "+uamigo.getNome()+" Postar", Toast.LENGTH_SHORT).show();

    //deletar seguidores do amigo
    int seguidores = uamigo.getSeguidores() - 1;
    HashMap<String, Object> dadosSeguidores = new HashMap<>();
    dadosSeguidores.put("seguidores", seguidores );
    DatabaseReference usuarioSeguidores = database_perfil
            .child( uamigo.getId() );
    usuarioSeguidores.updateChildren( dadosSeguidores );


    //deletar seguindo
    int seguindo = ulogado.getSeguindo() -1 ;
    HashMap<String, Object> dadosSeguindo = new HashMap<>();
    dadosSeguindo.put("seguindo", seguindo );
    DatabaseReference usuarioSeguindo = database_perfil
            .child( ulogado.getId() );
    usuarioSeguindo.updateChildren( dadosSeguindo );
}

}
