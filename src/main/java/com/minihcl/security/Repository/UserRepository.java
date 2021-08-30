package com.minihcl.security.Repository;

import com.minihcl.security.model.AppUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUsers,Long> {
    AppUsers findByUserName(String username);
}
