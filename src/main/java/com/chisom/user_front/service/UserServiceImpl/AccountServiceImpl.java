package com.chisom.user_front.service.UserServiceImpl;

import com.chisom.user_front.domain.*;
import com.chisom.user_front.repository.PrimaryAccountRepository;
import com.chisom.user_front.repository.SavingsAccountRepository;
import com.chisom.user_front.service.AccountService;
import com.chisom.user_front.service.TransactionService;
import com.chisom.user_front.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;

@Service
public class AccountServiceImpl implements AccountService {

    private static int nextAccountNumber = 1122345; //we are making this static so that it will be shared through out the instance.

    @Autowired
    private PrimaryAccountRepository primaryAccountRepository;

    @Autowired
    private SavingsAccountRepository savingsAccountRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    public PrimaryAccount createPrimaryAccount() {
        PrimaryAccount primaryAccount = new PrimaryAccount();
        primaryAccount.setAccountBalance(new BigDecimal(0.0));
        primaryAccount.setAccountNumber(accountGen());

        primaryAccountRepository.save(primaryAccount);

        /**
         * This line of code is a little bit confusing. Since we have already save the primaryAccount instance on line 31
         * while dont we just directly return the primaryAccount, instead of using the findByAccountNumber on line 40
         * defined in the primaryAccountRepository to retrieve the account we just saved.
         * Remember we we are declaring a new primaryAccount instance on line 27, we are not specifying its ID,
         * since we are using automatic generation strategy that is defined in our primaryAccount domain model to generate
         * id automatically. And its is only after we persist to the database that the ID will be generated.
         * And why do we need this account id?
         * if you look at the UserServiceImpl line 59 we are setting the primary account to the account that we just created
         * now the question is how does the table bind this together?
         * If you look at the User class, you will see that it has the primary_account_id column, so we need to specify what
         * kind of id it is. And in other to specify that, we need to have the Account Id available first,
         * that is why we need to persist the primaryAccount to the database first on line 31 so that an id can be
         * generated automatically first so that when we retrieve the instance by the same account number the Id will be
         * available to us to be used. same idea goes on line 58
         */
        return primaryAccountRepository.findByAccountNumber(primaryAccount.getAccountNumber());
    }

    public SavingsAccount createSavingsAccount() {
        SavingsAccount savingsAccount = new SavingsAccount();
        savingsAccount.setAccountBalance(new BigDecimal(0.0));
        savingsAccount.setAccountNumber(accountGen());

        savingsAccountRepository.save(savingsAccount);
        return savingsAccountRepository.findByAccountNumber(savingsAccount.getAccountNumber());
    }

    public void deposit(String accountType, double amount, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        if (accountType.equalsIgnoreCase("Primary")) {
            PrimaryAccount primaryAccount = user.getPrimaryAccount();
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
            primaryAccountRepository.save(primaryAccount);

            Date date = new Date();
            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Deposit to primary account",
                    "Account", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount);

            transactionService.savePrimaryDepositTransaction(primaryTransaction);

        } else if (accountType.equalsIgnoreCase("Savings")) {
            SavingsAccount savingsAccount = user.getSavingsAccount();
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
            savingsAccountRepository.save(savingsAccount);

            Date date = new Date();
            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Deposit to savings account",
                    "Account", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount);

            transactionService.saveSavingsDepositTransaction(savingsTransaction);
        }

    }

    public void withdraw(String accountType, double amount, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        if (accountType.equalsIgnoreCase("Primary")) {
            PrimaryAccount primaryAccount = user.getPrimaryAccount();
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            primaryAccountRepository.save(primaryAccount);

            Date date = new Date();
            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Withdraw from primary account",
                    "Account", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount);

            transactionService.savePrimaryWithdrawTransaction(primaryTransaction);

        } else if (accountType.equalsIgnoreCase("Savings")) {
            SavingsAccount savingsAccount = user.getSavingsAccount();
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savingsAccountRepository.save(savingsAccount);

            Date date = new Date();
            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Withdraw from savings account",
                    "Account", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount);

            transactionService.saveSavingsWithdrawTransaction(savingsTransaction);
        }
    }

    private int accountGen() {
        return ++nextAccountNumber;
    }
}
