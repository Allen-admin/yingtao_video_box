package com.k365.config.jwt;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author Gavin
 * @date 2019/6/20 18:29
 * @description：
 */
@Data
@Builder
public class JwtToken implements AuthenticationToken {

    private String token;

    private String username;

    private String password;

    private String uid;

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

}
