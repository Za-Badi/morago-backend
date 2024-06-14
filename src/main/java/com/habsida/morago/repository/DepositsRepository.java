package com.habsida.morago.repository;

import com.habsida.morago.model.entity.Deposits;
import com.habsida.morago.model.entity.User;
import com.habsida.morago.model.enums.CallStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepositsRepository extends JpaRepository<Deposits, Long> {
    List<Deposits> findByStatus(CallStatus status);

    List<Deposits> findByUser(User user);
}