package com.kamarena.pfc.quiniclubs.clases;

/**
 * Created by kamarena on 28/05/2015.
 */
public class ItemNavegador {
    private String titulos;
    private int icono;

    public ItemNavegador(String titulos, int icono) {
        this.titulos = titulos;
        this.icono = icono;
    }

    public String getTitulos() {
        return titulos;
    }

    public void setTitulos(String titulo) {
        this.titulos = titulo;
    }

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }
}
