package com.kamarena.pfc.quiniclubs.clases;

import java.util.ArrayList;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kamarena.pfc.quiniclubs.R;
import com.kamarena.pfc.quiniclubs.clases.ItemNavegador;

/**
 * Created by kamarena on 17/05/2015
 */

public class NavigationAdapter extends BaseAdapter {

    private Activity activity;
    ArrayList<ItemNavegador> arrayitms;

    public NavigationAdapter(Activity activity,ArrayList<ItemNavegador>  listarry) {
        super();
        this.activity = activity;
        this.arrayitms=listarry;
    }
    //Retorna objeto Item_objct del array list
    @Override
    public Object getItem(int position) {
        return arrayitms.get(position);
    }
    public int getCount() {
        return arrayitms.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    //Declaramos clase estatica la cual representa a la fila
    public static class Fila
    {
        TextView titulo_itm;
        ImageView icono;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        Fila view;
        LayoutInflater inflator = activity.getLayoutInflater();
        if(convertView==null)
        {
            view = new Fila();
            //Creo objeto item y lo obtengo del array
            ItemNavegador itm=arrayitms.get(position);
            convertView = inflator.inflate(R.layout.item_navigation, null);
            //Titulo
            view.titulo_itm = (TextView) convertView.findViewById(R.id.name_title);
            //Seteo en el campo titulo el nombre correspondiente obtenido del objeto
            view.titulo_itm.setText(itm.getTitulos());
            //Icono
            view.icono = (ImageView) convertView.findViewById(R.id.icon);
            //Seteo el icono
            view.icono.setImageResource(itm.getIcono());
            convertView.setTag(view);
        }
        else
        {
            view = (Fila) convertView.getTag();
        }
        return convertView;
    }

}
