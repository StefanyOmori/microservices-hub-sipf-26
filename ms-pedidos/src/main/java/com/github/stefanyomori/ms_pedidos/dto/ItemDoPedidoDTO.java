package com.github.stefanyomori.ms_pedidos.dto;

import com.github.stefanyomori.ms_pedidos.entities.ItemDoPedido;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ItemDoPedidoDTO {
    private Long id;
    @NotNull(message = "Quantidade requerida")
    @Positive(message = "Quantiade dvee ser um número positivo")
    private Integer qtd;
    @NotBlank(message = "Descrição requerida")
    private String descricao;
    @NotNull(message = "Preço unitário requerida")
    @Positive(message = "Preço unitário dvee ser um número positivo")
    private BigDecimal precoUnitario;

    public ItemDoPedidoDTO(ItemDoPedido itemDoPedido) {
        id = itemDoPedido.getId();
        qtd = itemDoPedido.getQtd();
        descricao = itemDoPedido.getDescricao();
        precoUnitario = itemDoPedido.getPrecoUnitario();
    }

}
