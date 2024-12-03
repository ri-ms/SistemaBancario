package com.univc.sistemabancario.ExLabel.table;

import com.univc.sistemabancario.connection.DTO.ContaDTO;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ContaTableModel extends AbstractTableModel {

    private static final int COL_ID_CONTA = 0;
    private static final int COL_TITULAR = 1;
    private static final int COL_SALDO = 2;
    private static final int COL_TIPO = 3;
    private static final int COL_RENDIMENTO = 4;
    private static final int COL_LIMITE = 5;
    private static final int COL_TAXA = 6;

    private List<ContaDTO> valores;

    public ContaTableModel(List<ContaDTO> valores) {
        this.valores = valores;
    }
    public int getRowCount() {
        return valores.size();
    }
    public int getColumnCount() {
        return 7;
    }
    public Object getValueAt(int rowIndex, int columnIndex){
        ContaDTO contaDTO = valores.get(rowIndex);
        switch (columnIndex){
            case COL_ID_CONTA: return contaDTO.getId_conta();
            case COL_TITULAR: return contaDTO.getTitular();
            case COL_SALDO: return contaDTO.getSaldo();
            case COL_TIPO: return contaDTO.getId_tipo().getTipo();
            case COL_RENDIMENTO: return contaDTO.getRendimento();
            case COL_LIMITE: return contaDTO.getLimite();
            case COL_TAXA: return contaDTO.getTaxa();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column){
        String coluna = "";
        switch (column){
            case COL_ID_CONTA: coluna = "Id"; break;
            case COL_TITULAR: coluna = "Titular"; break;
            case COL_TIPO: coluna = "Tipo"; break;
            case COL_SALDO: coluna = "Saldo"; break;
            case COL_LIMITE: coluna = "Limite"; break;
            case COL_RENDIMENTO: coluna = "Rendimento"; break;
            case COL_TAXA: coluna = "Taxa"; break;
            default: throw new IllegalArgumentException("Coluna inválida!");
        }
        return coluna;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex){
            case COL_ID_CONTA: return int.class;
            case COL_TITULAR: return String.class;
            case COL_SALDO: return double.class;
            case COL_TIPO: return String.class;
            case COL_LIMITE: return double.class;
            case COL_RENDIMENTO: return double.class;
            case COL_TAXA: return double.class;
        }
        return null;
    }

    public ContaDTO get(int row){
        return valores.get(row);
    }
}
