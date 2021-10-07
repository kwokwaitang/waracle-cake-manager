package com.waracle.cake_manager.service;

import com.waracle.cake_manager.model.UserDao;
import com.waracle.cake_manager.model.UserDto;
import com.waracle.cake_manager.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private static final List<GrantedAuthority> GRANTED_AUTHORITIES = new ArrayList<>();

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public JwtUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Pick-up the username and password from the database
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDao userDao = userRepository.findByUsername(username);
        if (userDao == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new org.springframework.security.core.userdetails.User(userDao.getUsername(), userDao.getPassword(),
                GRANTED_AUTHORITIES);
    }

    /**
     * From registration of the client's username and password, persist the details to the database
     *
     * @param user Details (username and password) regarding the client
     * @return The saved client
     */
    public UserDao save(final UserDto user) {
        UserDao newUser = new UserDao();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(newUser);
    }

    /**
     * From authentication, update the client's details with the generated JWT token (as that will be used from now
     * on in any further interactions)
     *
     * @param username
     * @param token
     * @return
     */
    public UserDao updateTokenByUsername(String username, String token) {
        UserDao updatedUser = userRepository.findByUsername(username);
        updatedUser.setToken(token);

        return userRepository.save(updatedUser);
    }

    public UserDao getUserDaoByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
