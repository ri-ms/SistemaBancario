/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.univc.sistemabancario;

import com.univc.sistemabancario.connection.DTO.TipoDTO;
import lombok.*;
import lombok.experimental.Delegate;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author UVC
 */

@Data
public abstract class Conta {

    private int id_conta;

    private String titular;

    private double saldo;

    private TipoDTO tipo;

    public Conta(int id_conta, String titular, double saldo, TipoDTO tipo){
        this.id_conta = id_conta;
        this.titular = titular;
        this.saldo = saldo;
        this.tipo = tipo;
    }
    public Conta(int id_conta, String titular, double saldo){
        this.id_conta = id_conta;
        this.titular = titular;
        this.saldo = saldo;
    }

    public TipoDTO getTipo() {
        return tipo;
    }

    public void setTipo(TipoDTO tipo) {
        this.tipo = tipo;
    }
    public abstract boolean sacar(double quantidade) throws Exception;

    public abstract void depositar(double quantidade) throws Exception;
}
