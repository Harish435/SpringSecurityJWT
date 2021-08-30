package com.minihcl.security.Service;

import com.minihcl.security.model.AppUsers;
import com.minihcl.security.model.Roles;

import java.util.List;

public interface UserService {

    AppUsers saveusers(AppUsers users);

    Roles saveRoles(Roles role);

    void addRoleToUser(String username,String rolename);

    AppUsers getUsers(String username);

    List<AppUsers> getUsers();
}
