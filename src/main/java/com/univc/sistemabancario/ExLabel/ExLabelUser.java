/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.univc.sistemabancario.ExLabel;

import com.univc.sistemabancario.*;
import com.univc.sistemabancario.connection.APIConnection;
import com.univc.sistemabancario.connection.DTO.ContaDTO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Optional;
import javax.swing.*;

/**
 *
 * @author UVC
 */

public class ExLabelUser extends JFrame{
    APIConnection connection = new APIConnection();
    ExLabelSistema sys = new ExLabelSistema();
    private Conta conta;

    //Menu de seleção
    private JLabel l1;
    private JButton b1, b2, b3;
    
    //Pagina do usuário
    private JFrame f1;
    private JLabel titular, l2, limite;
    private JButton b4, saqueBtn, depositoBtn, rendBtn, realizarRendBtn, sairBtn;
    
    //Pagina de valores
    private JFrame f2;
    private JLabel l3;
    private JTextField valorInput;
    private JButton InputBtn;

    //Pagina de Historico de Rendimentos
    private JFrame f3;
    private ArrayList<String> rends = new ArrayList<String>();
    public ExLabelUser(ContaDTO conta) throws Exception{
        try {
            Optional<Conta> possivelConta = parseConta(conta);
            if (possivelConta.isPresent()) {
                this.conta = possivelConta.get();
                acessarConta();
            }
        } catch (Exception ex){
            throw ex;
        }
    }

    private Optional<Conta> parseConta(ContaDTO conta) throws Exception{
        try {
            switch (conta.getId_tipo().getId_tipo()){
                case 1: return Optional.of(new ContaCorrente(conta.getId_conta(), conta.getTitular(), conta.getSaldo(), conta.getId_tipo(), conta.getTaxa()));
                case 2: return Optional.of(new ContaPoupanca(conta.getId_conta(), conta.getTitular(), conta.getSaldo(), conta.getId_tipo(), conta.getRendimento()));
                case 3: return Optional.of(new ContaEspecial(conta.getId_conta(), conta.getTitular(), conta.getSaldo(), conta.getId_tipo(), conta.getLimite()));
                default: throw new Exception("Conta não encontrada!");
            }
        } catch (Exception ex){
            throw new Exception("Conta não encontrada!");
        }
    }

