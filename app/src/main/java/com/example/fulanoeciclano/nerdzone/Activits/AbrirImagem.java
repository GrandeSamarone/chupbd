package com.example.fulanoeciclano.nerdzone.Activits;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fulanoeciclano.nerdzone.Adapter.Adapter_ampliar_imagem;
import com.example.fulanoeciclano.nerdzone.Adapter.MyViewPagerAdapter;
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Model.Mercado;
import com.example.fulanoeciclano.nerdzone.R;
import com.google.firebase.database.DatabaseReference;

public class AbrirImagem extends AppCompatActivity {

    private String[] imageUrls ;
    private String nomedomercado;
    private Adapter_ampliar_imagem myAdapter;
    private ViewPager viewPager;
    private AlertDialog alerta;
    private MyViewPagerAdapter adapter;
    private Mercado mercado;
    private DatabaseReference fotos_mercado;
    private TextView tantasfotos,nome;
    private ImageView botaovoltar;
    private SharedPreferences primeiravez = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_abrir_imagem);
        viewPager = findViewById(R.id.viewpager_imagem);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
      //Recebendo o link da pagina detalhe do mercado e armazenando
        imageUrls= getIntent().getStringArrayListExtra("fotoselecionada").toArray(new String[0]);
        nomedomercado=getIntent().getStringExtra("nome");
        myAdapter = new Adapter_ampliar_imagem( AbrirImagem.this,imageUrls);
        viewPager.setAdapter(myAdapter);


          //Configuracoes Basicas
        fotos_mercado = ConfiguracaoFirebase.getFirebaseDatabase().child("mercado");
        tantasfotos = findViewById(R.id.tantasfotos);
        nome = findViewById(R.id.nome_do_mercado_fotos);
        nome.setText(nomedomercado);
        nome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tantasfotos.setText("1" + " de " + imageUrls.length);

        botaovoltar = findViewById(R.id.foto_button_back);
        botaovoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        primeiravez = getSharedPreferences("primeiravezzom", MODE_PRIVATE);
        if (primeiravez.getBoolean("primeiravezzom", true)) {
            primeiravez.edit().putBoolean("primeiravezzom", false).apply();
                Zoom();
        }else{

        }
    }
    private int prox(int i) {
        return viewPager.getCurrentItem() + i;
    }
    private int voltar(int i) {
        return viewPager.getCurrentItem()-1;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_finish:
                    finish();
                    return true;
                case R.id.navigation_zoom:
                    Zoom();
                    return true;
                case R.id.navigation_voltar:

                    viewPager.setCurrentItem(voltar(-1),true);

                    return true;
                case R.id.navigation_prox:

                    viewPager.setCurrentItem(prox(+1),true);
                    if(viewPager.getCurrentItem()+1==imageUrls.length){
                        Toast.makeText(AbrirImagem.this, "Ultima", Toast.LENGTH_LONG).show();



                    }
                    return true;
            }
            return false;
        }
    };
    //  page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position)
        {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void displayMetaInfo(int position) {
        tantasfotos.setText((position + 1) + " de " + imageUrls.length);
        nome.setText(nomedomercado);



    }

    @Override
    protected void onStart() {
        super.onStart();
       recuperarFotos();
    }

    private void recuperarFotos(){

    }

    //dialog de opcoes
    private void Zoom() {
        //LayoutInflater é utilizado para inflar nosso layout em uma view.
        //-pegamos nossa instancia da classe
        LayoutInflater li = getLayoutInflater();

        //inflamos o layout tela_opcao_foto.xml_foto.xml na view
        View view = li.inflate(R.layout.dialog_zoom, null);
        //definimos para o botão do layout um clickListener


        //Dialog de tela
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setView(view);
        alerta = builder.create();
        alerta.show();

    }

}
