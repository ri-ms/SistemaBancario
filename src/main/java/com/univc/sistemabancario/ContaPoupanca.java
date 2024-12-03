/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.univc.sistemabancario;

import com.univc.sistemabancario.connection.DTO.TipoDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author UVC
 */

public class ContaPoupanca extends Conta{

    @Getter
    @Setter
    private double rendimento;

    public ContaPoupanca(int id_conta, String titular, double saldo, TipoDTO tipo, double rendimento){
        super(id_conta, titular, saldo, tipo);
        this.rendimento = rendimento/100;
    }
    public ContaPoupanca(int id_conta, String titular, double saldo, double rendimento){
        super(id_conta, titular, saldo);
        this.rendimento = rendimento/100;
    }

    public double realizarRendimento(){
        double saldo = this.getSaldo();
        this.setSaldo(saldo + (saldo*this.rendimento));
        return (saldo*this.rendimento);
    }

    public void depositar(double quantidade) throws Exception{
        if (quantidade <= 0){
            throw new Exception("Valor inválido.");
        }
        this.setSaldo(this.getSaldo() + quantidade);
    }

    public boolean sacar(double quantidade) throws Exception {

        if (quantidade < 0.01){
            throw new Exception("Valor inválido.");
        }
        if (this.getSaldo() >= quantidade) {
            this.setSaldo(this.getSaldo() - quantidade);
            return true;
        }
        throw new Exception(
                "Saldo insuficiente!" +
                        "\nSaldo: R$" + String.format("%.2f",this.getSaldo()) +
                        "\nSaque: R$" + String.format("%.2f",quantidade) +
                        "\nFaltam: R$" + String.format("%.2f",-(this.getSaldo() - quantidade))
        );

    }
}
