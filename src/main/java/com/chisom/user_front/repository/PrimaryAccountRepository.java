package com.chisom.user_front.repository;

import com.chisom.user_front.domain.PrimaryAccount;
import org.springframework.data.repository.CrudRepository;

public interface PrimaryAccountRepository extends CrudRepository<PrimaryAccount, Long> {

    PrimaryAccount findByAccountNumber(int accountNumber);
}
