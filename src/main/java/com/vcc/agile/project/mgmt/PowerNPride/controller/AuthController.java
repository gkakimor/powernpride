package com.vcc.agile.project.mgmt.PowerNPride.controller;

import com.vcc.agile.project.mgmt.PowerNPride.dto.AuthenticationResponse;
import com.vcc.agile.project.mgmt.PowerNPride.dto.LoginRequest;
import com.vcc.agile.project.mgmt.PowerNPride.dto.RefreshTokenRequest;
import com.vcc.agile.project.mgmt.PowerNPride.dto.RegisterRequest;
import com.vcc.agile.project.mgmt.PowerNPride.exceptions.SpringPowerNPrideException;
import com.vcc.agile.project.mgmt.PowerNPride.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest)
    {
        try{
            authService.signup(registerRequest);
            return new ResponseEntity<>("User Registration Successful", HttpStatus.OK);
        } catch (SpringPowerNPrideException spne){
            return new ResponseEntity<>(spne.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token)
    {
        authService.verifyAccount(token);
        return new ResponseEntity<>("<h1>Account activated successfully<h1>", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request,
                                         HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.status(OK).body("Refresh Token Deleted Successfully!!");
    }
}
