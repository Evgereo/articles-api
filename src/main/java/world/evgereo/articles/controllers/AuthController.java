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
import world.evgereo.articles.dtos.AuthRequestDto;
import world.evgereo.articles.dtos.AuthResponseDto;
import world.evgereo.articles.dtos.RefreshRequestDto;
import world.evgereo.articles.services.AuthService;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponseDto> giveAuthTokens(@RequestBody @Valid AuthRequestDto authRequest) {
        return new ResponseEntity<>(authService.createAuthTokens(authRequest), HttpStatus.OK);
    }

    @PostMapping(value = "/refresh", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponseDto> giveNewTokens(@RequestBody @Valid RefreshRequestDto refreshRequest) {
        return new ResponseEntity<>(authService.updateAuthTokens(refreshRequest), HttpStatus.OK);
    }
}
