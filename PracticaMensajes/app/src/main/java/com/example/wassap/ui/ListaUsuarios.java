package com.example.wassap.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wassap.MainActivity;
import com.example.wassap.R;
import com.example.wassap.modelo.Usuario;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;

public class ListaUsuarios extends Fragment implements MyUsuariosRecyclerViewAdapter.OnUsuarioClickListener {


    public ListaUsuarios() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_usuarios_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            HashMap user = (HashMap) ((MainActivity)getActivity()).usuarios.clone();
            user.remove(((MainActivity)getActivity()).user.getUid());
            recyclerView.setAdapter(new MyUsuariosRecyclerViewAdapter(new ArrayList<Usuario>(user.values()),getContext(),this));
        }
        return view;
    }
    @Override
    public void onStart(){
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
            Navigation.findNavController(getView()).navigate(R.id.action_global_login);
    }
    @Override
    public void irChat(Usuario u) {
        Bundle b =new Bundle();
        b.putSerializable("usuario",u);
        Navigation.findNavController(getView()).navigate(R.id.action_listaUsuarios_to_chatMessageFragment,b);
    }
}