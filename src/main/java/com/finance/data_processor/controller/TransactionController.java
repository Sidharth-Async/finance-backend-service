package com.finance.data_processor.controller;


import com.finance.data_processor.dto.DashboardSummary;
import com.finance.data_processor.model.Transaction;
import com.finance.data_processor.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {


    private final TransactionService transactionService;
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Create a new transaction
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody Transaction transaction) {

        Transaction savedTransaction = transactionService.createTransaction(transaction);
        return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
    }

    // Get all transactions
    @GetMapping
    public ResponseEntity<List<Transaction>> getMyTransactions(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category) {
        List<Transaction> transactions = transactionService.getFilteredTransactions(type, category);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    // for admin only
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Transaction>> getEverySingleTransaction() {
        List<Transaction> transactions = transactionService.getEveryTransactionInTheDatabase();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    // Get a specific transaction by ID
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        Transaction transaction = transactionService.getTransactionById(id);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    // Delete a transaction
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummary> getDashboardSummary() {
        DashboardSummary summary = transactionService.getDashboardSummary();
        return new ResponseEntity<>(summary, HttpStatus.OK);
    }
}