package com.univc.sistemabancario.controller;

import com.univc.sistemabancario.Conta;
import com.univc.sistemabancario.ContaCorrente;
import com.univc.sistemabancario.ContaEspecial;
import com.univc.sistemabancario.ContaPoupanca;
import com.univc.sistemabancario.connection.DTO.TipoDTO;
import com.univc.sistemabancario.entity.ContaEntity;
import com.univc.sistemabancario.service.BancoService;
import com.univc.sistemabancario.service.TipoService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("banco")
public class BancoController {

    @Autowired
    private BancoService bancoService;

    @GetMapping("/listar")
    public List<ContaEntity> listarContas() {
        return bancoService.listarContas();
    }

    @PostMapping("/cadastro")
    public ResponseEntity<ContaEntity> cadastroCliente(@RequestBody ContaEntity conta) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(201))
                .body(bancoService.cadastroConta(conta));
    }

    @GetMapping("/login/{id_conta}")
    public ResponseEntity<ContaEntity> loginConta(@PathVariable("id_conta") int id) {
        Optional<ContaEntity> conta = bancoService.buscarPeloId(id);
        if (conta.isPresent()) {
            ContaEntity contaEncontrada = conta.get();
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(201))
                    .body(contaEncontrada);
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).build();
    }

    @DeleteMapping("/delete/{id_conta}")
    public ResponseEntity<Void> apagarConta(@PathVariable("id_conta") int id) {
        Optional<ContaEntity> conta = bancoService.buscarPeloId(id);
        if (conta.isPresent()) {
            ContaEntity contaEncontrada = conta.get();
            bancoService.deleteConta(contaEncontrada);
            return ResponseEntity.status(HttpStatusCode.valueOf(204)).build();
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).build();
    }

    @PatchMapping("/update/{id_conta}")
    public ResponseEntity<ContaEntity> atualizarConta(@PathVariable("id_conta") int id, @RequestBody ContaEntity newConta) {
        Optional<ContaEntity> conta = bancoService.buscarPeloId(id);
        if (conta.isPresent()) {
            bancoService.updateConta(newConta);
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(201))
                    .body(newConta);
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).build();
    }

    @PostMapping("/saque/{id_conta}/{quantidade}")
    public ResponseEntity<String> realizarSaque(@PathVariable("id_conta") int id, @PathVariable("quantidade") Double quantia) {
        Optional<ContaEntity> contaEncontrada = bancoService.buscarPeloId(id);
        if (contaEncontrada.isPresent()) {
            ContaEntity conta = contaEncontrada.get();
            try {
                switch (conta.getId_tipo().getId_tipo()) {
                    case 1:
                        ContaCorrente contaCorrente = new ContaCorrente(conta.getId_conta(), conta.getTitular(), conta.getSaldo(), conta.getTaxa());
                        contaCorrente.sacar(quantia);
                        conta.setSaldo(contaCorrente.getSaldo());
                        break;
                    case 2:
                        ContaPoupanca contaPoupanca = new ContaPoupanca(conta.getId_conta(), conta.getTitular(), conta.getSaldo(), conta.getRendimento());
                        contaPoupanca.sacar(quantia);
                        conta.setSaldo(contaPoupanca.getSaldo());
                        break;
                    case 3:
                        ContaEspecial contaEspecial = new ContaEspecial(conta.getId_conta(), conta.getTitular(), conta.getSaldo(), conta.getLimite());
                        contaEspecial.sacar(quantia);
                        conta.setSaldo(contaEspecial.getSaldo());
                        conta.setLimite(contaEspecial.getLimite());
                        break;
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(e.getMessage());
            }
            bancoService.updateConta(conta);
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).build();
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).build();
    }

    @PostMapping("/rend/{id_conta}")
    public ResponseEntity<Double> realizarRendimento(@PathVariable("id_conta") int id){
        Optional<ContaEntity> contaEncontrada = bancoService.buscarPeloId(id);
        if (contaEncontrada.isPresent()) {
            ContaEntity conta = contaEncontrada.get();
            if (conta.getId_tipo().getId_tipo() == 2) {
                ContaPoupanca contaPoupanca = new ContaPoupanca(conta.getId_conta(), conta.getTitular(), conta.getSaldo(), conta.getRendimento());
                double rendimento = contaPoupanca.realizarRendimento();
                conta.setSaldo(contaPoupanca.getSaldo());
                bancoService.updateConta(conta);
                return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(rendimento);
            }
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
    }

    @PostMapping("/deposito/{id_conta}/{quantidade}")
    public ResponseEntity<String> realizarDeposito(@PathVariable("id_conta") int id, @PathVariable("quantidade") Double quantia) {
        Optional<ContaEntity> contaEncontrada = bancoService.buscarPeloId(id);
        if (contaEncontrada.isPresent()) {
            ContaEntity conta = contaEncontrada.get();
            try {
                switch (conta.getId_tipo().getId_tipo()) {
                    case 1:
                        ContaCorrente contaCorrente = new ContaCorrente(conta.getId_conta(), conta.getTitular(), conta.getSaldo(), conta.getTaxa());
                        contaCorrente.depositar(quantia);
                        conta.setSaldo(contaCorrente.getSaldo());
                        break;
                    case 2:
                        ContaPoupanca contaPoupanca = new ContaPoupanca(conta.getId_conta(), conta.getTitular(), conta.getSaldo(), conta.getRendimento());
                        contaPoupanca.depositar(quantia);
                        conta.setSaldo(contaPoupanca.getSaldo());
                        break;
                    case 3:
                        ContaEspecial contaEspecial = new ContaEspecial(conta.getId_conta(), conta.getTitular(), conta.getSaldo(), conta.getLimite());
                        contaEspecial.depositar(quantia);
                        conta.setSaldo(contaEspecial.getSaldo());
                        conta.setLimite(contaEspecial.getLimite());
                        break;
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(e.getMessage());
            }
            bancoService.updateConta(conta);
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).build();
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).build();
    }
}
