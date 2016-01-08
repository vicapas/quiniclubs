package com.kamarena.pfc.quiniclubs.clases;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kamarena.pfc.quiniclubs.R;

import java.util.ArrayList;

/**
 * Created by kamarena on 31/05/2015.
 */
public class ListaQuinielaAdapter extends ArrayAdapter<Partido> {

    private Context context;
    private ArrayList<Partido> datos;

    public ListaQuinielaAdapter(Context context, ArrayList<Partido> datos) {
        super(context, R.layout.quiniela_lista, datos);
        this.context = context;
        this.datos = datos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.quiniela_lista, null);

        final Partido i = datos.get(position);

        if (i != null) {
            LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.lay_lista_quiniela);
            TextView numeroPartido = (TextView) convertView.findViewById(R.id.tv_numero_partido);
            ImageView escudoLocal = (ImageView) convertView.findViewById(R.id.iv_escudo_local_quiniela);
            ImageView escudoVisitante = (ImageView) convertView.findViewById(R.id.iv_escudo_visitante_quiniela);
            TextView nombrePartido = (TextView) convertView.findViewById(R.id.tv_nombre_partido_quiniela);
            TextView apuesta = (TextView) convertView.findViewById(R.id.tv_quiniela_apuesta);

            // Establezco color de fondo alternado
            if (position % 2 == 0) {
                layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }

            numeroPartido.setText(i.getNumeroPartido());
            this.getEscudo("ic_escudo_" + i.getEscudoLocal(), escudoLocal);
            this.getEscudo("ic_escudo_" + i.getEscudoVisitante(), escudoVisitante);
            nombrePartido.setText(i.getNombrePartido());
            apuesta.setText(i.getPronostico());
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
