package com.chisom.user_front.service;

import com.chisom.user_front.domain.PrimaryAccount;
import com.chisom.user_front.domain.SavingsAccount;

import java.security.Principal;

public interface AccountService {

    PrimaryAccount createPrimaryAccount();

    SavingsAccount createSavingsAccount();

    void deposit(String accountType, double amount, Principal principal);

    void withdraw(String accountType, double amount, Principal principal);


}
