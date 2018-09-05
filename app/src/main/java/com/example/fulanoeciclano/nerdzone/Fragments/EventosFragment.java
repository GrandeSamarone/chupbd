package com.example.fulanoeciclano.nerdzone.Fragments;


import android.support.v4.app.Fragment;

import com.example.fulanoeciclano.nerdzone.Evento.EventoListFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventosFragment extends EventoListFragment {


    public EventosFragment() {
        // Required empty public constructor
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentPostsQuery = databaseReference.child("evento")
                .limitToFirst(100);
        // [END recent_posts_query]

        return recentPostsQuery;
    }



}
