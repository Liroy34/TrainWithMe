package com.example.tpfinal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.tpfinal.Entidades.Entrenamiento;
import com.example.tpfinal.R;

import java.util.List;

public class EntrenamientoAdapter extends BaseAdapter {
    private Context context;
    private List<Entrenamiento> entrenamientos;

    public EntrenamientoAdapter(Context context, List<Entrenamiento> entrenamientos) {
        this.context = context;
        this.entrenamientos = entrenamientos;
    }

    @Override
    public int getCount() {
        return entrenamientos.size();
    }

    @Override
    public Object getItem(int position) {
        return entrenamientos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_entrenamiento, parent, false);
        }

        TextView fechaTextView = convertView.findViewById(R.id.txtFechaEntrenamiento);
        TextView duracionTextView = convertView.findViewById(R.id.txtDuracionEntrenamiento);

        Entrenamiento entrenamiento = entrenamientos.get(position);
        fechaTextView.setText("Fecha: " + entrenamiento.getFecha());
        duracionTextView.setText("Duración: " + entrenamiento.getDuracion());

        return convertView;
    }
}
