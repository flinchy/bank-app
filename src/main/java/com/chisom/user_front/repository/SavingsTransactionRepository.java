package com.chisom.user_front.repository;

import com.chisom.user_front.domain.SavingsTransaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SavingsTransactionRepository extends CrudRepository<SavingsTransaction, Long> {

    List<SavingsTransaction> findAll();
}
