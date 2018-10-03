package com.example.fulanoeciclano.nerdzone.PerfilAmigos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fulanoeciclano.nerdzone.Activits.ChatActivity;
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Fragments.perfil.Art_Perfil_Fragment;
import com.example.fulanoeciclano.nerdzone.Fragments.perfil.Contos_Perfil_Fragment;
import com.example.fulanoeciclano.nerdzone.Fragments.perfil.Livros_Perfil_Fragment;
import com.example.fulanoeciclano.nerdzone.Fragments.perfil.Topicos_Perfil_Fragment;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import de.hdodenhof.circleimageview.CircleImageView;

public class Perfil extends AppCompatActivity implements View.OnClickListener {

    private TextView nome,fraserapida;
    private CircleImageView imgperfil;
    private SimpleDraweeView capausuario;
    private ChildEventListener ChildEventListenerperfil;
    private DatabaseReference database_perfil;
    private ViewPager mViewPager;
    private LinearLayout botao_voltar;
    private Button botao_seguir,botao_msg;
    private String id_do_usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        id_do_usuario=getIntent().getStringExtra("id");

     //Configuraçoes Originais
    database_perfil = ConfiguracaoFirebase.getDatabase().getReference().child("usuarios");
     nome = findViewById(R.id.nomeusuario_perfil);
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





        CarregarDados_do_Usuario();
    }


    private void CarregarDados_do_Usuario(){

        ChildEventListenerperfil=database_perfil.orderByChild("id")
                .equalTo(id_do_usuario).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Usuario usuario = dataSnapshot.getValue(Usuario.class );
                        assert usuario != null;


                         nome.setText(usuario.getNome());

                        Uri  capa = Uri.parse(usuario.getCapa());
                        DraweeController controllerOne = Fresco.newDraweeControllerBuilder()
                                .setUri(capa)
                                .setAutoPlayAnimations(true)
                                .build();

                        capausuario.setController(controllerOne);


                        String fotoperfil = usuario.getFoto();
                        Glide.with(Perfil.this)
                                .load(fotoperfil)
                                .into(imgperfil);


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
}
