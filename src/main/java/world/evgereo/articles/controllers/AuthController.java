package world.evgereo.articles.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import world.evgereo.articles.DTO.AuthRequestDTO;
import world.evgereo.articles.DTO.AuthResponseDTO;
import world.evgereo.articles.DTO.RefreshRequestDTO;
import world.evgereo.articles.errors.exceptions.AuthException;
import world.evgereo.articles.services.AuthService;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponseDTO> giveAuthTokens(@RequestBody @Valid AuthRequestDTO authRequest) {
        return new ResponseEntity<>(authService.createAuthTokens(authRequest), HttpStatus.OK);
    }

    @PostMapping(value = "/refresh", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponseDTO> giveNewTokens(@RequestBody @Valid RefreshRequestDTO refreshRequest) {
        return new ResponseEntity<>(authService.updateAuthTokens(refreshRequest), HttpStatus.OK);
    }
}
