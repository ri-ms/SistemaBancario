package com.univc.sistemabancario.repository;

import com.univc.sistemabancario.entity.ContaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaRepository extends JpaRepository<ContaEntity, Integer> {
}
