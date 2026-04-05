package com.finance.data_processor.repository;

import com.finance.data_processor.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserId(Long userId);

    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId " +
            "AND (:type IS NULL OR :type = '' OR t.type = :type) " +
            "AND (:category IS NULL OR :category = '' OR t.category = :category)")
    List<Transaction> findFilteredTransactions(
            @Param("userId") Long userId,
            @Param("type") String type,
            @Param("category") String category
    );
}
