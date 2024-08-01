package com.ntndev.ecommercespringboot.filters;

import com.ntndev.ecommercespringboot.components.JwtTokenUtil;
import com.ntndev.ecommercespringboot.models.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    @Value("${api.prefix}")
    private String apiPrefix;

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    // Phương thức xử lý lọc chính
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            // Bỏ qua kiểm tra token cho một số yêu cầu nhất định
            if (isBypassToken(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            // Lấy tiêu đề Authorization từ yêu cầu
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }

            // Trích xuất token từ tiêu đề Authorization
            final String token = authHeader.substring(7);
            final String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);

            // Nếu số điện thoại không rỗng và chưa được xác thực
            if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Lấy chi tiết người dùng từ UserDetailsService
                User userDetails = (User) userDetailsService.loadUserByUsername(phoneNumber);

                // Kiểm tra token có hợp lệ hay không
                if (jwtTokenUtil.validateToken(token, userDetails)) {
                    // Tạo đối tượng xác thực và đặt vào SecurityContext
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            // Tiếp tục xử lý chuỗi lọc
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // Gửi lỗi nếu có ngoại lệ xảy ra
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }

    // Phương thức kiểm tra các yêu cầu cần bỏ qua kiểm tra token
    private boolean isBypassToken(@NonNull HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("%s/products", apiPrefix), "GET"),
                Pair.of(String.format("%s/categories", apiPrefix), "GET"),
                Pair.of(String.format("%s/users/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/users/login", apiPrefix), "POST")
        );

        // Kiểm tra từng cặp đường dẫn và phương thức
        for (Pair<String, String> bypassToken : bypassTokens) {
            if (request.getServletPath().contains(bypassToken.getFirst()) && request.getMethod().equals(bypassToken.getSecond())) {
                return true;
            }
        }
        return false;
    }
}
