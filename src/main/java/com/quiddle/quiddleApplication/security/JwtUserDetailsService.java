package com.quiddle.quiddleApplication.security;

import com.quiddle.quiddleApplication.models.Permission;
import com.quiddle.quiddleApplication.models.Role;
import com.quiddle.quiddleApplication.models.User;
import com.quiddle.quiddleApplication.services.UserService;
import org.springframework.context.ApplicationContextException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class JwtUserDetailsService implements UserDetailsService {

    public final UserService userService;

    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByUsername(username).orElseThrow(() -> new ApplicationContextException("User does not exist"));
        user.setAuthorities(getAuthorities(user));

        return user;
    }

    private Collection<GrantedAuthority> getAuthorities(User user){
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        Role role = user.getRole();

        authorities.add(new SimpleGrantedAuthority(role.getName()));

        List<Permission> permissions = role.getPermissions();

        permissions.stream().forEach((permission) -> {
            authorities.add(new SimpleGrantedAuthority(permission.getName()));
        });

        return authorities;
    }
}
