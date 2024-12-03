package com.univc.sistemabancario.controller;

import com.univc.sistemabancario.entity.TipoEntity;
import com.univc.sistemabancario.service.TipoService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("tipo")
public class TipoController {
    @Autowired
    private TipoService tipoService;

    @GetMapping("/listar")
    public List<TipoEntity> listar(){
        return tipoService.listarTipos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoEntity> obterTipo(@PathVariable("id") short id){
        Optional<TipoEntity> tipo = tipoService.obterTipo(id);
        if (tipo.isPresent()){
            TipoEntity tipoEncontrado = tipo.get();
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(201))
                    .body(tipoEncontrado);
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).build();
    }

}
