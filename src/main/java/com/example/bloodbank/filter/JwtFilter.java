package com.example.bloodbank.filter;

import com.example.bloodbank.service.implementation.MyUserDetailsService;
import com.example.bloodbank.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailsService myUserDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        String token=null;
        String username=null;

        if(authorization != null && authorization.startsWith("Bearer ")){
            token=authorization.substring(7);
            username=jwtUtil.extractUsername(token);
        }

        if(token != null && username != null){
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
            ArrayList<String> roles = jwtUtil.extractClaim(token, claims -> claims.get("roles", ArrayList.class));
            List<SimpleGrantedAuthority> grantedAuthorityList=new ArrayList<>();
            for(String role: roles){
                SimpleGrantedAuthority s=new SimpleGrantedAuthority(role);
                grantedAuthorityList.add(s);
            }
            if(jwtUtil.validateToken(token,userDetails) && SecurityContextHolder.getContext().getAuthentication() == null){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(username,null,grantedAuthorityList);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
