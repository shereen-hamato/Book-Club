package com.ebookclub.ebookclub.security;

import com.ebookclub.ebookclub.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

/**
 * Filter for Java Web Token Authentication and Authorization
 */
public class JwtTokenFilter extends GenericFilterBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenFilter.class);

    private EBookClubUserDetailsService userDetailsService;

    private LoginUser loginUser;


    public JwtTokenFilter(EBookClubUserDetailsService userDetailsService, LoginUser loginUser ) {
        this.userDetailsService = userDetailsService;
        this.loginUser= loginUser;
    }

    /**
     * Determine if there is a JWT as part of the HTTP Request Header.
     * If it is valid then set the current context With the Authentication(user and roles) found in the token
     *
     * @param req Servlet Request
     * @param res Servlet Response
     * @param filterChain Filter Chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        LOGGER.info("Process request to check for a JSON Web Token ");

        String headerValue = ((HttpServletRequest)req).getHeader("Authorization");
            //Pull the Username  from the JWT to construct the user details
        loginUser.set(null);
        if(headerValue!=null)
        {
            userDetailsService.loadUserByJwtToken(headerValue).ifPresent(userDetails -> {
                //Add the user details (Permissions) to the Context for just this API invocation
                SecurityContextHolder.getContext().setAuthentication(
                        new PreAuthenticatedAuthenticationToken(userDetails, "", userDetails.getAuthorities()));
                loginUser.set(userDetailsService.getLoginUser(headerValue));
                LOGGER.info("Current Logged user {} ",LoginUser.get().getUsername());
            });

        }



        filterChain.doFilter(req, res);
    }

}