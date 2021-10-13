package com.smiecj.hive;

import java.util.HashMap;
import java.util.Map;

import javax.security.sasl.AuthenticationException;

import org.apache.hive.service.auth.PasswdAuthenticationProvider;
import org.apache.log4j.Logger;

public class CustomSimpleAuthenticator implements PasswdAuthenticationProvider {

    private static final Map<String, String> userPwdMap = new HashMap<String, String>();
    private static Logger log = Logger.getLogger(CustomSimpleAuthenticator.class);

    public CustomSimpleAuthenticator() {
        userPwdMap.put("smiecj", "smiecj_123");
        userPwdMap.put("hive", "hive_123");
    }

    @Override
    public void Authenticate(String user, String password) throws AuthenticationException {
        log.debug("[Authenticate] current user: " + user + ", password: " + password);
        String currentPwd = userPwdMap.get(user);
        if ("" != currentPwd && password.equals(currentPwd)) {
            return;
        }
        throw new AuthenticationException("CustomSimpleAuthenticator: Error validating user: " + user);
    }
    
}
