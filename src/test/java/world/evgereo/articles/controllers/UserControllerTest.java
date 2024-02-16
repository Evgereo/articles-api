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
import world.evgereo.articles.DTOs.UpdateUserDTO;
import world.evgereo.articles.services.UserService;
import world.evgereo.articles.security.filters.JwtFilter;
import world.evgereo.articles.security.utils.JwtTokenUtils;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static world.evgereo.articles.mockfactories.UserMockFactory.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;
    @Mock
    JwtTokenUtils jwtTokenUtils;
    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .addFilters(new JwtFilter(jwtTokenUtils))
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getUsers() throws Exception {
    when(userService.getUsers()).thenReturn(getListOfTwoUsers());
    mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
    verify(userService, times(1)).getUsers();
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
    void updateUser() throws Exception {
        when(userService.updateUser(any(UpdateUserDTO.class), eq(1))).thenReturn(getFirstUser());
        String userJson = objectMapper.writeValueAsString(getUpdateUserDTO());
        mockMvc.perform(patch("/users/{id}?edit", 1)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1));
        verify(userService, times(1)).updateUser(any(UpdateUserDTO.class), eq(1));
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/{id}?delete", 1))
                .andExpect(status().isNoContent());
        verify(userService, times(1)).deleteUser(1);
    }
}