package com.univc.sistemabancario.connection.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TipoDTO {
    private short id_tipo;
    private String tipo;

    public short getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(short id) {
        this.id_tipo = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "{" +
                "id_tipo=" + id_tipo +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
