package prime.video.controller;

import prime.video.api.AuthenticationApiDelegate;
import prime.video.model.LoginRequest;
import prime.video.model.RegisterRequest;
import prime.video.model.TokenResponse;
import prime.video.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationApiDelegateImpl implements AuthenticationApiDelegate {

    private final AuthService authService;

    public AuthenticationApiDelegateImpl(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public ResponseEntity<TokenResponse> registerAccount(RegisterRequest registerRequest) {
        authService.register(registerRequest.getEmail(), registerRequest.getPassword(), registerRequest.getCountry());
        String token = authService.login(registerRequest.getEmail(), registerRequest.getPassword());
        
        TokenResponse response = new TokenResponse();
        response.setAccessToken(token);
        response.setTokenType("Bearer");
        response.setExpiresIn(3600);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<TokenResponse> login(LoginRequest loginRequest) {
        String token = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        
        TokenResponse response = new TokenResponse();
        response.setAccessToken(token);
        response.setTokenType("Bearer");
        response.setExpiresIn(3600);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
