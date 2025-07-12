package com.example.tabi.login;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtVerificationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        AntPathMatcher pathMatcher = new AntPathMatcher();

        return pathMatcher.match("/api/auth/login", path) || pathMatcher.match("/api/auth/refresh", path) || pathMatcher.match("/api/app-user/initial/**", path);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if(header == null || !header.startsWith("Bearer ")){
            // access 토큰이 없는 경우
            filterChain.doFilter(request,response); // -> UsernamePasswordJwtLoginFilter로 넘어가
            return;
        }

        String token = header.replace("Bearer ", "");
        String email;
        try {
            email = jwtUtil.extractAccessTokenLoginId(token);
        } catch (ExpiredJwtException e) {
            log.info("엑세스 토큰 만료");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Expiration Token");
            return;
        } catch (JwtException e) {
            log.info("유효하지 않은 토큰");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
            return;
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(email);

            log.info("customUserDetails:{}",customUserDetails);

            if (jwtUtil.validateAccessToken(token, customUserDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.info("Authentication:{}",authentication);
            }
        }

        filterChain.doFilter(request,response);
    }
}
