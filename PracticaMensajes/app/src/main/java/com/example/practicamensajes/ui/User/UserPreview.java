package com.example.practicamensajes.ui.User;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.practicamensajes.MainActivity;
import com.example.practicamensajes.R;
import com.example.practicamensajes.modelo.Usuario;
import com.google.firebase.auth.FirebaseAuth;

public class UserPreview extends Fragment {
    private EditText nombre,telefono;
    private TextView proveedor;
    private ImageView foto;
    private Usuario usuario;
    private Button actualizar;
    public UserPreview() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usuario = (Usuario) getArguments().get("usuario");
        nombre = this.getActivity().findViewById(R.id.editTextUser);
        telefono = this.getActivity().findViewById(R.id.editTextPhone);
        foto = this.getActivity().findViewById(R.id.imageViewUsuario);
        actualizar = this.getActivity().findViewById(R.id.b_updateUser);
        proveedor = this.getActivity().findViewById(R.id.textViewTipoCuenta);
        actualizar.setVisibility(View.INVISIBLE);
    }
    public void onStart(){
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
            Navigation.findNavController(getView()).navigate(R.id.action_global_login);
        else {
            nombre.setText(usuario.getNombre());
            nombre.setKeyListener(null);
            telefono.setText(usuario.getTelefono());
            telefono.setKeyListener(null);
            telefono.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (usuario.getTelefono() != null) {
                        if (!usuario.getTelefono().isEmpty()) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + usuario.getTelefono()));
                            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                                startActivity(intent);
                            }
                        }
                    }
                }
            });
            proveedor.setText(usuario.getProveedor());
            if (usuario.getFoto() != null)
                Glide.with(getContext())
                        .load(usuario.getFoto())
                        .into(foto);
        }
    }
}