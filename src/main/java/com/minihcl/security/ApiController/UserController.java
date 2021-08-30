package com.minihcl.security.ApiController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minihcl.security.Service.UserService;
import com.minihcl.security.model.AppUsers;
import com.minihcl.security.model.Roles;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<AppUsers>> getUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/users/save")
    public ResponseEntity<AppUsers> saveUsers(@RequestBody AppUsers appUsers){
        URI uri=URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveusers(appUsers));
    }
    @PostMapping("/roles/save")
    public ResponseEntity<Roles> saveRole(@RequestBody Roles role){
        URI uri=URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/roles/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRoles(role));
    }
    @PostMapping("/roles/addtousers")
    public ResponseEntity<?> RoleTouser(@RequestBody RoleToUsersfor form){
        userService.addRoleToUser(form.getUserName(),form.getRoleName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authoHeader=request.getHeader(AUTHORIZATION);
        if(authoHeader!=null && authoHeader.startsWith("Bearer ")){
            try{
                String refresh_token=authoHeader.substring(7);
                Algorithm algorithm=Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier= JWT.require(algorithm).build();
                DecodedJWT decodedJWT=verifier.verify(refresh_token);
                String userName=decodedJWT.getSubject();
                AppUsers user= userService.getUsers(userName);

                String access_token= JWT.create()
                        .withSubject(user.getUserName())
                        .withExpiresAt(new Date(System.currentTimeMillis()+ 10*60*1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",user.getRoles().stream().map(Roles::getName).collect(Collectors.toList()))
                        .sign(algorithm);

                //To get Everything in Json format
                Map<String,String> token=new HashMap<>();
                token.put("access_token",access_token);
                token.put("refresh_token",refresh_token);

                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),token);

            }catch (Exception e){
               // logger.error("Error logging in"+e.getMessage());
                response.setHeader("error",e.getMessage());
                response.setStatus(FORBIDDEN.value());
                // response.sendError(FORBIDDEN.value());
                Map<String,String> error=new HashMap<>();
                error.put("error_message",e.getMessage());


                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }

        }else{
            throw new RuntimeException("Refresh Token Is Missing!!!");
        }
    }


}
@Data
class RoleToUsersfor{
    private String userName;
    private String roleName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}