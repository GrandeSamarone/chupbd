package com.wegeekteste.fulanoeciclano.nerdzone.Icons;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.wegeekteste.fulanoeciclano.nerdzone.Activits.MinhaConta;
import com.wegeekteste.fulanoeciclano.nerdzone.Fragments.Fragments_Icons.AleatorioFragment;
import com.wegeekteste.fulanoeciclano.nerdzone.Fragments.Fragments_Icons.DesenhoFragment;
import com.wegeekteste.fulanoeciclano.nerdzone.Fragments.Fragments_Icons.FilmesFragment;
import com.wegeekteste.fulanoeciclano.nerdzone.Fragments.Fragments_Icons.HeroiFragment;
import com.wegeekteste.fulanoeciclano.nerdzone.Fragments.Fragments_Icons.PretoeBrancoFragment;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import static com.wegeekteste.fulanoeciclano.nerdzone.Feed.FeedActivity.setWindowFlag;


public class PageIcon extends AppCompatActivity {

    private ViewPager mViewPagerIcons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_page_icon);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarsecundario);
        toolbar.setTitle("Escolha um icone");
        setSupportActionBar(toolbar);

        //Configurar Abas
        final FragmentPagerItemAdapter adapter= new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("COMIC", HeroiFragment.class )
                        // .add("Noticia",Noticia_Fragment.class)
                        .add("POP", AleatorioFragment.class)
                        .add("FILME",FilmesFragment.class)
                        .add("DESENHO",DesenhoFragment.class)
                        .add("BLACK",PretoeBrancoFragment.class)
                        // .add("Tops", RankFragment.class)
                        .create()
        );
        SmartTabLayout ViewPageTab = findViewById(R.id.SmartTabLayoutIcons);
        mViewPagerIcons = findViewById(R.id.viewPagerIcons);
        mViewPagerIcons.setAdapter(adapter);
        ViewPageTab.setViewPager(mViewPagerIcons);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TrocarFundos_status_bar();
    }

    //Botao Voltar
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                Intent it = new Intent(PageIcon.this, MinhaConta.class);
                startActivity(it);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }else{
                    finish();
                }
                break;

            default:
                break;
        }

        return true;
    }
    //Nao muito uteis
    private void TrocarFundos_status_bar(){
        //mudando a cor do statusbar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintResource(R.drawable.gradiente_toolbarstatusbar);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintResource(R.drawable.gradiente_toolbarstatusbar);
            //  systemBarTintManager.setStatusBarTintDrawable(Mydrawable);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.parseColor("#1565c0"));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setNavigationBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintResource(R.drawable.gradiente_toolbarstatusbar);
        }
    }

}
