package com.finance.data_processor.service;

import com.finance.data_processor.dto.DashboardSummary;
import com.finance.data_processor.model.Transaction;
import com.finance.data_processor.model.User;
import com.finance.data_processor.repository.TransactionRepository;
import com.finance.data_processor.repository.UserRepository;
import jakarta.transaction.UserTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;

    public Transaction createTransaction(Transaction transaction) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        }  else {
            username = principal.toString();
        }

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username not found"));
        transaction.setUser(currentUser);

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username not found"));

        return transactionRepository.findByUserId(currentUser.getId());
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    public List<Transaction> getEveryTransactionInTheDatabase() {
        return transactionRepository.findAll();
    }

    public DashboardSummary getDashboardSummary() {
        List<Transaction> userTransactions = getAllTransactions();
        DashboardSummary dashboardSummary = new DashboardSummary();

        double income = userTransactions.stream()
                .filter(t -> "INCOME".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();
        double expense = userTransactions.stream()
                .filter(t -> "EXPENSE".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();
        dashboardSummary.setTotalIncome(income);
        dashboardSummary.setTotalExpense(expense);
        dashboardSummary.setNetBalance(income - expense);
        return dashboardSummary;

    }

    public List<Transaction> getFilteredTransactions(String type, String category) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User currentUser =  userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username not found"));

        return transactionRepository.findFilteredTransactions(currentUser.getId(), type, category);

    }
}