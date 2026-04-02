package com.finance.data_processor.repository;

import com.finance.data_processor.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
