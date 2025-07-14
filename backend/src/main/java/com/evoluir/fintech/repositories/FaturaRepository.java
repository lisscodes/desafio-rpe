package com.evoluir.fintech.repositories;

import com.evoluir.fintech.domain.entities.Fatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface FaturaRepository extends JpaRepository<Fatura, Long> {

    List<Fatura> findByClienteId(Long clienteId);

    //faturas vencidas há mais de 3 dias e ainda não pagas
    @Query("SELECT f FROM Fatura f WHERE f.dataPagamento IS NULL AND f.dataVencimento < :limite AND f.status <> 'P'")
    List<Fatura> findFaturasAtrasadas(LocalDate limite);
}
