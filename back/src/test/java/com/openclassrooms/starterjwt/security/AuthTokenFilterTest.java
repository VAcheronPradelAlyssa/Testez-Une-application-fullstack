package com.openclassrooms.starterjwt.security;

import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collections;

import static org.mockito.Mockito.*;

public class AuthTokenFilterTest {

    private JwtUtils jwtUtils;
    private UserDetailsServiceImpl userDetailsService;
    private AuthTokenFilter authTokenFilter;

    @BeforeEach
    void setUp() {
        jwtUtils = mock(JwtUtils.class);
        userDetailsService = mock(UserDetailsServiceImpl.class);
        authTokenFilter = new AuthTokenFilter();
        // Injection manuelle car @Autowired ne fonctionne pas en test unitaire simple
        authTokenFilter.jwtUtils = jwtUtils;
        authTokenFilter.userDetailsService = userDetailsService;
    }

    @Test
    void doFilterInternal_validJwt_setsAuthentication() throws ServletException, IOException {
        String jwt = "valid.jwt.token";
        String username = "user@example.com";
        UserDetails userDetails = new User(username, "password", Collections.emptyList());

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + jwt);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        when(jwtUtils.validateJwtToken(jwt)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(jwt)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        authTokenFilter.doFilter(request, response, filterChain);

        // Vérifie que le filterChain continue
        verify(filterChain).doFilter(request, response);
        // Vérifie que l'utilisateur a bien été authentifié
        assert org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication() != null;
        assert org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals(userDetails);
    }

    @Test
    void doFilterInternal_noJwt_doesNotSetAuthentication() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        authTokenFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assert org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication() == null;
    }
    @Test
void doFilterInternal_exceptionInFilter_logsErrorAndContinues() throws ServletException, IOException {
    String jwt = "valid.jwt.token";
    String username = "user@example.com";

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Authorization", "Bearer " + jwt);
    MockHttpServletResponse response = new MockHttpServletResponse();
    FilterChain filterChain = mock(FilterChain.class);

    when(jwtUtils.validateJwtToken(jwt)).thenReturn(true);
    when(jwtUtils.getUserNameFromJwtToken(jwt)).thenReturn(username);
    // On force une exception ici
    when(userDetailsService.loadUserByUsername(username)).thenThrow(new RuntimeException("Erreur simulée"));

    authTokenFilter.doFilter(request, response, filterChain);

    // Le filterChain doit être appelé même en cas d'exception
    verify(filterChain).doFilter(request, response);
    // Le contexte de sécurité ne doit pas être rempli
    assert org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication() == null;
}
}