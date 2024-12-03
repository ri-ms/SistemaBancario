package com.univc.sistemabancario.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "conta", uniqueConstraints = {@UniqueConstraint(columnNames = {"id_conta"})})
@NoArgsConstructor
@AllArgsConstructor
public class ContaEntity {
    @Id
    @Column(name = "id_conta")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_conta;

    @NonNull
    @Column(name="titular", length=50)
    private String titular;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "id_tipo", nullable = false)
    private TipoEntity id_tipo;

    @NonNull
    @Column(name="saldo")
    private double saldo;

    @Column(name = "limite", nullable = true)
    private Double limite = null;

    @Column(name = "taxa", nullable = true)
    private Double taxa = null;

    @Column(name = "rendimento", nullable = true)
    private Double rendimento = null;

    public TipoEntity getId_tipo() {
        return this.id_tipo;
    }
    public void setId_tipo(TipoEntity id_tipo){
        this.id_tipo = id_tipo;
    }
}