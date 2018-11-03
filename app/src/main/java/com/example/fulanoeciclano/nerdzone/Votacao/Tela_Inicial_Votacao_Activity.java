package com.example.fulanoeciclano.nerdzone.Votacao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

import com.example.fulanoeciclano.nerdzone.R;
import com.example.fulanoeciclano.nerdzone.Votacao.Listar.canal_youtube.Lista_canal_youtube_Activity;
import com.example.fulanoeciclano.nerdzone.Votacao.Listar.cinema.Lista_cinema_Activity;
import com.example.fulanoeciclano.nerdzone.Votacao.Listar.cosplay.Lista_cosplay_Femi;
import com.example.fulanoeciclano.nerdzone.Votacao.Listar.cosplay.Lista_cosplay_masc;
import com.example.fulanoeciclano.nerdzone.Votacao.Listar.digital_influencer.Lista_digital_fem;
import com.example.fulanoeciclano.nerdzone.Votacao.Listar.digital_influencer.Lista_digital_masc;
import com.example.fulanoeciclano.nerdzone.Votacao.Listar.empreendedor.Lista_empreendedor;
import com.example.fulanoeciclano.nerdzone.Votacao.Listar.empreendedora.Lista_empreendedora;
import com.example.fulanoeciclano.nerdzone.Votacao.Listar.escritor_roterista.Lista_escritor;
import com.example.fulanoeciclano.nerdzone.Votacao.Listar.escritora_roterista.Lista_escritora;
import com.example.fulanoeciclano.nerdzone.Votacao.Listar.espaco_geek.Lista_espaco_geek;
import com.example.fulanoeciclano.nerdzone.Votacao.Listar.kpop.Lista_kpop_fem;
import com.example.fulanoeciclano.nerdzone.Votacao.Listar.kpop.Lista_kpop_masc;
import com.example.fulanoeciclano.nerdzone.Votacao.Listar.livro_quadrinho.Lista_livro_quadrinho;
import com.example.fulanoeciclano.nerdzone.Votacao.Listar.loja_virtual.Lista_loja_vitural;
import com.example.fulanoeciclano.nerdzone.Votacao.Listar.portal_noticia.Lista_portal_noticia;
import com.example.fulanoeciclano.nerdzone.Votacao.Listar.reporter.Lista_reporter_fem;
import com.example.fulanoeciclano.nerdzone.Votacao.Listar.reporter.Lista_reporter_masc;
import com.example.fulanoeciclano.nerdzone.Votacao.Listar.youtuber.Lista_youtuber_fem;
import com.example.fulanoeciclano.nerdzone.Votacao.Listar.youtuber.Lista_youtuber_masc;

public class Tela_Inicial_Votacao_Activity extends AppCompatActivity implements View.OnClickListener {

