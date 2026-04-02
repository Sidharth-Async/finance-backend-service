package com.finance.data_processor.repository;

import com.finance.data_processor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
