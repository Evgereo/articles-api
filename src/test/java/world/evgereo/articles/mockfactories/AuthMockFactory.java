package world.evgereo.articles.mockfactories;

import world.evgereo.articles.DTOs.AuthRequestDTO;


public class AuthMockFactory {
    private static final String accessToken = "any.access.token";
    private static final String refreshToken = "any.refresh.token";

    public static AuthRequestDTO getAuthRequestDTO() {
        return new AuthRequestDTO(
                "testfirst@gmail.com",
                "password"
        );
    }

    public static String getAccessToken() {
        return AuthMockFactory.accessToken;
    }

    public static String getRefreshToken() {
        return AuthMockFactory.refreshToken;
    }
}
