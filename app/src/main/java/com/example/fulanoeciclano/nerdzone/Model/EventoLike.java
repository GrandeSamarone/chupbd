package com.example.fulanoeciclano.nerdzone.Model;

import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

public class EventoLike {

    public Evento evento;
    public Usuario usuario;
    public int qtdlikes = 0;

    public EventoLike() {

    }

    public void Salvar(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();

        HashMap<String,Object> dadosusuario = new HashMap<>();
        dadosusuario.put("nomeUsuario",usuario.getNome());
        dadosusuario.put("foto",usuario.getFoto());

        DatabaseReference pLikeRef=firebaseRef
                .child("evento-likes")
                .child(evento.getUid())
                .child(usuario.getId());
        pLikeRef.setValue(dadosusuario);

        //Atualizar quantidade de like
        atualizarQtd(1);
    }

      public void atualizarQtd(int valor){
          DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
          DatabaseReference pLikeRef=firebaseRef
                  .child("evento-likes")
                  .child(evento.getUid())
                  .child("qtdlikes");

          setQtdlikes(getQtdlikes()+valor);
          pLikeRef.setValue(getQtdlikes());
    }
    public  void removerlike(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();

        DatabaseReference pLikeRef=firebaseRef
                .child("evento-likes")
                .child(evento.getUid())
                .child(usuario.getId());
        pLikeRef.removeValue();
        //Atualizar quantidade de like
        atualizarQtd(-1);

    }

    public int getQtdlikes() {
        return qtdlikes;
    }

    public void setQtdlikes(int qtdlikes) {
        this.qtdlikes = qtdlikes;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
