package world.evgereo.articles.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import world.evgereo.articles.dtos.AuthRequestDto;
import world.evgereo.articles.dtos.RefreshRequestDto;
import world.evgereo.articles.services.AuthService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static world.evgereo.articles.mockfactories.AuthMockFactory.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock
    private AuthService authService;
    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void giveAuthTokens() throws Exception {
        when(authService.createAuthTokens(any(AuthRequestDto.class))).thenReturn(getAuthResponseDTO());
        String authRequestJson = objectMapper.writeValueAsString(getAuthRequestDTO());
        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(authRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(getAccessToken()))
                .andExpect(jsonPath("$.refreshToken").value(getRefreshToken()));
        verify(authService, times(1)).createAuthTokens(any(AuthRequestDto.class));
    }

    @Test
    void giveNewTokens() throws Exception {
        when(authService.updateAuthTokens(any(RefreshRequestDto.class))).thenReturn(getAuthResponseDTO());
        String refreshRequestJson = objectMapper.writeValueAsString(getRefreshRequestDTO());
        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(refreshRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(getAccessToken()))
                .andExpect(jsonPath("$.refreshToken").value(getRefreshToken()));
        verify(authService, times(1)).updateAuthTokens(any(RefreshRequestDto.class));
    }
}
