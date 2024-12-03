package com.univc.sistemabancario.connection.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.univc.sistemabancario.entity.TipoEntity;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContaDTO {
    private int id_conta;
    private String titular;
    private TipoDTO id_tipo;
    private double saldo;
    private Double limite;
    private Double taxa;
    private Double rendimento;

    public int getId_conta() {
        return id_conta;
    }

    public void setId_conta(int id_conta) {
        this.id_conta = id_conta;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public TipoDTO getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(TipoDTO id_tipo) {
        this.id_tipo = id_tipo;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Double getLimite() {
        return limite;
    }

    public void setLimite(Double limite) {
        this.limite = limite;
    }

    public Double getTaxa() {
        return taxa;
    }

    public void setTaxa(Double taxa) {
        this.taxa = taxa;
    }

    public Double getRendimento() {
        return rendimento;
    }

    public void setRendimento(Double rendimento) {
        this.rendimento = rendimento;
    }

    @Override
    public String toString() {
        return "{" +
                "id_conta=" + id_conta +
                ", titular='" + titular + '\'' +
                ", id_tipo=" + id_tipo +
                ", saldo=" + saldo +
                ", limite=" + limite +
                ", taxa=" + taxa +
                ", rendimento=" + rendimento +
                '}';
    }
}
