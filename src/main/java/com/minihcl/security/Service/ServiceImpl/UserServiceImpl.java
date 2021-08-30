package com.minihcl.security.Service.ServiceImpl;

import com.minihcl.security.Repository.RoleRepo;
import com.minihcl.security.Repository.UserRepository;
import com.minihcl.security.Service.UserService;
import com.minihcl.security.model.AppUsers;
import com.minihcl.security.model.Roles;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
   private UserRepository userRepository;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUsers user=userRepository.findByUserName(username);
        if(user==null){
            logger.error("User Not Found in DataBase!!");
            throw new UsernameNotFoundException("User Not Found in DataBase!!");
        }else{
            logger.info("User  Found in DataBase!!"+username);
        }
        Collection<SimpleGrantedAuthority> authorities=new ArrayList<>();
        user.getRoles()
                .forEach(role->{authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getUserName(),user.getPassword(),authorities);
    }

    @Override
    public AppUsers saveusers(AppUsers users) {
        logger.info("Saving new user to database" +users.getName());
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        return userRepository.save(users);
    }

    @Override
    public Roles saveRoles(Roles role) {
        logger.info("Saving Role to database"+role.getName());
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, String rolename) {

        logger.info("Adding Role to the User"+ username,rolename);
        AppUsers user=userRepository.findByUserName(username);
        Roles role=roleRepo.findByName(rolename);
        user.getRoles().add(role);
    }

    @Override
    public AppUsers getUsers(String username) {
        logger.info("Fetching Users Based On UserName");
        return userRepository.findByUserName(username);
    }

    @Override
    public List<AppUsers> getUsers() {
        logger.info("Fetching Users");
        return userRepository.findAll();
    }

}
