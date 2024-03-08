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
import world.evgereo.articles.dtos.RegistrationUserDto;
import world.evgereo.articles.services.UserService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static world.evgereo.articles.mockfactories.UserMockFactory.getFirstUser;
import static world.evgereo.articles.mockfactories.UserMockFactory.getValidRegistrationUserDTO;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private RegistrationController registrationController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController)
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createUser() throws Exception {
        when(userService.createUser(any(RegistrationUserDto.class))).thenReturn(getFirstUser());
        String userJson = objectMapper.writeValueAsString(getValidRegistrationUserDTO());
        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1));
        verify(userService, times(1)).createUser(any(RegistrationUserDto.class));
    }
}
