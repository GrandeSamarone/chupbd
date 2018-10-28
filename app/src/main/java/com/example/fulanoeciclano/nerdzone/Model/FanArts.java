package com.example.fulanoeciclano.nerdzone.Model;

import android.util.Log;

import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FanArts implements Serializable {

private String id;
private String idauthor;
private String artfoto;
private String legenda;
private int likecount = 0;
private int quantcolecao=0;
private int quantvizualizacao=0;

    public FanArts() {
        DatabaseReference eventoref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("fanarts");
        setId(eventoref.push().getKey());  }

public void Salvar(DataSnapshot seguidoressnapshot){

    Map objeto = new HashMap();
    DatabaseReference artref = ConfiguracaoFirebase.getFirebaseDatabase();
    String combinacaoId="/"+ getIdauthor()+"/"+getId();

    objeto.put("/minhasarts"+combinacaoId,this);

    for(DataSnapshot Seguidores:seguidoressnapshot.getChildren()){
        Log.i("sewgrsw", String.valueOf(seguidoressnapshot.getKey()));
        String idSeguidores=Seguidores.getKey();
        HashMap<String,Object> dadosSeguidor = new HashMap<>();
        dadosSeguidor.put("idarts",getId());
        String IdAtualizacao="/"+ idSeguidores+"/"+getId();

        objeto.put("/feed-arts"+IdAtualizacao,dadosSeguidor);
    }

    artref.updateChildren(objeto);

    salvarArtPublico();

}

    public void salvarArtPublico(){
        DatabaseReference anuncioref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("arts");
        anuncioref.child(getId()).setValue(this);
    }
    public  void AdicioneiConto(){
        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        DatabaseReference anuncioref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("adicionei-arts");
        anuncioref.child(identificadorUsuario)
                .child(getId()).setValue(this);

        salvarArtPublico();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdauthor() {
        return idauthor;
    }

    public void setIdauthor(String idauthor) {
        this.idauthor = idauthor;
    }

    public String getArtfoto() {
        return artfoto;
    }

    public void setArtfoto(String artfoto) {
        this.artfoto = artfoto;
    }

    public String getLegenda() {
        return legenda;
    }

    public void setLegenda(String legenda) {
        this.legenda = legenda;
    }


    public int getLikecount() {
        return likecount;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }

    public int getQuantcolecao() {
        return quantcolecao;
    }

    public void setQuantcolecao(int quantcolecao) {
        this.quantcolecao = quantcolecao;
    }

    public int getQuantvizualizacao() {
        return quantvizualizacao;
    }

    public void setQuantvizualizacao(int quantvizualizacao) {
        this.quantvizualizacao = quantvizualizacao;
    }
}
