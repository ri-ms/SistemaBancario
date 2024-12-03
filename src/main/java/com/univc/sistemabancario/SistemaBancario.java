/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.univc.sistemabancario;

import com.univc.sistemabancario.ExLabel.ExLabelSistema;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 *
 * @author UVC
 */

@SpringBootApplication
public class SistemaBancario {

    private static ExLabelSistema sys = new ExLabelSistema();

    public static void main(String[] args) {
        SpringApplication.run(SistemaBancario.class, args);
        sys.setVisible(true);
    }
}