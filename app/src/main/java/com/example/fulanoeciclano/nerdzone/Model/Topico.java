package com.example.fulanoeciclano.nerdzone.Model;



import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Topico {

    public String uid;
    public String author;
    public String titulo;
    public String foto;
    public String mensagem;
    public int starCount = 0;
    public  int quantcomentario=0;
    public Map<String, Boolean> stars = new HashMap<>();

    public Topico() {
        DatabaseReference eventoref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("topico");
        setUid(eventoref.push().getKey());  }

   /* public Topico(String uid, String author, String foto, String titulo, String mensagem) {
        this.uid = uid;
        this.author = author;
        this.titulo = titulo;
        this.mensagem = mensagem;
       this.foto= foto;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("foto", foto);
        result.put("titulo", titulo);
        result.put("mensagem", mensagem);
        result.put("starCount", starCount);
        result.put("totalcomentario",quantcomentario);
        result.put("stars", stars);

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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public int getStarCount() {
        return starCount;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public int getQuantcomentario() {
        return quantcomentario;
    }

    public void setQuantcomentario(int quantcomentario) {
        this.quantcomentario = quantcomentario;
    }

    public Map<String, Boolean> getStars() {
        return stars;
    }

    public void setStars(Map<String, Boolean> stars) {
        this.stars = stars;
    }
}
// [END post_class]
