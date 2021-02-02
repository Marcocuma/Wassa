package com.example.wassap.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wassap.R;
import com.example.wassap.modelo.Mensaje;
import com.example.wassap.modelo.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatMessageFragment extends Fragment {

    private Usuario user;
    private Button b;
    private EditText mensaje;
    private TextView nombre;
    private ImageView foto;
    public ChildEventListener listener;
    public DatabaseReference mDatabase;
    public ArrayList<Mensaje> mensajes;
    public ChatMessageFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_list, container, false);

        b = view.findViewById(R.id.button_enviar);
        mensaje = view.findViewById(R.id.editTextTextChatMensaje);
        nombre = view.findViewById(R.id.textViewChat);
        foto = view.findViewById(R.id.imageViewChat);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensaje();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = (Usuario) getArguments().get("usuario");
        nombre.setText(user.getNombre());
        if(user.getFoto() != null)
            Glide.with(getContext())
                    .load(user.getFoto())
                    .into(foto);
        nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b =new Bundle();
                b.putSerializable("usuario",user);
                Navigation.findNavController(getView()).navigate(R.id.action_chatMessageFragment_to_userPreview,b);
            }
        });
    }
    public void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
            Navigation.findNavController(getView()).navigate(R.id.action_global_login);
        else
            cargarMensajes();
    }
    @Override
    public void onStop() {
        super.onStop();
        System.out.println("hace stop");
        mDatabase.removeEventListener(listener);
    }
    public void enviarMensaje(){
        ArrayList<Mensaje> mensajeAux = new ArrayList<>(mensajes);
        if(!mensaje.getText().toString().isEmpty()){
            final Mensaje m = new Mensaje(mensaje.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), user.getUid());
            //Añade mensaje al la lista del usuario que lo envia
            mensajeAux.add(m);
            FirebaseDatabase.getInstance().getReference("chat").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(user.getUid()).setValue(mensajeAux);
            //Añade el mensaje a la lista del receptor
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("chat").child(user.getUid()).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            final ArrayList<Mensaje> mensajesReceptor = new ArrayList<>();
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot e: dataSnapshot.getChildren()){
                        mensajesReceptor.add(e.getValue(Mensaje.class));
                    }
                    mensajesReceptor.add(m);
                    FirebaseDatabase.getInstance().getReference().child("chat").child(user.getUid()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(mensajesReceptor);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        mensaje.setText("");
    }
    public void cargarMensajes(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("chat").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(user.getUid());
        mensajes = new ArrayList<>();
        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(mensaje.length() != 0)
                    System.out.println(mensajes.get(mensajes.size()-1).getMensaje());
                mensajes.add(dataSnapshot.getValue(Mensaje.class));
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
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewElemento);;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new MyChatMessageRecyclerViewAdapter(mensajes,getContext()));
        recyclerView.scrollToPosition(mensajes.size() - 1);
    }
}