package com.chisom.user_front.service.UserServiceImpl;

import com.chisom.user_front.domain.*;
import com.chisom.user_front.repository.*;
import com.chisom.user_front.service.TransactionService;
import com.chisom.user_front.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private UserService userService;

    @Autowired
    private PrimaryTransactionRepository primaryTransactionRepository;

    @Autowired
    private SavingsTransactionRepository savingsTransactionRepository;

    @Autowired
    private PrimaryAccountRepository primaryAccountRepository;

    @Autowired
    private SavingsAccountRepository savingsAccountRepository;

    @Autowired
    private RecipientRepository recipientRepository;

    public List<PrimaryTransaction> findPrimaryTransactionList(String username) {
        User user = userService.findByUsername(username);
        List<PrimaryTransaction> primaryTransactionList = user.getPrimaryAccount().getPrimaryTransactionsList();

        return primaryTransactionList;
    }

    public List<SavingsTransaction> findSavingsTransactionList(String username) {
        User user = userService.findByUsername(username);
        List<SavingsTransaction> savingsTransactionList = user.getSavingsAccount().getSavingsTransactionList();

        return savingsTransactionList;
    }

    public void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction) {
        primaryTransactionRepository.save(primaryTransaction);
    }

    public void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction) {
        savingsTransactionRepository.save(savingsTransaction);
    }

    public void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction) {
        primaryTransactionRepository.save(primaryTransaction);
    }

    public void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransaction) {
        savingsTransactionRepository.save(savingsTransaction);
    }

    public void betweenAccountTransfer(String transferFrom, String transferTo, String amount,
                                       PrimaryAccount primaryAccount, SavingsAccount savingsAccount) throws Exception {

        if (transferFrom.equalsIgnoreCase("Primary") && transferTo.equalsIgnoreCase("Savings")) {
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));

            primaryAccountRepository.save(primaryAccount);
            savingsAccountRepository.save(savingsAccount);

            Date date = new Date();

            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Between account transfer from "
                    + transferFrom + " to " + transferTo, " Account", "Finished", Double.parseDouble(amount),
                    primaryAccount.getAccountBalance(), primaryAccount);

            primaryTransactionRepository.save(primaryTransaction);

        } else if (transferFrom.equalsIgnoreCase("Savings") && transferTo.equalsIgnoreCase("Primary")) {
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));

            savingsAccountRepository.save(savingsAccount);
            primaryAccountRepository.save(primaryAccount);

            Date date = new Date();

            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Between account transfer from "
                    + transferFrom + " to " + transferTo, "Account", "Finished", Double.parseDouble(amount),
                    savingsAccount.getAccountBalance(), savingsAccount);

            savingsTransactionRepository.save(savingsTransaction);

        } else {
            throw new Exception("Invalid transfer");
        }
    }

    public List<Recipient> findRecipientList(Principal principal) {
        String username = principal.getName();
        /**here we are checking to see if this recipient is binding to the current user that
         * is logged in only the recipients that meets the condition will be reserved.
         *NOTE this method is not an efficient way to do this. because as the database
         *increases this method becomes too expensive to use,
         *a better way to do this is to write an sql query that will get what we want
         instead of getting all the records from the database and then filtering them out**/
        List<Recipient> recipientList = recipientRepository.findAll().stream()
                .filter(recipient -> username.equals(recipient.getUser().getUsername()))
                .collect(Collectors.toList());

        return recipientList;
    }

    public Recipient saveRecipient(Recipient recipient) {
        return recipientRepository.save(recipient);
    }

    public Recipient findRecipientByName(String recipientName) {
        return recipientRepository.findByName(recipientName);
    }

    public void deleteRecipientByName(String recipientName) {
        recipientRepository.deleteByName(recipientName);
    }

    public void toSomeoneElseTransfer(Recipient recipient, String accountType, String amount,
                                      PrimaryAccount primaryAccount, SavingsAccount savingsAccount) {

        if (accountType.equalsIgnoreCase("Primary")) {
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            primaryAccountRepository.save(primaryAccount);

            Date date = new Date();
            PrimaryTransaction primaryTransaction = new PrimaryTransaction(
                    date, "Transfer to recipient " + recipient.getName(),
                    "Transfer", "Finished", Double.parseDouble(amount),
                    primaryAccount.getAccountBalance(), primaryAccount);

            primaryTransactionRepository.save(primaryTransaction);

        } else if (accountType.equalsIgnoreCase("Savings")) {
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savingsAccountRepository.save(savingsAccount);

            Date date = new Date();
            SavingsTransaction savingsTransaction = new SavingsTransaction(
                    date, "Transfer to recipient " + recipient.getName(),
                    "Transfer", "Finished", Double.parseDouble(amount),
                    savingsAccount.getAccountBalance(), savingsAccount);

            savingsTransactionRepository.save(savingsTransaction);
        }
    }

}



















