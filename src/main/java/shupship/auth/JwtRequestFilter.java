package shupship.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import shupship.common.Const;
import shupship.service.JwtUserDetailsService;
//import shupship.service.impl.UserDetailsImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tuandv
 * The JwtRequestFilter extends the Spring Web Filter OncePerRequestFilter class. For any incoming request this Filter class gets executed.
 * It checks if the request has a valid JWT token. If it has a valid JWT Token then it sets the Authentication in the context,
 * to specify that the current user is authenticated.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {


        String requestURL = request.getRequestURI();
        System.out.println(requestURL);
        try {
            // JWT Token is in the form "Bearer token". Remove Bearer word and get
            // only the Token
            String jwtToken = jwtTokenUtil.extractJwtFromRequest(request);
            if (requestURL.startsWith("/user/refresh-token")) {
                Claims claims = jwtTokenUtil.getAllClaimsFromToken(jwtToken);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        null, null, null);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                request.setAttribute("claims", claims);

            } else {
                if (StringUtils.hasText(jwtToken)) {
                    String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                    // Once we get the token validate it.
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails =  jwtUserDetailsService.loadUserByUsername(username);
                        // if token is valid configure Spring Security to manually set
                        // authentication

                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken
                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        // After setting the Authentication in the context, we specify
                        // that the current user is authenticated. So it passes the
                        // Spring Security Configurations successfully.
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//                        }
                    }
                }
            }

        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            Map<String, Object> result = new HashMap<>();
            result.put("code", Const.API_RESPONSE.RETURN_CODE_ERROR);
            result.put("success", Const.API_RESPONSE.RESPONSE_STATUS_FALSE);
            result.put("description", null);
            result.put("message", e.getMessage());
            result.put("data", null);
            printWriter.print(new ObjectMapper().writeValueAsString(result));
            printWriter.close();
            return;
        } catch (ExpiredJwtException e) {
            logger.error("Token has expired");
            String isRefreshToken = request.getHeader("isRefreshToken");
            // allow for Refresh Token creation if following conditions are true.
            if (isRefreshToken != null && isRefreshToken.equals("true") && requestURL.contains("refresh-token")) {
                allowForRefreshToken(e, request);
            } else {
                request.setAttribute("exception", e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        chain.doFilter(request, response);
    }

    private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {

        // create a UsernamePasswordAuthenticationToken with null values.
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                null, null, null);
        // After setting the Authentication in the context, we specify
        // that the current user is authenticated. So it passes the
        // Spring Security Configurations successfully.
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        // Set the claims so that in controller we will be using it to create
        // new JWT
        request.setAttribute("claims", ex.getClaims());

    }

}
