package com.example.wassap.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wassap.MainActivity;
import com.example.wassap.R;
import com.example.wassap.modelo.Usuario;
import com.example.wassap.ui.MyUsuariosRecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment implements MyUsuariosRecyclerViewAdapter.OnUsuarioClickListener{
    public ArrayList<Usuario> chats;
    public ChildEventListener listener, listenerUsuarios;
    public DatabaseReference mDatabase;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
            Navigation.findNavController(getView()).navigate(R.id.action_global_login);
        else {
            cargarUsuarios();
            ((MainActivity) getActivity()).fab.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        mDatabase.removeEventListener(listener);
        ((MainActivity) getActivity()).fab.setVisibility(View.INVISIBLE);
    }

    public void cargarUsuarios(){
        DatabaseReference matabase = FirebaseDatabase.getInstance().getReference().child("usuarios");
        ((MainActivity) getActivity()).usuarios = new HashMap();
        matabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot e : children){
                    ((MainActivity) getActivity()).usuarios.put(e.getKey(), e.getValue(Usuario.class));
                }
                cargarMensajes();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void cargarMensajes(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("chat").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        chats = new ArrayList<>();
        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                chats.add((Usuario) ((MainActivity) getActivity()).usuarios.get(dataSnapshot.getKey()));
                recargarLista();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabase.addChildEventListener(listener);
    }
    public void recargarLista(){
        System.out.println("hace recargar Lista");
        Context context = getView().getContext();
        RecyclerView recyclerView = getView().findViewById(R.id.recView_chats);;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new MyUsuariosRecyclerViewAdapter(chats,getContext(),this));
    }

    @Override
    public void irChat(Usuario u) {
        Bundle b =new Bundle();
        b.putSerializable("usuario",u);
        Navigation.findNavController(getView()).navigate(R.id.action_nav_home_to_chatMessageFragment,b);
    }
}