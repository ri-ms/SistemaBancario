package com.univc.sistemabancario.service;

import com.univc.sistemabancario.entity.TipoEntity;
import com.univc.sistemabancario.repository.TipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoService {
    @Autowired
    private TipoRepository tipoRepository;

    public List<TipoEntity> listarTipos(){
        return tipoRepository.findAll();
    }

    public Optional<TipoEntity> obterTipo(Short id){
        return tipoRepository.findById(id);
    }
}
