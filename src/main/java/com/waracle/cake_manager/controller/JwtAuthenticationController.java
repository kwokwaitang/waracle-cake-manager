package com.waracle.cake_manager.controller;

import com.waracle.cake_manager.advice.LogMethodAccess;
import com.waracle.cake_manager.config.JwtTokenUtil;
import com.waracle.cake_manager.model.JwtAuthenticationRequest;
import com.waracle.cake_manager.model.JwtAuthenticationResponse;
import com.waracle.cake_manager.model.UserDao;
import com.waracle.cake_manager.model.UserDto;
import com.waracle.cake_manager.service.JwtUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

    private static final Logger LOGGER = Logger.getGlobal();
    private static final String EMPTY_TOKEN = "";

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final JwtUserDetailsService userDetailsService;

    public JwtAuthenticationController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
                                       JwtUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Authenticate the client's details, to obtain a JWT token which will be persisted as part of the client's details
     *
     * @param authenticationRequest
     * @return
     */
    @LogMethodAccess
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) {
        try {
            // Initiate authentication...
            final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword());
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(authenticationRequest.getUsername());

            final String token = jwtTokenUtil.generateToken(userDetails);

            UserDao userDao = userDetailsService.updateTokenByUsername(authenticationRequest.getUsername(), token);
            LOGGER.info(() -> String.format("\tuserDao = [%s]", userDao));

            return ResponseEntity.ok(new JwtAuthenticationResponse(token));
        } catch (Exception e) {
            LOGGER.info(() -> String.format("\tAuthentication has generated an exception [%s]", e));
            return ResponseEntity.ok(new JwtAuthenticationResponse(EMPTY_TOKEN));
        }
    }

    /**
     * Just capture a username and a password, to be used later for authentication when attempting to gain a JWT token
     *
     * @param user with username and password details
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody UserDto user) throws Exception {
        return ResponseEntity.ok(userDetailsService.save(user));
    }
}
