package com.smiecj.hive;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.sasl.AuthenticationException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hive.service.auth.PasswdAuthenticationProvider;
import org.apache.log4j.Logger;

/**
 * Custom md5 authenticator
 * @author: smiecj
 */
public class CustomMD5Authenticator implements PasswdAuthenticationProvider {

    private static final Map<String, String> userPwdMap = new HashMap<String, String>();
    private static Logger LOG = Logger.getLogger(CustomMD5Authenticator.class);
    private static final String userPwdFilePathConfigKey = "hive.server2.custom.authentication.file";

    // constructor: get file store username and password and store them into map
    public CustomMD5Authenticator() {
        HiveConf hiveConf = new HiveConf();
        Configuration conf = new Configuration(hiveConf);
        String userPwdFilePath = conf.get(userPwdFilePathConfigKey);
        File userPwdFile = new File(userPwdFilePath);
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(userPwdFile));
            String userPwdStr;
            while ((userPwdStr = reader.readLine()) != null) {
                String[] splitArr = userPwdStr.split(":");
                if (splitArr.length != 2) {
                    continue;
                }
                String userName = splitArr[0];
                String password = splitArr[1];
                userPwdMap.put(userName, password);
            }
        } catch (IOException e) {
            LOG.error("[CustomMD5Authenticator] read config file error: " + e.getMessage());
        }
    }

    @Override
    public void Authenticate(String user, String password) throws AuthenticationException {
        LOG.debug("[Authenticate] current user: " + user + ", password: " + password);
        String md5Password = DigestUtils.md5Hex(password);
        String currentPwd = userPwdMap.get(user);
        if ("" != currentPwd && md5Password.equals(currentPwd)) {
            return;
        }
        throw new AuthenticationException("CustomSimpleAuthenticator: Error validating user: " + user);
    }

}
