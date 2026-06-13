package com.example.hospital.security;

import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.hospital.repository.TokenBlacklistRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
// Lớp này là "người gác cổng" - kiểm tra token trước khi request vào ứng dụng.
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1. Lấy token từ header Authorization
        final String authHeader = request.getHeader("Authorization");

        // 2. Nếu không có token → bỏ qua
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Chạy tiếp (sẽ bị chặn ở nơi khác)
            return;
        }

        // 3. Trích xuất token (bỏ "Bearer " ở đầu)
        final String token = jwtUtil.getTokenFromRequest(request);
        if (token != null && tokenBlacklistRepository.existsByToken(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        // 4. Lấy username từ token
        final String username = jwtUtil.extractUsername(token); // "admin"

        // 5. Nếu chưa đăng nhập thì kiểm tra token
        // username trong token phải hợp lệ
        // nếu đăng nhập r thì bỏ qua
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 5a. Lấy thông tin user từ database
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 5b. Kiểm tra token có hợp lệ không
            if (jwtUtil.isTokenValid(token, userDetails)) {
                // 5c. Tạo đối tượng authentication
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null,
                                userDetails.getAuthorities());

                // 5d. Lưu vào SecurityContext (đánh dấu user đã đăng nhập)
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 6. Cho request đi tiếp vào Controller
        filterChain.doFilter(request, response);
    }
}
