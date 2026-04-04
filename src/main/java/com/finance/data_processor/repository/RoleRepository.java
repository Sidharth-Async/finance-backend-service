package com.finance.data_processor.repository;

import com.finance.data_processor.model.Role;
import com.finance.data_processor.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
