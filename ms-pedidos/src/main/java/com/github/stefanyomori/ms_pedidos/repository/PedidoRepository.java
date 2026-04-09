package com.github.stefanyomori.ms_pedidos.repository;

import com.github.stefanyomori.ms_pedidos.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}