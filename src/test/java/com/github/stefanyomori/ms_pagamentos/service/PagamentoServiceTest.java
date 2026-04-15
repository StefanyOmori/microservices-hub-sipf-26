package com.github.stefanyomori.ms_pagamentos.service;

import com.github.stefanyomori.ms_pagamentos.dto.PagamentoDTO;
import com.github.stefanyomori.ms_pagamentos.entities.Pagamento;
import com.github.stefanyomori.ms_pagamentos.exceptions.ResourceNotFoundException;
import com.github.stefanyomori.ms_pagamentos.repository.PagamentoRepository;
import com.github.stefanyomori.ms_pagamentos.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class PagamentoServiceTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @InjectMocks
    private PagamentoService pagamentoService;

    private Long existingId;
    private Long nonExistingId;

    private Pagamento pagamento;

    @BeforeEach
    void setUp(){
        existingId = 1L;
        nonExistingId = Long.MAX_VALUE;
        pagamento = Factory.createPagamento();
    }

    @Test
    void deletePagamentoByIdShouldDeleteWhenIdExists(){
        Mockito.when(pagamentoRepository.existsById(existingId)).thenReturn(true);

        pagamentoService.deletePagamentoById(existingId);

        Mockito.verify(pagamentoRepository).existsById(existingId);
        Mockito.verify(pagamentoRepository,Mockito.times(1)).deleteById(existingId);
    }

    @Test
    @DisplayName("deletePagamentoByID deveria lançar ResourceNOtFoundException quando o Id não existir")
    void deletePagamentoByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){
        Mockito.when(pagamentoRepository.existsById(nonExistingId)).thenReturn(false);
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> {
                    pagamentoService.deletePagamentoById(nonExistingId);
                });

        Mockito.verify(pagamentoRepository).existsById(nonExistingId);
        Mockito.verify(pagamentoRepository, Mockito.never()).deleteById(nonExistingId);
    }

    @Test
    void findPagamentoByIdShouldReturnPagamentoDTOWhenIdExists(){
        // Arrange
        Mockito.when(pagamentoRepository.findById(existingId))
                .thenReturn(Optional.of(pagamento));

        // Act
        PagamentoDTO result = pagamentoService.findPagamentoById(existingId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(pagamento.getId(), result.getId());
        Assertions.assertEquals(pagamento.getValor(), result.getValor());

        Mockito.verify(pagamentoRepository).findById(existingId);
        Mockito.verifyNoMoreInteractions(pagamentoRepository);
    }

    @Test
    void findPagamentoByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){
        Mockito.when(pagamentoRepository.findById(nonExistingId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> pagamentoService.findPagamentoById(nonExistingId));

        Mockito.verify(pagamentoRepository).findById(nonExistingId);
        Mockito.verifyNoMoreInteractions(pagamentoRepository);
    }

    @Test
    @DisplayName("Dado parâmetros válidos e Id nulo "+
    "quando chamar Salvar Pagamento "+
    "então deve gerar Id e persistir um Pagamento")
    void givenValidParamsAndIdIsNull_whenSave_thenShouldPersistPagamento(){
        pagamento.setId(null);
        //Arrange
        Mockito.when(pagamentoRepository.save(any(Pagamento.class)))
                .thenReturn(pagamento);
        PagamentoDTO inputDto = new PagamentoDTO(pagamento);
        //Act
        PagamentoDTO result = pagamentoService.savePagamento(inputDto);
        //Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(pagamento.getId(), result.getId());
        //Verify
        Mockito.verify(pagamentoRepository).save(any(Pagamento.class));
        Mockito.verifyNoMoreInteractions(pagamentoRepository);
    }

    @Test
    void updatePagamentoShouldReturnPagamentoDTOWhenIdExists(){
        //Arrange
        Long id = pagamento.getId();
        Mockito.when(pagamentoRepository.getReferenceById(id)).thenReturn(pagamento);
        Mockito.when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);

        //Act
        PagamentoDTO result = pagamentoService.updatePagamento(id, new PagamentoDTO(pagamento));

        //Assert e Verify
        Assertions.assertNotNull(result);
        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals(pagamento.getValor(), result.getValor());
        Mockito.verify(pagamentoRepository).getReferenceById(id);
        Mockito.verify(pagamentoRepository).save(Mockito.any(Pagamento.class));
        Mockito.verifyNoMoreInteractions(pagamentoRepository);
    }

    @Test
    void updatePagamentoShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){
        Mockito.when(pagamentoRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
        PagamentoDTO inputDto = new PagamentoDTO(pagamento);

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> pagamentoService.updatePagamento(nonExistingId, inputDto));

        Mockito.verify(pagamentoRepository).getReferenceById(nonExistingId);
        Mockito.verify(pagamentoRepository, Mockito.never()).save(Mockito.any(Pagamento.class));
        Mockito.verifyNoMoreInteractions(pagamentoRepository);
    }

}
