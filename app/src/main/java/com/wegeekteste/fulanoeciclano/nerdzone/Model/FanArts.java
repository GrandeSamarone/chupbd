package com.wegeekteste.fulanoeciclano.nerdzone.Model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FanArts implements Serializable {

private String id;
private String idauthor;
private String categoria;
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
             anuncioref.child(getCategoria())
                .child(getId()).setValue(this);
    }
    public  void AdicioneiFanArts(){
        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        DatabaseReference anuncioref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("adicionei-arts");
        anuncioref.child(identificadorUsuario)
                .child(getId()).setValue(this);

        salvarArtPublico();
    }

    public void remover(){
        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        DatabaseReference anuncioref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("minhasarts")
                .child(identificadorUsuario)
                .child(getId());

        anuncioref.removeValue();
        removerfanartPublico();
        deletar_img_fanarts();
        removerfanartcolecao();
        removerfanartlike();
    }

    public void removerfanartPublico(){
        DatabaseReference anuncioref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("arts")
              .child(getCategoria())
                .child(getId());

        anuncioref.removeValue();

    }
    public void removerfanartcolecao(){
        DatabaseReference anuncioref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("fanarts-colecao")
                .child(getId());
        anuncioref.removeValue();
    }
    public void removerfanartlike(){
        DatabaseReference anuncioref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("fanarts-likes")
                .child(getId());

        anuncioref.removeValue();

    }

    public void deletar_img_fanarts(){
        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        StorageReference storageReference = ConfiguracaoFirebase.getFirebaseStorage()
                .child("imagens")
                .child("arts")
                .child(identificadorUsuario);

        storageReference.delete();
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
