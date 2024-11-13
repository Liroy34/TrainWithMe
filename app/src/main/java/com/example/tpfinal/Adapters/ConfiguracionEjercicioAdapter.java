package com.example.tpfinal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tpfinal.Entidades.ConfiguracionEjercicio;
import com.example.tpfinal.R;

import java.util.List;

public class ConfiguracionEjercicioAdapter extends ArrayAdapter<ConfiguracionEjercicio> {

    public ConfiguracionEjercicioAdapter(Context context, List<ConfiguracionEjercicio> objects) {
        super(context, R.layout.rutina_seleccionada_lista, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.rutina_seleccionada_lista, parent, false);
        }

        ConfiguracionEjercicio configuracionEjercicio = getItem(position);

        TextView nombreEjercicio = convertView.findViewById(R.id.txtEjercicioRutinaPropia);
        TextView series = convertView.findViewById(R.id.txtRepesRutinaPropia);


        if (configuracionEjercicio != null) {
            nombreEjercicio.setText(configuracionEjercicio.getEjercicio().getNombre());
            series.setText("Series: " + configuracionEjercicio.getSeries());

        }

        return convertView;
    }
}
