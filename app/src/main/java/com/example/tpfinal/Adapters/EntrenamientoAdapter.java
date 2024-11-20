package com.example.tpfinal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.tpfinal.Entidades.Entrenamiento;
import com.example.tpfinal.Entidades.Rutina;
import com.example.tpfinal.R;

import org.w3c.dom.Text;

import java.util.List;

public class EntrenamientoAdapter extends ArrayAdapter<Entrenamiento> {

    public EntrenamientoAdapter(Context context, List<Entrenamiento> objects) {
        super(context, R.layout.entrenamientos_lista, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.entrenamientos_lista, parent, false);
        }

        TextView txtNombre = convertView.findViewById(R.id.tvEntrenamientosEnListaNombre);
        TextView txtFecha = convertView.findViewById(R.id.tvEntrenamientosEnListaFecha);
        TextView txtDuracion= convertView.findViewById(R.id.tvEntrenamientosEnListaDuracion);

        txtNombre.setText(getItem(position).getNombre());
        txtFecha.setText(getItem(position).getFecha());
        txtDuracion.setText(getItem(position).getDuracion() + " minutos");

        return convertView;
    }
}
