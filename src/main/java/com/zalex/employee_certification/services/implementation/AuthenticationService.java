package com.zalex.employee_certification.services.implementation;

import com.zalex.employee_certification.mappers.AuthenticationMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    
    @Value("${zalex.api.key.header:apiKeyHeader}")
    private String apiKeyHeader;
    
    @Value("${zalex.api.key:AuthKey}")
    private String authKey;
    
    public Authentication getAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader(apiKeyHeader);
        if(apiKey == null || !apiKey.equals(authKey)) {
            throw new BadCredentialsException("Invalid API Key");
        }

        return new AuthenticationMapper(apiKey, AuthorityUtils.NO_AUTHORITIES);
    }
}
