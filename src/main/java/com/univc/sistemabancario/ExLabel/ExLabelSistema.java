package com.univc.sistemabancario.ExLabel;

import com.univc.sistemabancario.Conta;
import com.univc.sistemabancario.connection.APIConnection;
import com.univc.sistemabancario.connection.DTO.ContaDTO;
import net.miginfocom.layout.AC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ExLabelSistema extends JFrame {
    private static APIConnection connection = new APIConnection();
    private ArrayList<Conta> contas = new ArrayList<Conta>();

    private JTextField fieldAcesso;
    private JLabel titulo, labelAcesso;
    private JPanel panelAcesso;
    private JButton b1, b2;

    public ExLabelSistema(){
        criarInterface();
        addActionListeners();
    }

    public void criarInterface(){
        this.setTitle("Sistema Bancário");
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 600);
        this.setLocationRelativeTo(null);

        titulo = new JLabel("Acesso ao Sistema");
        titulo.setBounds(0, 25, 500, 30);
        titulo.setHorizontalAlignment(0);
        titulo.setFont(new Font(titulo.getFont().getFontName(), Font.PLAIN, 28));

        JPanel panelAcesso = new JPanel(new MigLayout(
                "align center center",
                "[grow, center]",
                "[]10[]10[]"
        ));
        panelAcesso.setBounds(0, 150, 485, 120);

        labelAcesso = new JLabel("Numero da conta");
        fieldAcesso = new JTextField(20);
        fieldAcesso.setToolTipText("Insira o numero da conta a ser acessada");

        b1 = new JButton("Acessar a conta");

        panelAcesso.add(labelAcesso, "wrap");
        panelAcesso.add(fieldAcesso,"wrap");
        panelAcesso.add(b1);
        panelAcesso.setBorder(BorderFactory.createEtchedBorder());


        b2 = new JButton("Gerenciar Contas");
        b2.setBounds(310, 500, 150, 30);

        this.add(titulo);
        this.add(b2);
        this.add(panelAcesso);
    }

    public void addActionListeners(){
        this.b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acessarConta();
            }
        });
        this.b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acessarManager();
            }
        });
    }

    private void acessarManager(){
        ExLabelManager manager = new ExLabelManager();
        this.dispose();
    }

    private void acessarConta(){
        int id = Integer.parseInt(fieldAcesso.getText());

        ContaDTO contaDTO = connection.getConta(id);
        System.out.println(contaDTO.toString());
        try {
            ExLabelUser exLabelUser = new ExLabelUser(contaDTO);
            this.dispose();
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }

    }
}
