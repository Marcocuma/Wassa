package com.example.wassap.modelo;

public class Mensaje {
    String mensaje, origen, destino;

    public Mensaje(String mensaje, String origen, String destino) {
        this.mensaje = mensaje;
        this.origen = origen;
        this.destino = destino;
    }
    public Mensaje(){}

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }
}
