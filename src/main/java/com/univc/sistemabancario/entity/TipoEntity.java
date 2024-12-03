package com.univc.sistemabancario.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "tipo")
@NoArgsConstructor
@AllArgsConstructor
public class TipoEntity {
    @Id
    @Column(name = "id_tipo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id_tipo;

    @Column(name = "tipo", length = 15, nullable = false)
    private String tipo;

    public Short getId_tipo(){
        return this.id_tipo;
    }
}
