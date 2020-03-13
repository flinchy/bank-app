package com.chisom.user_front.service;

import com.chisom.user_front.domain.*;

import java.security.Principal;
import java.util.List;

public interface TransactionService {

    List<PrimaryTransaction> findPrimaryTransactionList(String username);

    List<SavingsTransaction> findSavingsTransactionList(String username);

    void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction);

    void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction);

    void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction);

    void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransaction);

    void betweenAccountTransfer(String transferFrom, String transferTo, String amount,
                                PrimaryAccount primaryAccount, SavingsAccount savingsAccount) throws Exception;

    List<Recipient> findRecipientList(Principal principal);

    Recipient findRecipientByName(String recipientName);

    Recipient saveRecipient(Recipient recipient);

    void deleteRecipientByName(String recipientName);

    void toSomeoneElseTransfer(Recipient recipient, String accountType, String amount,
                               PrimaryAccount primaryAccount, SavingsAccount savingsAccount);
}
