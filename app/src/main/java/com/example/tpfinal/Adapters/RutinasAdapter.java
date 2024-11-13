package com.example.tpfinal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.tpfinal.ActivityRutinaSeleccionada;
import com.example.tpfinal.Conexion.ConexionRutinas;
import com.example.tpfinal.Entidades.ConfiguracionEjercicio;
import com.example.tpfinal.Entidades.Rutina;
import com.example.tpfinal.R;

import java.util.ArrayList;
import java.util.List;

public class RutinasAdapter extends ArrayAdapter<Rutina> {

    public RutinasAdapter(Context context, List<Rutina> objects) {
        super(context, R.layout.rutinas_propias_list, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.rutinas_propias_list, parent, false);
        }

        Button btnSelectRutina = (Button) convertView.findViewById(R.id.btnMostrarRutinaPropiaLista);

        Rutina rutina = getItem(position);

        if (rutina != null) {
            btnSelectRutina.setText(rutina.getNombre());

            btnSelectRutina.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String rutinaId = String.valueOf(rutina.getId());

                    ConexionRutinas conexionRutinas = new ConexionRutinas(getContext());
                    conexionRutinas.getEjerciciosConfiguracionPropios(rutinaId, new ConexionRutinas.RutinaCallback<List<ConfiguracionEjercicio>>() {
                        @Override
                        public void onComplete(List<ConfiguracionEjercicio> configuracionEjercicioList) {

                            Intent intent = new Intent(getContext(), ActivityRutinaSeleccionada.class);
                            intent.putParcelableArrayListExtra("configuracionEjercicioList", new ArrayList<>(configuracionEjercicioList));
                            getContext().startActivity(intent);
                        }
                    });
                }
            });
        }

        return convertView;
    }

}
