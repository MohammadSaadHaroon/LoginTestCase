package com.example.loginusecase;

import android.accounts.NetworkErrorException;

public class LoginUseCaseSync {


    public enum UseCaseResult{
        SUCCESS,
        NETWORK_ERROR,
        FAILURE
    }
    private final LoginHttpEndpointSync mLoginHttpEndpointSync;
    private final AuthTokenCache mAuthTokenCache;
    private final EventBusPoster mEventBusPoster;

    public LoginUseCaseSync(LoginHttpEndpointSync loginHttpEndpointSync,AuthTokenCache authTokenCache,EventBusPoster eventBusPoster) {
        this.mLoginHttpEndpointSync = loginHttpEndpointSync;
        this.mAuthTokenCache =authTokenCache;
        this.mEventBusPoster =eventBusPoster;
    }
    public UseCaseResult loginSync(String username,String password) {
        LoginHttpEndpointSync.EndPointResult endpointEndPointResult;
        try {
            endpointEndPointResult = mLoginHttpEndpointSync.loginSync(username, password);
        } catch (NetworkErrorException e) {
            return UseCaseResult.NETWORK_ERROR;
        }
        if (isSuccessfulEndPointResult(endpointEndPointResult)) {
            mAuthTokenCache.cacheAuthToken(endpointEndPointResult.getmAuthToken());
            return UseCaseResult.SUCCESS;
        } else {
            return UseCaseResult.FAILURE;
        }
    }
    private boolean isSuccessfulEndPointResult(LoginHttpEndpointSync.EndPointResult endpointEndPointResult) {
        return false;
    }
}
