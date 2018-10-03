package com.example.fulanoeciclano.nerdzone.Model;


import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Evento implements Serializable {

    public String uid;
    public String author;
    public String titulo;
    public String subtitulo;
    public String imgperfilusuario;
    public String idUsuario;
    public String capaevento;
    private List<String> fotoseventos;
    public String mensagem;
    public String datainicio;
    public String datafim;
    public String estado;
    public int curtirCount = 0;
    public Map<String, Boolean> curtida = new HashMap<>();



    public Evento() {
        DatabaseReference eventoref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("evento");
        setUid(eventoref.push().getKey());
    }

    public void salvar(){
        String idUsuario = ConfiguracaoFirebase.getIdUsuario();
        DatabaseReference anuncioref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("meusevento");
        anuncioref.child(idUsuario)
                .child(getUid()).setValue(this);

          salvarEventoPublico();
    }

    public void salvarEventoPublico(){
        DatabaseReference anuncioref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("evento");
        anuncioref.child(getEstado())
                .child(getUid()).setValue(this);
    }

    /*public Evento(String uid, String author, String imgperfilusuario,String fotoevento, String titulo,
                  String subtitulo,
                  String mensagem,String datafim,String datainicio,String estado) {
        this.uid = uid;
        this.author = author;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.mensagem = mensagem;
        this.imgperfilusuario= imgperfilusuario;
        this.datainicio = datainicio;
        this.datafim= datafim;
        this.estado= estado;
        this.fotoevento = fotoevento;
    }
*/
    // [START post_to_map]
    /*@Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("imgperfilusuario", imgperfilusuario);
        result.put("fotoevento", fotoevento);
        result.put("titulo", titulo);
        result.put("subtitulo",subtitulo);
        result.put("mensagem", mensagem);
        result.put("datainicio",datainicio);
        result.put("datafim", datafim);
        result.put("estado", estado);
        result.put("starCount", curtirCount);
        result.put("stars", curtida);

        return result;
    }
    */
    // [END post_to_map]


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public String getImgperfilusuario() {
        return imgperfilusuario;
    }

    public void setImgperfilusuario(String imgperfilusuario) {
        this.imgperfilusuario = imgperfilusuario;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCapaevento() {
        return capaevento;
    }

    public void setCapaevento(String capaevento) {
        this.capaevento = capaevento;
    }

    public List<String> getFotoseventos() {
        return fotoseventos;
    }

    public void setFotoseventos(List<String> fotoseventos) {
        this.fotoseventos = fotoseventos;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getDatainicio() {
        return datainicio;
    }

    public void setDatainicio(String datainicio) {
        this.datainicio = datainicio;
    }

    public String getDatafim() {
        return datafim;
    }

    public void setDatafim(String datafim) {
        this.datafim = datafim;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getCurtirCount() {
        return curtirCount;
    }

    public void setCurtirCount(int curtirCount) {
        this.curtirCount = curtirCount;
    }

    public Map<String, Boolean> getCurtida() {
        return curtida;
    }

    public void setCurtida(Map<String, Boolean> curtida) {
        this.curtida = curtida;
    }
}