    private CardView digit_masc,digital_fem,cosplay_masc,cosplay_fem,portal_noticia,livro_quadrinho,
    youtuber_masc,youtuber_fem,repoter_masc,reporter_fem,cinema,canal_youtube,kpop_masc,kpop_fem,
    espaco_geek,loja_virtual,escritor_masc,escritora_fem,empreendedor_masc,empreendedora_fem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela__inicial__votacao_);

        digit_masc = findViewById(R.id.digital_influencer_masc);
        digit_masc.setOnClickListener(this);
        digital_fem = findViewById(R.id.digital_influencer_fem);
        digital_fem.setOnClickListener(this);
        cosplay_masc=findViewById(R.id.cosplay_masc);
        cosplay_masc.setOnClickListener(this);
        cosplay_fem = findViewById(R.id.cosplay_fem);
        cosplay_fem.setOnClickListener(this);
        portal_noticia=findViewById(R.id.portal_noticia);
        portal_noticia.setOnClickListener(this);
        livro_quadrinho= findViewById(R.id.livro_quadrinho);
        livro_quadrinho.setOnClickListener(this);
        youtuber_masc= findViewById(R.id.youtuber_masc);
        youtuber_masc.setOnClickListener(this);
        youtuber_fem = findViewById(R.id.youtuber_fem);
        youtuber_fem.setOnClickListener(this);
        repoter_masc = findViewById(R.id.reporter_masc);
        repoter_masc.setOnClickListener(this);
        reporter_fem = findViewById(R.id.reporter_fem);
        reporter_fem.setOnClickListener(this);
        cinema = findViewById(R.id.cinema);
        cinema.setOnClickListener(this);
        canal_youtube = findViewById(R.id.canal_do_youtube);
        canal_youtube.setOnClickListener(this);
        kpop_masc = findViewById(R.id.kpop_masc);
        kpop_masc.setOnClickListener(this);
        kpop_fem = findViewById(R.id.kpop_fem);
        kpop_fem.setOnClickListener(this);
        espaco_geek = findViewById(R.id.espaco_geek);
        espaco_geek.setOnClickListener(this);
        loja_virtual = findViewById(R.id.loja_virtual);
        loja_virtual.setOnClickListener(this);
        escritor_masc = findViewById(R.id.escritor_masc);
        escritor_masc.setOnClickListener(this);
        escritora_fem = findViewById(R.id.escritora_fem);
        escritora_fem.setOnClickListener(this);
        empreendedor_masc = findViewById(R.id.empreendedor_masc);
        empreendedor_masc.setOnClickListener(this);
        empreendedora_fem = findViewById(R.id.empreendedora_fem);
        empreendedora_fem.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case  R.id.digital_influencer_masc:
                Intent it_digit_masc = new Intent( Tela_Inicial_Votacao_Activity.this, Lista_digital_masc.class);
                startActivity(it_digit_masc);
                break;

            case  R.id.digital_influencer_fem:
                Intent it_dig_fem = new Intent(Tela_Inicial_Votacao_Activity.this, Lista_digital_fem.class);
                startActivity(it_dig_fem);
                break;

            case  R.id.cosplay_masc:
                Intent it_cosp_masc= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_cosplay_masc.class);
                startActivity(it_cosp_masc);
                break;

            case  R.id.cosplay_fem:
                Intent it_cosp_fem= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_cosplay_Femi.class);
                startActivity(it_cosp_fem);
                break;

            case  R.id.portal_noticia:
                Intent it_portal_noti= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_portal_noticia.class);
                startActivity(it_portal_noti);
                break;

            case  R.id.livro_quadrinho:
                Intent it_livro_quad= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_livro_quadrinho.class);
                startActivity(it_livro_quad);
                break;

            case  R.id.youtuber_masc:
                Intent it_youtuber_masc= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_youtuber_masc.class);
                startActivity(it_youtuber_masc);
                break;

            case  R.id.youtuber_fem:
                Intent it_youtuber_fem= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_youtuber_fem.class);
                startActivity(it_youtuber_fem);
                break;

            case  R.id.reporter_masc:
                Intent it_reporter_masc= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_reporter_masc.class);
                startActivity(it_reporter_masc);
                break;

            case  R.id.reporter_fem:
                Intent it_reporter_fem= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_reporter_fem.class);
                startActivity(it_reporter_fem);
                break;

            case  R.id.cinema:
                Intent it_cinema= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_cinema_Activity.class);
                startActivity(it_cinema);
                break;

            case  R.id.canal_do_youtube:
                Intent it_canal_youtube= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_canal_youtube_Activity.class);
                startActivity(it_canal_youtube);
                break;

            case  R.id.kpop_masc:
                Intent it_kpop_masc= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_kpop_masc.class);
                startActivity(it_kpop_masc);
                break;

            case  R.id.kpop_fem:
                Intent it_kpop_fem= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_kpop_fem.class);
                startActivity(it_kpop_fem);
                break;

            case  R.id.espaco_geek:
                Intent it_epaco_geek= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_espaco_geek.class);
                startActivity(it_epaco_geek);
                break;

            case  R.id.loja_virtual:
                Intent loja_virtual= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_loja_vitural.class);
                startActivity(loja_virtual);
                break;

            case  R.id.escritor_masc:
                Intent escritor_masc= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_escritor.class);
                startActivity(escritor_masc);
                break;

            case  R.id.escritora_fem:
                Intent escritora_fem= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_escritora.class);
                startActivity(escritora_fem);
                break;

            case  R.id.empreendedor_masc:
                Intent empreendedor_masc= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_empreendedor.class);
                startActivity(empreendedor_masc);
                break;

            case  R.id.empreendedora_fem:
                Intent empreendedora_fem= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_empreendedora.class);
                startActivity(empreendedora_fem);
                break;
         }
    }
}