    private void acessarConta(){

        criarPainelConta(conta.getTipo().getTipo());
        atualizarSaldo();
        addListenersConta();
        System.out.println(conta.getTipo());
        switch (conta.getTipo().getId_tipo()){
            case 1:break;
            case 2:
                realizarRendBtn = new JButton("Realizar Rendimento");
                realizarRendBtn.setBounds(225, 60, 175,30);

                addListenersRendBtn();

                rendBtn = new JButton("Histórico de Rendimentos");
                rendBtn.setBounds(25,140,150,30);

                addListenersHistorico();

                f1.add(realizarRendBtn);
                f1.add(rendBtn);
                break;
            case 3:
                limite = new JLabel();
                limite.setBounds(25,40, 200, 30);

                f1.add(limite);

                atualizarSaldo();
                break;
        }
    }
    private void criarPainelConta(String titulo){
        this.setVisible(false);
        f1 = new JFrame();
        f1.setLayout(null);
        f1.setTitle(titulo);
        f1.setSize(500,300);
        f1.setVisible(true);
        f1.setLocationRelativeTo(null);
        f1.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        System.out.println(conta.getTitular());
        titular = new JLabel("Nome: " + conta.getTitular());
        titular.setBounds(25, 5, 300, 30);

        l2 = new JLabel();
        l2.setBounds(25,25,200,30);

        b4 = new JButton("Atualizar Saldo");
        b4.setBounds(225, 25, 125,30);

        saqueBtn = new JButton("Saque");
        saqueBtn.setBounds(25,70,150,30);

        depositoBtn = new JButton("Deposito");
        depositoBtn.setBounds(25,105,150,30);

        sairBtn = new JButton("Sair");
        sairBtn.setBounds(25, 225, 150,30);

        f1.add(sairBtn);
        f1.add(titular);
        f1.add(b4);
        f1.add(l2);
        f1.add(saqueBtn);
        f1.add(depositoBtn);
    }
    private void criarFrameInput(String titulo){
        f2 = new JFrame();
        f2.setLayout(null);
        f2.setTitle(titulo);
        f2.setSize(500,150);
        f2.setVisible(true);
        f2.setLocationRelativeTo(null);
        
        l3 = new JLabel("Insira um valor a ser processado:");
        l3.setBounds(25,0, 275, 30);
        
        valorInput = new JTextField();
        valorInput.setBounds(25,30,275,30);
        
        InputBtn = new JButton(titulo);
        InputBtn.setBounds(25, 60, 275, 30);
        f2.add(l3);
        f2.add(valorInput);
        f2.add(InputBtn);
        addListenersInput();
    }
    private void addListenersRendBtn(){
        realizarRendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarRendimento();
            }
        });
    }
    private void addListenersHistorico(){
        rendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarHistoricoRend();
                f3.setVisible(true);
            }
        });
    }
    private void addListenersInput(){
        InputBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    if (valorInput.getText().length() < 1){
                        throw new Exception("Valor inválido.");
                    }

                    double valor = Double.parseDouble(valorInput.getText().trim());
                    f2.setVisible(false);

                    switch(f2.getTitle()){
                        case "Saque":
                            if (confirmarSaque(JOptionPane.showConfirmDialog(f1, "Confirmar o saque de " + valor + " ?", "Confirmação", JOptionPane.YES_NO_CANCEL_OPTION))){
                                HttpResponse<String> response = connection.realizarSaque(conta.getId_conta(), valor);
                                if (response.statusCode() != 200){
                                    throw new Exception(response.body());
                                }
                                JOptionPane.showMessageDialog(f1, "Saque de " + valor + " Realizado com sucesso.");
                            }
                        break;
                        case "Deposito":
                            connection.realizarDeposito(conta.getId_conta(), valor);
                            JOptionPane.showMessageDialog(f1, "Deposito de " + valor + " Realizado com sucesso.");
                        break;
                    }
                    atualizarSaldo();
                } catch (Exception ex){
                    JOptionPane.showMessageDialog(f1, ex.getMessage());
                }
            }
        });
    }
    private void mostrarHistoricoRend(){
        f3 = new JFrame();
        f3.setLayout(null);
        f3.setSize(250,300);
        f3.setLocationRelativeTo(null);

        //if (rends == null){ return;}
        int startY = 35;
        for(int i = 0; i < rends.size(); i++){
            JLabel label = new JLabel(i+1 +" - " + rends.get(i));
            label.setBounds(25, (startY*i)/2, 275, 30);
            f3.add(label);
        }
    }
    private boolean confirmarSaque(int confirmation){
        switch (confirmation){
            case 0:
                return true;
            default: return false;
        }
    }
    private void atualizarSaldo(){
        ContaDTO contaDTO = connection.getConta(this.conta.getId_conta());
        try{
            Optional<Conta> conta = parseConta(contaDTO);
            if (conta.isPresent()){
                this.conta = conta.get();
                l2.setText("Saldo: R$" + String.format("%.2f",this.conta.getSaldo()));
                if (limite != null) {
                    ContaEspecial contaEspecial = (ContaEspecial) this.conta;
                    limite.setText("Limite: R$" + String.format("%.2f",contaEspecial.getLimite()));
                }
            }
        } catch (Exception ex){
            JOptionPane.showMessageDialog(f1, ex.getMessage());
        }


    }

    private void addListenersConta(){
        f1.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosed(WindowEvent e){
                sair();
            }
        });
        //Atualizar saldo
         b4.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarSaldo();
            }
        });
        //Saque
        saqueBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                criarFrameInput(saqueBtn.getText());
            }
        });
        //Deposito
        depositoBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                criarFrameInput(depositoBtn.getText());

            }
        });
        sairBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sair();
            }
        });
    }
    private void sair(){
        if (!sys.isVisible() ){
            sys.setVisible(true);
        }
        f1.dispose();
    }
    private void realizarRendimento(){
        try {
            double valor = connection.realizarRendimento(conta.getId_conta());
            rends.add(String.format("%1$,.2f", valor));
            atualizarSaldo();
        } catch (Exception ex){
            System.out.println("Rendimento não realizado.");
        }
    }
}