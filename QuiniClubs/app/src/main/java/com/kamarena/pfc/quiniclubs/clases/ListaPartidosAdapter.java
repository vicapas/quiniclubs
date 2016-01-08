package com.kamarena.pfc.quiniclubs.clases;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kamarena.pfc.quiniclubs.R;

import java.util.ArrayList;


/**
 * ArrayAdapter para gestionar la lista de los partidos que se juegan en una jornada
 *
 * Created by kamarena on 20/05/2015.
 */
public class ListaPartidosAdapter extends ArrayAdapter<Partido> {

    private Context context;
    private ArrayList<Partido> datos;

    public ListaPartidosAdapter(Context context, ArrayList<Partido> datos) {
        super(context, R.layout.partido_lista, datos);
        this.context = context;
        this.datos = datos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Se crea el LayoutInflater para obtener cada uno de los items que se añaden a la lista
        LayoutInflater inflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.partido_lista, null);

        final Partido i = datos.get(position);

        if (i != null) {
            // Modificamos cada control de la vista ya correctamente configurada
            RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.lay_lista_partidos);
            TextView nombrePartido = (TextView) convertView.findViewById(R.id.lb_lista_nombre_partido);
            ImageView escudoLocal = (ImageView) convertView.findViewById(R.id.iv_escudo_local);
            ImageView escudoVisitante = (ImageView) convertView.findViewById(R.id.iv_escudo_visitante);
            CheckBox pronostico1 = (CheckBox) convertView.findViewById(R.id.cb_pronostico_1);
            CheckBox pronosticoX = (CheckBox) convertView.findViewById(R.id.cb_pronostico_X);
            CheckBox pronostico2 = (CheckBox) convertView.findViewById(R.id.cb_pronostico_2);

            // Establezco color de fondo alternado
            if (position % 2 == 0) {
                layout.setBackgroundColor(Color.parseColor("#F0F5E6"));
            }

            nombrePartido.setText(datos.get(position).getNombrePartido());
            this.getEscudo("ic_escudo_" + datos.get(position).getEscudoLocal(), escudoLocal);
            this.getEscudo("ic_escudo_" + datos.get(position).getEscudoVisitante(), escudoVisitante);
            pronostico1.setChecked(i.isCb1());
            pronosticoX.setChecked(i.isCbX());
            pronostico2.setChecked(i.isCb2());
            pronostico1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    i.setCb1(isChecked);
                    i.setCbX(false);
                    i.setCb2(false);
                    ListaPartidosAdapter.this.notifyDataSetChanged();
                }
            });
            pronosticoX.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    i.setCbX(isChecked);
                    i.setCb1(false);
                    i.setCb2(false);
                    ListaPartidosAdapter.this.notifyDataSetChanged();
                }
            });
            pronostico2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    i.setCb2(isChecked);
                    i.setCb1(false);
                    i.setCbX(false);
                    ListaPartidosAdapter.this.notifyDataSetChanged();
                }
            });
        }

        return convertView;
    }

    /**
     * Método que al pasarle el nombre del escudo que se encuentra en drawable establece esa imagen
     * a la ImageView que se pasa por parámetro
     *
     * @param name String
     * @param imageView ImageView
     */
    private void getEscudo(String name, ImageView imageView) {
        Resources res = context.getResources();
        int resID = res.getIdentifier(name , "drawable", context.getPackageName());
        Drawable drawable = res.getDrawable(resID);
        imageView.setImageDrawable(drawable);
    }

}
