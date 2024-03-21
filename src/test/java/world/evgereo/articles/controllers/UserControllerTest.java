package world.evgereo.articles.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.StringEndsWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import world.evgereo.articles.dtos.PasswordUserDto;
import world.evgereo.articles.dtos.UpdateUserDto;
import world.evgereo.articles.security.jwt.JwtFilter;
import world.evgereo.articles.security.jwt.JwtTokenUtils;
import world.evgereo.articles.services.UserService;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static world.evgereo.articles.mockfactories.UserMockFactory.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private JwtTokenUtils jwtTokenUtils;
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .addFilters(new JwtFilter(jwtTokenUtils))
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isMovedPermanently())
                .andExpect(header().string("Location", new StringEndsWith("/users?page=0&size=10")));
    }

    @Test
    void getPaginatedUsers() throws Exception {
        when(userService.getPaginatedUsers(0, 10)).thenReturn(getPageOfTwoUsers());
        mockMvc.perform(get("/users?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(header().exists("Link"));
        verify(userService, times(1)).getPaginatedUsers(0, 10);
    }

    @Test
    void getUser() throws Exception {
        when(userService.loadUserById(1)).thenReturn(getFirstUser());
        mockMvc.perform(get("/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1));
        verify(userService, times(1)).loadUserById(1);
    }

    @Test
    void editUser() throws Exception {
        when(userService.updateUser(any(UpdateUserDto.class), eq(1))).thenReturn(getFirstUser());
        String userJson = objectMapper.writeValueAsString(getUpdateUserDto());
        mockMvc.perform(patch("/users/{id}?edit", 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1));
        verify(userService, times(1)).updateUser(any(UpdateUserDto.class), eq(1));
    }

    @Test
    void editPasswordOfUser() throws Exception {
        when(userService.updatePassword(any(PasswordUserDto.class), eq(1))).thenReturn(getFirstUser());
        String passwordJson = objectMapper.writeValueAsString(getValidPasswordUserDto());
        mockMvc.perform(patch("/users/{id}?password", 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(passwordJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1));
        verify(userService, times(1)).updatePassword(any(PasswordUserDto.class), eq(1));
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/{id}?delete", 1))
                .andExpect(status().isNoContent());
        verify(userService, times(1)).deleteUser(1);
    }
}
