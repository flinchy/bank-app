package com.chisom.user_front.repository;

import com.chisom.user_front.domain.SavingsAccount;
import org.springframework.data.repository.CrudRepository;

public interface SavingsAccountRepository extends CrudRepository<SavingsAccount, Long> {

    SavingsAccount findByAccountNumber(int accountNumber);
}
