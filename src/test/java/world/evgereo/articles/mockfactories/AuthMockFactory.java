package world.evgereo.articles.mockfactories;

import world.evgereo.articles.dtos.AuthRequestDto;
import world.evgereo.articles.dtos.AuthResponseDto;
import world.evgereo.articles.dtos.RefreshRequestDto;

public class AuthMockFactory {
    private static final String accessToken = "any.access.token";
    private static final String refreshToken = "any.refresh.token";

    public static AuthRequestDto getAuthRequestDto() {
        return new AuthRequestDto(
                "testfirst@gmail.com",
                "password"
        );
    }

    public static RefreshRequestDto getRefreshRequestDto() {
        return new RefreshRequestDto(
                AuthMockFactory.refreshToken
        );
    }

    public static AuthResponseDto getAuthResponseDto() {
        return new AuthResponseDto(
                AuthMockFactory.accessToken,
                AuthMockFactory.refreshToken
        );
    }

    public static String getAccessToken() {
        return AuthMockFactory.accessToken;
    }

    public static String getRefreshToken() {
        return AuthMockFactory.refreshToken;
    }
}
