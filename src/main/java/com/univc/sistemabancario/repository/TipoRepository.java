package com.univc.sistemabancario.repository;

import com.univc.sistemabancario.entity.TipoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoRepository extends JpaRepository<TipoEntity, Short> {
}
