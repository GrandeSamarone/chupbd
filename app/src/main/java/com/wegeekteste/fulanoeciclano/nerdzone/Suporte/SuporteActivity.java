package com.wegeekteste.fulanoeciclano.nerdzone.Suporte;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

public class SuporteActivity extends AppCompatActivity {
    private Spinner spinnerMotivo;
    private Toolbar toolbar;
    private String motivo;
    private EditText mensagem;
    private Button botaoenviar;
    private   String email;
    private FirebaseUser usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suporte);

        toolbar = findViewById(R.id.toolbarsecundario_sem_foto);
        toolbar.setTitle("Contato");
        setSupportActionBar(toolbar);

        spinnerMotivo=findViewById(R.id.spinner_motivo_suporte);
        botaoenviar=findViewById(R.id.btn_enviar_contato);
        mensagem=findViewById(R.id.desc_suporte);
        CarregarDadosSpinner();
        usuario = UsuarioFirebase.getUsuarioAtual();
      email = usuario.getEmail();

      botaoenviar.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              sendEmail();
          }
      });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }





    protected void sendEmail() {
        final String mensagems = mensagem.getText().toString();
        Log.i("Send email", "");

        String[] TO = {"suportewegeek@gmail.com"};
        String[] CC = {email};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, motivo);
        emailIntent.putExtra(Intent.EXTRA_TEXT, mensagems);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SuporteActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }



    }




    //carregar spinner
    private void CarregarDadosSpinner() {
        //
        String[] artista = getResources().getStringArray(R.array.suporte);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, artista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMotivo.setAdapter(adapter);
        spinnerMotivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                motivo = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
}

}
