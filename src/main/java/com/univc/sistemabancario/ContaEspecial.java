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

public class ContaEspecial extends Conta{

    @Getter
    @Setter
    private double limite;

    public ContaEspecial(int id_conta, String titular, double saldo, TipoDTO tipo, double limite){
        super(id_conta, titular, saldo, tipo);
        this.limite = limite;
    }
    public ContaEspecial(int id_conta, String titular, double saldo, double limite){
        super(id_conta, titular, saldo);
        this.limite = limite;
    }

    @Override
    public boolean sacar(double quantidade) throws Exception {
        double saldo = this.getSaldo();
        if (quantidade < 0.01){
            throw new Exception("Valor inválido.");
        }
        if (saldo + this.limite >= quantidade) {
            this.setSaldo(saldo - quantidade);
            if (quantidade > saldo){
                this.limite += this.getSaldo();
            }
            return true;
        }
        throw new Exception(
                "Saldo insuficiente!" +
                "\nSaldo: R$" + String.format("%.2f",this.getSaldo()) + " Limite disponivel: R$" + String.format("%.2f",this.getLimite()) +
                "\nSaque: R$" + String.format("%.2f",quantidade) +
                "\nFaltam: R$" + String.format("%.2f",-(saldo - (quantidade - this.getLimite())))
        );
    }
    public void depositar(double quantidade) throws Exception{
        if (quantidade <= 0){
            throw new Exception("Valor inválido.");
        }
        this.setSaldo(this.getSaldo() + quantidade);
    }
}
