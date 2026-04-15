package com.github.stefanyomori.ms_pagamentos.repository;

import com.github.stefanyomori.ms_pagamentos.entities.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}
