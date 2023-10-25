package com.vcc.agile.project.mgmt.PowerNPride.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vcc.agile.project.mgmt.PowerNPride.config.JwtService;
import com.vcc.agile.project.mgmt.PowerNPride.dto.AuthenticationResponse;
import com.vcc.agile.project.mgmt.PowerNPride.dto.LoginRequest;
import com.vcc.agile.project.mgmt.PowerNPride.dto.RefreshTokenRequest;
import com.vcc.agile.project.mgmt.PowerNPride.dto.RegisterRequest;
import com.vcc.agile.project.mgmt.PowerNPride.exceptions.SpringPowerNPrideException;
import com.vcc.agile.project.mgmt.PowerNPride.model.*;
import com.vcc.agile.project.mgmt.PowerNPride.repository.TokenRepository;
import com.vcc.agile.project.mgmt.PowerNPride.repository.UserRepository;
import com.vcc.agile.project.mgmt.PowerNPride.repository.VerificationTokenRepository;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void signup (RegisterRequest registerRequest)
    {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        //user.setPassword(registerRequest.getPassword());
        user.setCreated(Instant.now());
        //user.setEnabled(false);
        user.setEnabled(true);
        user.setRole(Role.USER);

        userRepository.save(user);

        //String token = generateVerificationToken(user);

        //log.info("Activation token: http://localhost:8080/api/auth/accountVerification/" + token);

        /*mailService.sendMail(new NotificationEmail("Please Activate your Account",
                user.getEmail(), "Thank you for signing up to Spring Reddit, " +
                "please click on the below url to activate your account : " +
                "http://localhost:8080/api/auth/accountVerification/" + token));
                */
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);

        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringPowerNPrideException("Invalid Token"));
        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringPowerNPrideException("User not found with name: " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));

        User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new SpringPowerNPrideException("User not found with name: " + loginRequest.getUsername()));

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .expiresAt(Instant.now().plusMillis(jwtService.getExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {

        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);

    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + authentication.getName()));
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {

        String refreshToken = refreshTokenRequest.getRefreshToken();
        //String refreshToken = refreshTokenRequest.getRefreshToken().substring(7);
        String userName = jwtService.extractUsername(refreshToken);
        if (userName != null) {
            User user = this.userRepository.findByUsername(userName)
                    .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + userName));
            if (jwtService.isTokenValid(refreshToken, user)) {
                String accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);

                return AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshTokenRequest.getRefreshToken())
                        .expiresAt(Instant.now().plusMillis(jwtService.getExpirationInMillis()))
                        .username(userName)
                        .build();
            }
        }

        return null;
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUserId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken = authHeader.substring(7);
        String userName = jwtService.extractUsername(refreshToken);
        if (userName != null) {
            User user = this.userRepository.findByUsername(userName)
                    .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + userName));
            revokeAllUserTokens(user);
        }
    }
}
