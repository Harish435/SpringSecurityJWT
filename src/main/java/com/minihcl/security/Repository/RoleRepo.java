package com.minihcl.security.Repository;

import com.minihcl.security.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Roles,Long> {

    Roles findByName(String name);
}
