package com.example.practicamensajes.ui;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.practicamensajes.MainActivity;
import com.example.practicamensajes.R;
import com.example.practicamensajes.modelo.Usuario;

import java.util.List;

public class MyUsuariosRecyclerViewAdapter extends RecyclerView.Adapter<MyUsuariosRecyclerViewAdapter.ViewHolder> {

    private final List<Usuario> mValues;
    private final Context context;
    private final OnUsuarioClickListener listener;

    public MyUsuariosRecyclerViewAdapter(List<Usuario> items, Context contexto, OnUsuarioClickListener listener2) {
        mValues = items;
        context = contexto;
        listener = listener2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_lista_usuarios, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(mValues.get(position).getFoto() != null)
            Glide.with(context)
                    .load(mValues.get(position).getFoto())
                    .into(holder.mImagen);
        holder.nombre.setText(mValues.get(position).getNombre());
        holder.user = mValues.get(position);
    }
    public interface OnUsuarioClickListener {
        public void irChat(Usuario u);
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImagen;
        public final TextView nombre;
        public Usuario user;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImagen = (ImageView) view.findViewById(R.id.imageViewUsuarioLista);
            nombre = (TextView) view.findViewById(R.id.textViewNombreLista);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.irChat(user);
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nombre.getText() + "'";
        }
    }
}