package com.model1.application.security;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFillter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //Lấy token từ cookie
        String token;
        Cookie cookie = WebUtils.getCookie(httpServletRequest, "JWT_TOKEN");
        if (cookie != null) {
            token = cookie.getValue();
        } else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        // Parse thông tin từ token
        Claims claims = jwtTokenUtil.getClaimsFromToken(token);
        if (claims == null) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        // Tạo object Authentication
        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(claims);
        if (authenticationToken == null) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

//        authenticationToken.setDetails(new org.springframework.security.web.authentication.WebAuthenticationDetailsSource()
//                .buildDetails(httpServletRequest));

        // Xác thực thành công, lưu object Authentication vào SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(Claims claims) {
        String username = claims.getSubject();

        if (username != null) {
            UserDetails user = userDetailsService.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        }
        return null;
    }
}
