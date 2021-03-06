package com.voyageone.security.shiro.credentials;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;

import java.util.concurrent.atomic.AtomicInteger;


public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    // 密码加密固定盐值
    public static final String MD5_FIX_SALT = "crypto.voyageone.la";
    // 密码加密散列加密次数
    public static final int MD5_HASHITERATIONS = 4;

    private Cache<String, AtomicInteger> passwordRetryCache;

    private static final String RETRY_PRE_FIX = "_____retry_";

    public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager) {
//        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token,
                                      AuthenticationInfo info) {
        String username = (String) token.getPrincipal();

        String password = String.valueOf(((UsernamePasswordToken) token).getPassword());

        // retry count + 1
        //用redis做缓存了，先把这段逻辑注释掉
//        AtomicInteger retryCount = passwordRetryCache.get(RETRY_PRE_FIX + username);
//
//        if (retryCount == null) {
//            retryCount = new AtomicInteger(0);
//            passwordRetryCache.put(RETRY_PRE_FIX + username, retryCount);
//        }
//
//        if (retryCount.incrementAndGet() > 10) {
//            // if retry count > 5 throw
//            throw new ExcessiveAttemptsException();
//        }

        //把MySimpleAuthenticationInfo变回SimpleAuthenticationInfo
        SimpleAuthenticationInfo sInfo =  new SimpleAuthenticationInfo();
        sInfo.setPrincipals(info.getPrincipals());
        sInfo.setCredentials(info.getCredentials());
        if(info instanceof  MySimpleAuthenticationInfo) {
            sInfo.setCredentialsSalt(ByteSource.Util.bytes(((MySimpleAuthenticationInfo) info).getCredentialsSalt()));
        }

        boolean matches = super.doCredentialsMatch(token, sInfo);

        //尝试用老密码登录
        if (!matches) {
            String cryptoPassword = new Md5Hash(password, username + MD5_FIX_SALT, MD5_HASHITERATIONS).toHex();

            if (cryptoPassword.equals(info.getCredentials())) {
                matches = true;
            }
        }

//        matches = true;
        //用redis做缓存了，先把这段逻辑注释掉
//        if (matches) {
//            // clear retry count
//            passwordRetryCache.remove(RETRY_PRE_FIX + username);
//        }

        return matches;
    }

    /**
     * build user password
     */
    public String buildCredentials(String userName, String password, String credentialsSalt) {
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName, password, ByteSource.Util.bytes(userName + credentialsSalt), userName);
        AuthenticationToken token = new UsernamePasswordToken(userName, password);
        return super.hashProvidedCredentials(token, authenticationInfo).toString();
    }
}
