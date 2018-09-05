package com.example.fulanoeciclano.nerdzone.Model;



import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Noticia {

    public String uid;
    public String author;
    public String titulo;
    public String foto;
    public String mensagem;
    public int starCount = 0;
    public  int quantcomentario=0;
    public Map<String, Boolean> stars = new HashMap<>();

    public Noticia() {
        // Default constructor required for calls to DataSnapshot.getValue(Noticia.class)
    }

    public Noticia(String uid, String author, String foto, String titulo, String mensagem) {
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
    // [END post_to_map]

}
// [END post_class]
