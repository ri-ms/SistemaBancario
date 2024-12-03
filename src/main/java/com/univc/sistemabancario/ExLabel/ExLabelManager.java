package com.univc.sistemabancario.ExLabel;

import com.univc.sistemabancario.*;
import com.univc.sistemabancario.ExLabel.table.ContaTableModel;
import com.univc.sistemabancario.connection.APIConnection;
import com.univc.sistemabancario.connection.DTO.ContaDTO;
import com.univc.sistemabancario.connection.DTO.TipoDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ExLabelManager extends JFrame {
    private static APIConnection connection = new APIConnection();
    ExLabelSistema sys = new ExLabelSistema();
    private JLabel labelNome, labelSaldo, labelLimite, labelRendimento, labeltipoConta, labelTaxa;
    private JTextField fieldNome, fieldSaldo, fieldLimite, fieldRendimento, fieldTaxa;
    private JPanel panelInfo, panelTable, panelButtons, panelUser;
    private JScrollPane scrollPane;
    private JButton btnNew, btnSave, btnCancel, btnRemove, btnEdit, btnUpdate;
    private JTable tabela;
    private List<ContaDTO> listaContas;
    private JButton btnLeave;

    private JComboBox fieldtipoConta;
    private JButton cadastroBtn;
    private String[] tipos = {"Selecionar", "Conta Corrente", "Conta Poupança", "Conta Especial"};
    private Conta conta;


    private int numconta;

    public ExLabelManager(){
        super("Cadastro de Usuário");
        criarInterface();
        addActionListeners();
        refreshTable();
        this.setVisible(true);
    }

    public void criarInterface(){
        this.setContentPane(new JPanel());
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setMinimumSize(new Dimension(525, 560));
        this.setLocationRelativeTo(null);

        panelInfo = new JPanel(new MigLayout());
        panelInfo.setBorder(BorderFactory.createTitledBorder("Informações da conta"));
        panelInfo.setBounds(5, 0, 500, 200);

        labelNome = new JLabel("Nome");
        fieldNome = new JTextField(30);

        labelSaldo = new JLabel("Saldo Inicial");
        fieldSaldo = new JTextField(30);

        labelLimite = new JLabel("Limite");
        fieldLimite = new JTextField(30);

        labelRendimento = new JLabel("Rendimento");
        fieldRendimento = new JTextField(30);

        labelTaxa = new JLabel("Taxa");
        fieldTaxa = new JTextField(30);

        labeltipoConta = new JLabel("Tipo de Conta");
        fieldtipoConta = new JComboBox(tipos);

        fieldtipoConta.setSelectedIndex(0);

        panelInfo.add(labelNome);
        panelInfo.add(fieldNome, "span, growx");

        panelInfo.add(labelSaldo);
        panelInfo.add(fieldSaldo, "span, growx");

        panelInfo.add(labelLimite);
        panelInfo.add(fieldLimite, "span, growx");
        fieldLimite.setEnabled(false);

        panelInfo.add(labelRendimento);
        panelInfo.add(fieldRendimento, "span, growx");
        fieldRendimento.setEnabled(false);

        panelInfo.add(labelTaxa);
        panelInfo.add(fieldTaxa, "span, growx");
        fieldTaxa.setEnabled(false);

        panelInfo.add(labeltipoConta);
        panelInfo.add(fieldtipoConta, "wrap para");

        panelButtons = new JPanel(new MigLayout());
        panelButtons.setBorder(BorderFactory.createEtchedBorder());
        panelButtons.setBounds(5, 200, 500, 40);

        panelTable = new JPanel(new MigLayout());
        panelTable.setBorder(BorderFactory.createTitledBorder("Lista de Contas"));
        panelTable.setBounds(5, 240, 500, 240);

        btnNew = new JButton("Novo");
        btnSave = new JButton("Salvar");
        btnCancel = new JButton("Cancelar");
        btnRemove = new JButton("Deletar");
        btnEdit = new JButton("Editar");
        btnUpdate = new JButton("Atualizar");

        tabela = new JTable();

        scrollPane = new JScrollPane(tabela);

        panelButtons.add(btnNew);
        panelButtons.add(btnCancel);
        panelButtons.add(btnSave);
        panelButtons.add(btnEdit);
        panelButtons.add(btnUpdate);
        panelButtons.add(btnRemove);

        panelTable.add(scrollPane);

        panelUser = new JPanel(new MigLayout());
        panelUser.setBorder(BorderFactory.createEtchedBorder());
        panelUser.setBounds(5, 480, 500, 40);

        btnLeave = new JButton("Voltar");

        panelUser.add(btnLeave);

        this.add(panelInfo);
        this.add(panelButtons);
        this.add(panelTable);
        this.add(panelUser);
        enableFields(false);
    }
    private void close(){
        if (!sys.isVisible() ){
            sys.setVisible(true);
        }
        this.dispose();
    }
    public void addActionListeners(){
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e){
                close();
            }
        });
        this.fieldtipoConta.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                alterarSelect();
            }
        });
        this.btnNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableFields(true);
            }
        });
        this.btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTable();
            }
        });
        this.btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
                enableFields(false);
                tabela.setEnabled(true);
            }
        });
        this.btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRemove();
                refreshTable();
            }
        });
        this.btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarCadastro();
                refreshTable();
                tabela.setEnabled(true);
            }
        });
        this.btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
                onEdit();
            }
        });
        this.btnLeave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
    }

    private void onEdit(){
        int rowIndex = tabela.getSelectedRow();
        if (rowIndex == -1){
            JOptionPane.showMessageDialog(this, "Selecione uma conta a ser atualizada!");
            return;
        }
        tabela.setEnabled(false);
        ContaDTO contaDTO = new ContaTableModel(listaContas).get(rowIndex);

        numconta = contaDTO.getId_conta();

        enableFields(true);

        fieldtipoConta.setSelectedIndex(contaDTO.getId_tipo().getId_tipo());
        if (contaDTO.getTitular() != null){
            fieldNome.setText(String.valueOf(contaDTO.getTitular()));
        }

        fieldSaldo.setText(String.valueOf(contaDTO.getSaldo()));

        if (contaDTO.getTaxa() != null){
            fieldTaxa.setText(String.valueOf(contaDTO.getTaxa()));
        }
        if (contaDTO.getRendimento() != null){
            fieldRendimento.setText(String.valueOf(contaDTO.getRendimento()));
        }
        if (contaDTO.getLimite() != null){
            fieldLimite.setText(String.valueOf(contaDTO.getLimite()));
        }
    }

    private void onRemove() {
        int rowIndex = tabela.getSelectedRow();
        if (rowIndex == -1){
            JOptionPane.showMessageDialog(this, "Selecione uma conta a ser removida!");
            return;
        }

        ContaDTO contaDTO = new ContaTableModel(listaContas).get(rowIndex);
        int confirm = JOptionPane.showConfirmDialog(this, "Confirmar exclusão?", "Excluir conta", JOptionPane.YES_NO_OPTION);
        switch (confirm){
            case 0:
                try {
                    connection.deleteConta(contaDTO.getId_conta());
                } catch (Exception ex){
                    JOptionPane.showMessageDialog(this, "Erro ao obter a conta selecionada, tente novamente mais tarde!");
                }
                break;
            case 1:
                return;
            default: JOptionPane.showMessageDialog(this, "Tente novamente!"); break;
        }
    }

    private void onCancel(){
        fieldNome.setText("");
        fieldSaldo.setText("");
        fieldLimite.setText("");
        fieldTaxa.setText("");
        fieldRendimento.setText("");
        fieldtipoConta.setSelectedIndex(0);

        numconta = -1;
    }

    private void refreshTable() {
        try {
            listaContas = connection.getContas();
            if (listaContas != null){
                tabela.setModel(new ContaTableModel(listaContas));
            }
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Ocorreu um erro ao se conectar no Banco de Dados, tente novamente mais tarde!");
        }
    }

    private void enableFields(boolean b){
        fieldNome.setEnabled(b);
        fieldSaldo.setEnabled(b);
        fieldtipoConta.setEnabled(b);
    }

    private void alterarSelect(){
            System.out.println(fieldtipoConta.getSelectedIndex());
            fieldLimite.setText("");
            fieldTaxa.setText("");
            fieldRendimento.setText("");
            String select = fieldtipoConta.getSelectedItem().toString();
            if (fieldtipoConta.getSelectedIndex() == 0) {
                fieldLimite.setEnabled(false);
                fieldTaxa.setEnabled(false);
                fieldRendimento.setEnabled(false);
            } else {
                switch (select) {
                    case "Conta Corrente": {
                        fieldLimite.setEnabled(false);
                        fieldTaxa.setEnabled(true);
                        fieldRendimento.setEnabled(false);
                        break;
                    }
                    case "Conta Poupança": {
                        fieldLimite.setEnabled(false);
                        fieldTaxa.setEnabled(false);
                        fieldRendimento.setEnabled(true);
                        break;
                    }
                    case "Conta Especial": {
                        fieldLimite.setEnabled(true);
                        fieldTaxa.setEnabled(false);
                        fieldRendimento.setEnabled(false);
                        break;
                    }
                }
            }
    }

    private void validarCadastro(){
        try {
            if (fieldNome.getText().length() <= 3){
                throw new Exception("Insira um nome válido.");
            }
            if (fieldSaldo.getText().length() < 1){
                throw new Exception("Insira um saldo válido.");
            }
            if (fieldtipoConta.getSelectedIndex() == 0){
                throw new Exception("Tipo de conta inválido");
            }

            ContaDTO contaDTO = new ContaDTO();

            if (numconta > 0){
                contaDTO.setId_conta(numconta);
            }

            contaDTO.setTitular(fieldNome.getText());
            contaDTO.setSaldo(Double.valueOf(fieldSaldo.getText()));

            switch (fieldtipoConta.getSelectedItem().toString()) {
                case ("Conta Corrente"):
                    if (fieldTaxa.getText().length() < 1){
                        throw new Exception("Insira uma Taxa válida.");
                    }
                    contaDTO.setTaxa(Double.valueOf(fieldTaxa.getText()));
                    break;
                case ("Conta Poupança"):
                    if (fieldRendimento.getText().length() < 1){
                        throw new Exception("Insira um rendimento válido.");
                    }
                    contaDTO.setRendimento(Double.valueOf(fieldRendimento.getText()));
                    break;
                case ("Conta Especial"):
                    if (fieldLimite.getText().length() < 1){
                        throw new Exception("Insira um limite válido.");
                    }
                    contaDTO.setLimite(Double.valueOf(fieldLimite.getText()));
                    break;
            }
            if (fieldtipoConta.getSelectedIndex() >= 0){

                cadastrarConta(contaDTO, fieldtipoConta.getSelectedIndex());
                onCancel();
                enableFields(false);
            } else{
                throw new Exception("Erro ao buscar o tipo de Conta.");
            }
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, ex.getMessage());
            return;
        }
        if (numconta > 0){
            JOptionPane.showMessageDialog(this, "Conta atualizada com sucesso!");
        } else {
            JOptionPane.showMessageDialog(this, "Conta criada com sucesso!");
        }

    }
    private void cadastrarConta (ContaDTO conta, int id_tipo) throws Exception{
        try {
            TipoDTO tipo = connection.getTipo(id_tipo);
            System.out.println(tipo);
            conta.setId_tipo(tipo);
            System.out.println(conta);
            connection.postConta(conta);
        } catch (Exception ex){
            throw new Exception("Ocorreu um erro ao buscar os dados, tente novamente mais tarde!");
        }
    }
}
