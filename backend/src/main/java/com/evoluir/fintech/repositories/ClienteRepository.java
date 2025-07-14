package com.evoluir.fintech.repositories;

import com.evoluir.fintech.domain.entities.Cliente;
import com.evoluir.fintech.domain.entities.StatusBloqueio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByStatusBloqueio(StatusBloqueio status);
}
