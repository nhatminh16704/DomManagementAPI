package com.domhub.api.security;

import com.domhub.api.util.JwtUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;
import java.util.List;

import java.io.IOException;

public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Lấy token từ header Authorization
        String jwtToken = extractJwtFromRequest(request);

        // Kiểm tra nếu có token và token hợp lệ
        if (jwtToken != null && jwtUtil.isTokenValid(jwtToken)) {
            String username = jwtUtil.extractUsername(jwtToken);

            // Kiểm tra username trong token có khớp với người dùng hiện tại không (nếu có)
            if (username != null && jwtUtil.validateToken(jwtToken, username)) {
                // Lấy role từ JWT token
                String role = jwtUtil.extractRole(jwtToken);

                // Tạo danh sách authorities từ role
                List<SimpleGrantedAuthority> authorities;
                if (role != null && !role.isEmpty()) {
                    // Thêm prefix "ROLE_" theo quy ước của Spring Security
                    authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
                } else {
                    authorities = Collections.emptyList();
                }

                // Thiết lập thông tin vào SecurityContext với authorities
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Tiếp tục xử lý request
        filterChain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Lấy phần token (xóa "Bearer ")
        }
        return null;
    }
}
