package com.chisom.user_front.repository;

import com.chisom.user_front.domain.PrimaryTransaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PrimaryTransactionRepository extends CrudRepository<PrimaryTransaction, Long> {

    List<PrimaryTransaction> findAll();
}
