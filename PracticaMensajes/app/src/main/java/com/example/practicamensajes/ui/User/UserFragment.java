package com.example.practicamensajes.ui.User;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.practicamensajes.MainActivity;
import com.example.practicamensajes.R;
import com.example.practicamensajes.modelo.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;

public class UserFragment extends Fragment {

    private EditText nombre,telefono;
    private TextView proveedor;
    private ImageView foto;
    private Usuario usuario;
    private Button actualizar;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nombre = this.getActivity().findViewById(R.id.editTextUser);
        telefono = this.getActivity().findViewById(R.id.editTextPhone);
        foto = this.getActivity().findViewById(R.id.imageViewUsuario);
        actualizar = this.getActivity().findViewById(R.id.b_updateUser);
        proveedor = this.getActivity().findViewById(R.id.textViewTipoCuenta);
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarUser();
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
            Navigation.findNavController(getView()).navigate(R.id.action_global_login);
        else {
            usuario = ((MainActivity) getActivity()).user;
            nombre.setText(usuario.getNombre());
            telefono.setText(usuario.getTelefono());
            proveedor.setText(usuario.getProveedor());
            if (usuario.getFoto() != null)
                Glide.with(getContext())
                        .load(usuario.getFoto())
                        .into(foto);
        }
    }
    public void actualizarUser(){
        final String usern = nombre.getText().toString();
        final String movil = telefono.getText().toString();
        if(usern.isEmpty()){
            Toast.makeText(getContext(),getResources().getText(R.string.error_username),Toast.LENGTH_SHORT).show();
        } else {
            new AlertDialog.Builder(getContext())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.update_user)
                    .setMessage(R.string.confirmar_update)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            usuario.setNombre(usern);
                            usuario.setTelefono(movil);
                            FirebaseDatabase.getInstance().getReference("usuarios").child(usuario.getUid()).setValue(usuario);
                        }

                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        }
    }
}