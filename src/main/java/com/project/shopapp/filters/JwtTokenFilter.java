package com.project.shopapp.filters;

import com.project.shopapp.components.JwtTokenUtils;
import com.project.shopapp.models.User;
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
public class JwtTokenFilter extends OncePerRequestFilter{
    @Value("${api.prefix}")
    private String apiPrefix;
    private final UserDetailsService userDetailsService;//thông tin chi tiết người dùng từ cơ sở dữ liệu.
    private final JwtTokenUtils jwtTokenUtil;//tiện ích để làm việc với JWT, chẳng hạn như tạo, xác thực và trích xuất thông tin từ JWT.
    @Override
    protected void doFilterInternal(@NonNull  HttpServletRequest request,//Nó xử lý từng yêu cầu HTTP để kiểm tra và xác thực JWT.
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if(isBypassToken(request)) {//Kiểm tra xem yêu cầu có được phép bỏ qua kiểm tra JWT không.
                filterChain.doFilter(request, response); //enable bypass
                return;
            }
            final String authHeader = request.getHeader("Authorization");//Lấy tiêu đề Authorization từ yêu cầu.
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {//Kiểm tra xem tiêu đề có bắt đầu bằng Bearer không.
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
            final String token = authHeader.substring(7);//Bỏ qua phần "Bearer " để lấy token thực tế.
            final String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);//Trích xuất số điện thoại từ token.
            if (phoneNumber != null
                    && SecurityContextHolder.getContext().getAuthentication() == null) {//Kiểm tra xem có người dùng đã xác thực trong ngữ cảnh bảo mật chưa.
                User userDetails = (User) userDetailsService.loadUserByUsername(phoneNumber);//Tải thông tin người dùng từ số điện thoại.
                if(jwtTokenUtil.validateToken(token, userDetails)) {// Xác thực token với thông tin người dùng.
                    UsernamePasswordAuthenticationToken authenticationToken =//Tạo đối tượng xác thực với thông tin người dùng.
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));// Thiết lập chi tiết xác thực cho đối tượng.
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);// Thiết lập ngữ cảnh bảo mật với đối tượng xác thực.
                }
            }
            filterChain.doFilter(request, response); //enable bypass, // Tiếp tục xử lý chuỗi bộ lọc
        }catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }

    }
    private boolean isBypassToken(@NonNull HttpServletRequest request) {//không cần kiểm tra token
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("%s/roles", apiPrefix), "GET"),
                Pair.of(String.format("%s/products", apiPrefix), "GET"),
                Pair.of(String.format("%s/orders", apiPrefix), "GET"),
                Pair.of(String.format("%s/categories", apiPrefix), "GET"),
                Pair.of(String.format("%s/users/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/users/login", apiPrefix), "POST"),
                Pair.of(String.format("%s/products/uploads/", apiPrefix), "POST")//thêm ảnh qua postman
                );

        for (Pair<String, String> bypassToken : bypassTokens) {
            if (request.getServletPath().contains(bypassToken.getFirst()) &&
                    request.getMethod().equals(bypassToken.getSecond())) {
                return true;
            }
        }



        return false;
    }

}
