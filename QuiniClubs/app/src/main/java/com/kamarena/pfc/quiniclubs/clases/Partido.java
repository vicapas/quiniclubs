package com.kamarena.pfc.quiniclubs.clases;


/**
 * Created by kamarena on 20/05/2015.
 */
public class Partido {

    private String idJornada;
    private String idPartido;
    private String nombrePartido;
    private String resultado;
    private String fechaPartido;
    private String numeroPartido;
    private String escudoLocal;
    private String escudoVisitante;
    private String equipoLocal;
    private String equipoVisitante;
    private String nombreJornada;
    private String inicioJornada;
    private String finalJornada;
    private String pronostico;

    // Añadimos tres campos que corresponden a los tres checkbox que pueden ser marcados
    private boolean cb1;
    private boolean cbX;
    private boolean cb2;

    public Partido(String idJornada, String idPartido, String nombrePartido, String resultado,
                   String fechaPartido, String numeroPartido, String escudoLocal, String escudoVisitante,
                   String equipoLocal, String equipoVisitante, String nombreJornada,
                   String inicioJornada, String finalJoranada) {

        this.idJornada = idJornada;
        this.idPartido = idPartido;
        this.nombrePartido = nombrePartido;
        this.resultado = resultado;
        this.fechaPartido = fechaPartido;
        this.numeroPartido = numeroPartido;
        this.escudoLocal = escudoLocal;
        this.escudoVisitante = escudoVisitante;
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.nombreJornada = nombreJornada;
        this.inicioJornada = inicioJornada;
        this.finalJornada = finalJoranada;

        this.cb1 = false;
        this.cbX = false;
        this.cb2 = false;
    }

    public String getIdJornada() {
        return idJornada;
    }

    public void setIdJornada(String idJornada) {
        this.idJornada = idJornada;
    }

    public String getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(String idPartido) {
        this.idPartido = idPartido;
    }

    public String getNombrePartido() {
        return nombrePartido;
    }

    public void setNombrePartido(String nombrePartido) {
        this.nombrePartido = nombrePartido;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getFechaPartido() {
        return fechaPartido;
    }

    public void setFechaPartido(String fechaPartido) {
        this.fechaPartido = fechaPartido;
    }

    public String getNumeroPartido() {
        return numeroPartido;
    }

    public void setNumeroPartido(String numeroPartido) {
        this.numeroPartido = numeroPartido;
    }

    public String getEscudoLocal() {
        return escudoLocal;
    }

    public void setEscudoLocal(String escudoLocal) {
        this.escudoLocal = escudoLocal;
    }

    public String getEscudoVisitante() {
        return escudoVisitante;
    }

    public void setEscudoVisitante(String escudoVisitante) {
        this.escudoVisitante = escudoVisitante;
    }

    public String getEquipoLocal() {
        return equipoLocal;
    }

    public void setEquipoLocal(String equipoLocal) {
        this.equipoLocal = equipoLocal;
    }

    public String getEquipoVisitante() {
        return equipoVisitante;
    }

    public void setEquipoVisitante(String equipoVisitante) {
        this.equipoVisitante = equipoVisitante;
    }

    public String getNombreJornada() {
        return nombreJornada;
    }

    public void setNombreJornada(String nombreJornada) {
        this.nombreJornada = nombreJornada;
    }

    public String getInicioJornada() {
        return inicioJornada;
    }

    public void setInicioJornada(String inicioJornada) {
        this.inicioJornada = inicioJornada;
    }

    public String getFinalJornada() {
        return finalJornada;
    }

    public void setFinalJornada(String finalJornada) {
        this.finalJornada = finalJornada;
    }

    public boolean isCb1() {
        return cb1;
    }

    public void setCb1(boolean cb1) {
        this.cb1 = cb1;
    }

    public boolean isCbX() {
        return cbX;
    }

    public void setCbX(boolean cbX) {
        this.cbX = cbX;
    }

    public boolean isCb2() {
        return cb2;
    }

    public void setCb2(boolean cb2) {
        this.cb2 = cb2;
    }

    public String getPronostico() {
        return pronostico;
    }

    public void setPronostico(String pronostico) {
        this.pronostico = pronostico;
    }
}
