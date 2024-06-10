package com.habsida.morago.repository;

import com.habsida.morago.model.entity.Deposits;
import com.habsida.morago.model.entity.User;
import com.habsida.morago.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepositsRepository extends JpaRepository<Deposits, Long> {
    List<Deposits> findByStatus(Status status);

    List<Deposits> findByUser(User user);
}