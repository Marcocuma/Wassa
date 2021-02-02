package com.example.wassap.ui;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wassap.R;
import com.example.wassap.modelo.Mensaje;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MyChatMessageRecyclerViewAdapter extends RecyclerView.Adapter<MyChatMessageRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Mensaje> mValues;
    private Context context;

    public MyChatMessageRecyclerViewAdapter(ArrayList<Mensaje> items, Context contexto) {
        mValues = items;
        context = contexto;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mensaje.setText(mValues.get(position).getMensaje());
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(mValues.get(position).getOrigen())) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.colorMensajeEnvio));
        }else{
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.colorMensajeDestinatario));
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mensaje;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mensaje = (TextView) view.findViewById(R.id.textViewTextoMensaje);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mensaje.getText() + "'";
        }
    }
}