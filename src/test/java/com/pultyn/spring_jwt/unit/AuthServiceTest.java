package com.pultyn.spring_jwt.unit;

import com.pultyn.spring_jwt.dto.UserDTO;
import com.pultyn.spring_jwt.enums.UserRole;
import com.pultyn.spring_jwt.model.UserEntity;
import com.pultyn.spring_jwt.repository.UserRepository;
import com.pultyn.spring_jwt.request.LoginRequest;
import com.pultyn.spring_jwt.request.RegisterRequest;
import com.pultyn.spring_jwt.response.LoginResponse;
import com.pultyn.spring_jwt.response.RegisterResponse;
import com.pultyn.spring_jwt.security.JwtService;
import com.pultyn.spring_jwt.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    AuthenticationManager authManager;
    @Mock
    JwtService jwtService;
    @InjectMocks
    AuthService authService;

    private static final String EMAIL = "user@example.com";
    private static final String PASSWORD = "passwd";

    @Test
    void register_success() {
        when(userRepository.existsByEmail(EMAIL)).thenReturn(false);

        when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        UserEntity user = new UserEntity(
                null,
                PASSWORD,
                EMAIL,
                Set.of(UserRole.BOOK_USER),
                new ArrayList<>());

        RegisterRequest req = new RegisterRequest(EMAIL, PASSWORD, PASSWORD);
        RegisterResponse res = new RegisterResponse(String.format("%s registered!", EMAIL), new UserDTO(user));
        assertThat(authService.register(req)).isEqualTo(res);

        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue()).isEqualTo(user);
    }

    @Test
    void register_userExists() {
        when(userRepository.existsByEmail(EMAIL)).thenReturn(true);

        RegisterRequest req = new RegisterRequest(EMAIL, PASSWORD, PASSWORD);
        assertThatThrownBy(() -> authService.register(req))
                .isInstanceOf(IllegalArgumentException.class)
                .message().isEqualTo("User already exists");
    }

    @Test
    void login_success() {
        LoginRequest req = new LoginRequest(EMAIL, PASSWORD);

        Authentication mockAuth = mock(Authentication.class);
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuth);

        UserEntity user = UserEntity.builder()
                .id(1L)
                .email(EMAIL)
                .password(PASSWORD)
                .reviews(new ArrayList<>())
                .roles(Set.of(UserRole.BOOK_USER))
                .build();
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        when(jwtService.generateToken(user)).thenReturn("token");
        when(jwtService.getJwtExpiration()).thenReturn(3_600_000L);

        LoginResponse res = authService.login(req);

        assertThat(res.getToken()).isEqualTo("token");
        assertThat(res.getExpires_in()).isEqualTo(3_600_000L);

        verify(authManager).authenticate(
                argThat(t -> t.getPrincipal().equals(EMAIL) &&
                        t.getCredentials().equals(PASSWORD)));
        verify(jwtService).generateToken(user);
    }

    @Test
    void login_userMissing() {
        LoginRequest req = new LoginRequest(EMAIL, PASSWORD);

        when(authManager.authenticate(any())).thenReturn(mock(Authentication.class));
        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(req))
                .isInstanceOf(IllegalStateException.class)
                .message().isEqualTo("User was authenticated but not found in database");
    }

    @Test
    void login_badCredentials() {
        LoginRequest req = new LoginRequest(EMAIL, PASSWORD);

        when(authManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("bad password"));

        assertThatThrownBy(() -> authService.login(req))
                .isInstanceOf(BadCredentialsException.class);
    }
}
