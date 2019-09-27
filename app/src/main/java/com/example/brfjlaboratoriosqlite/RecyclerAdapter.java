package com.example.brfjlaboratoriosqlite;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private String[] clave;
    String[] nombresito;
    String[] numCel;
    String[] correo;
    String[] carrera;
    private Context context;

    public RecyclerAdapter(String[] clave, String[] nombresito, String[] numCel,String[] correo,String[] carrera, Context con) {
        this.clave = clave;
        this.nombresito = nombresito;
        this.numCel = numCel;
        this.correo = correo;
        this.carrera = carrera;
        this.context = con;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder,final int position) {
        holder.clave.setText(clave[position]);
        holder.nombresito.setText(nombresito[position]);
        holder.numCel.setText(numCel[position]);
        holder.correo.setText(correo[position]);
        holder.carrera.setText(carrera[position]);

    }

    @Override
    public int getItemCount() {
        return nombresito.length;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView clave;
        TextView nombresito;
        TextView numCel,correo,carrera;
        public RecyclerViewHolder(View itemView) {
            super(itemView);

            clave = itemView.findViewById(R.id.idClaveControl);
            nombresito = itemView.findViewById(R.id.idNombre);
            numCel = itemView.findViewById(R.id.idCelular);
            correo = itemView.findViewById(R.id.idCorreo);
            carrera = itemView.findViewById(R.id.idCarrera);
        }
    }
}
