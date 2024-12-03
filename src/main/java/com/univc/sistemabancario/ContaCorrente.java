/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.univc.sistemabancario;

import com.univc.sistemabancario.connection.DTO.TipoDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author UVC
 */

public class ContaCorrente extends Conta{

    @Getter
    @Setter
    private double taxa;

    public ContaCorrente(int id_conta, String titular, double saldo, TipoDTO tipo, double taxa){
        super(id_conta, titular, saldo, tipo);
        this.taxa = taxa/100;
    }
    public ContaCorrente(int id_conta, String titular, double saldo,double taxa){
        super(id_conta, titular, saldo);
        this.taxa = taxa/100;
    }

    @Override
    public boolean sacar(double quantidade) throws Exception {
        double saldo = this.getSaldo();
        double taxa = this.getTaxa();
        if (quantidade < 0.01){
            throw new Exception("Valor inválido.");
        }

        if (saldo >= (quantidade + (quantidade * taxa))){
            this.setSaldo(saldo - (quantidade + (quantidade * taxa)));
            return true;
        }
        throw new Exception(
            "Saldo insuficiente!" +
            "\nSaldo: R$" + String.format("%.2f", this.getSaldo()) +
            "\nSaque: R$" + String.format("%.2f", quantidade) + " + Taxa: R$" + String.format("%.2f", quantidade * taxa) +
            "\nFaltam: R$" + String.format("%.2f", -(saldo - (quantidade + (quantidade * taxa))))
            );
    }

    public void depositar(double quantidade) throws Exception{
        if (quantidade <= 0){
            throw new Exception("Valor inválido.");
        }
        this.setSaldo(this.getSaldo() + quantidade);
    }
}
