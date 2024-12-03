package com.univc.sistemabancario.service;

import com.univc.sistemabancario.Conta;
import com.univc.sistemabancario.ContaCorrente;
import com.univc.sistemabancario.ContaEspecial;
import com.univc.sistemabancario.ContaPoupanca;
import com.univc.sistemabancario.entity.ContaEntity;
import com.univc.sistemabancario.entity.TipoEntity;
import com.univc.sistemabancario.repository.ContaRepository;
import com.univc.sistemabancario.repository.TipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BancoService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private TipoRepository tipoRepository;

    public ContaEntity cadastroConta(ContaEntity body){
        TipoEntity tipoEntity = tipoRepository.findById(body.getId_tipo().getId_tipo())
                .orElseThrow(() -> new IllegalArgumentException("Tipo não encontrado com o id: " + body.getId_tipo().getId_tipo()));
        body.setId_tipo(tipoEntity);
        return contaRepository.save(body);
    }

    public ContaEntity updateConta(ContaEntity contaEntity){
        contaRepository.save(contaEntity);
        return contaEntity;
    }

    public ContaEntity deleteConta(ContaEntity conta){
        contaRepository.delete(conta);
        return conta;
    }

    public List<ContaEntity> listarContas() {
        return contaRepository.findAll();
    }

    public Optional<ContaEntity> buscarPeloId(int id){
        return contaRepository.findById(id);
    }
}
