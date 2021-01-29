package com.example.loginusecase;

import android.accounts.NetworkErrorException;

import org.junit.Before;
import org.junit.Test;

import static com.example.loginusecase.LoginUseCaseSync.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class LoginUseCaseSyncTest {
    public static final String USERNAME ="username";
    public static final String PASSWORD ="password";
    public static final String AUTH_TOKEN ="authToken";
    LoginHttpEndpointSyncTd mLoginHttpEndpointSyncTd;
    AuthTokenCacheTd mAuthTokenCacheTd;
EventBusPosterTd mEventBusPosterTd;
    LoginUseCaseSync SUT;

@Before
    public void setup() throws Exception{
    mLoginHttpEndpointSyncTd =new LoginHttpEndpointSyncTd();
    mAuthTokenCacheTd =new AuthTokenCacheTd();
    mEventBusPosterTd =new EventBusPosterTd();
    SUT =new LoginUseCaseSync(mLoginHttpEndpointSyncTd,mAuthTokenCacheTd,mEventBusPosterTd);
}
@Test
public void loginsyncTest_success() throws Exception{
    SUT.loginSync(USERNAME,PASSWORD);
    assertThat(mLoginHttpEndpointSyncTd.mUsername, is(USERNAME));
    assertThat(mLoginHttpEndpointSyncTd.mPassword, is(PASSWORD));
}
    @Test
    public void loginsyncTest_success1() throws Exception{
        SUT.loginSync(USERNAME,PASSWORD);
        assertThat(mAuthTokenCacheTd.getAuthToken(), is(AUTH_TOKEN));
        assertThat(mLoginHttpEndpointSyncTd.mPassword, is(PASSWORD));
    }
    @Test
    public void loginsyncTest_genError() throws Exception{
       mLoginHttpEndpointSyncTd.mIsGenerateError =true;
       SUT.loginSync(USERNAME,PASSWORD);
       assertThat(mAuthTokenCacheTd.getAuthToken(), is(""));

    }
    @Test
    public void loginsyncTest_authError() throws Exception{
        mLoginHttpEndpointSyncTd.mauthError =true;
        SUT.loginSync(USERNAME,PASSWORD);
        assertThat(mAuthTokenCacheTd.getAuthToken(), is(""));

    }
    @Test
    public void loginsyncTest_severError() throws Exception{
        mLoginHttpEndpointSyncTd.mserverError =true;
        SUT.loginSync(USERNAME,PASSWORD);
        assertThat(mAuthTokenCacheTd.getAuthToken(), is(""));

    }

    @Test
    public void loginsyncTest_success_loginEventPosted() throws Exception{

        SUT.loginSync(USERNAME,PASSWORD);
        assertThat(mEventBusPosterTd.mEvent, is(instanceOf(LoggedInEvent.class)));

    }


    @Test
    public void loginsyncTest_genError_NoInteractWithEventBusPoster() throws Exception{
mLoginHttpEndpointSyncTd.mIsGenerateError = true;
        SUT.loginSync(USERNAME,PASSWORD);
        assertThat(mEventBusPosterTd.mInteractionCount, is(0));

    }
    @Test
    public void loginsyncTest_authError_NoInteractWithEventBusPoster() throws Exception{
        mLoginHttpEndpointSyncTd.mauthError = true;
        SUT.loginSync(USERNAME,PASSWORD);
        assertThat(mEventBusPosterTd.mInteractionCount, is(0));

    }
    @Test
    public void loginsyncTest_serverError_NoInteractWithEventBusPoster() throws Exception{
        mLoginHttpEndpointSyncTd.mserverError = true;
        SUT.loginSync(USERNAME,PASSWORD);
        assertThat(mEventBusPosterTd.mInteractionCount, is(0));

    }
    @Test
    public void loginsyncTest_success2() throws Exception{
        UseCaseResult result = SUT.loginSync(USERNAME,PASSWORD);
        assertThat(result, is(UseCaseResult.SUCCESS));

    }
    @Test
    public void loginsyncTest_serverError() throws Exception{
    mLoginHttpEndpointSyncTd.mserverError =true;
        UseCaseResult result = SUT.loginSync(USERNAME,PASSWORD);
        assertThat(result, is(UseCaseResult.FAILURE));

    }
    @Test
    public void loginsyncTest_authError2() throws Exception{
        mLoginHttpEndpointSyncTd.mauthError =true;
        UseCaseResult result = SUT.loginSync(USERNAME,PASSWORD);
        assertThat(result, is(UseCaseResult.FAILURE));

    }
    @Test
    public void loginsyncTest_genError2() throws Exception{
        mLoginHttpEndpointSyncTd.mIsGenerateError =true;
        UseCaseResult result = SUT.loginSync(USERNAME,PASSWORD);
        assertThat(result, is(UseCaseResult.FAILURE));

    }

    @Test
    public void loginsyncTest_netError() throws Exception{
        mLoginHttpEndpointSyncTd.mIsNetworkError =true;
        UseCaseResult result = SUT.loginSync(USERNAME,PASSWORD);
        assertThat(result, is(UseCaseResult.NETWORK_ERROR));

    }
public static class LoginHttpEndpointSyncTd implements LoginHttpEndpointSync{
    public String mUsername;
    public String mPassword;
    public boolean mIsGenerateError;
    public boolean mauthError;
    public boolean mserverError;
    public boolean mIsNetworkError;

    @Override
    public EndPointResult loginSync(String username, String password) throws NetworkErrorException {
        mUsername = username;
        mPassword = password;
        if (mIsGenerateError) {
            return new EndPointResult(EndpointResultStatus.GENERAL_ERROR, "");
        }else if(mauthError) {
            return new EndPointResult(EndpointResultStatus.AUTH_ERROR, "");

        } else if(mserverError) {
            return new EndPointResult(EndpointResultStatus.SERVER_ERROR, "");

        } else if(mIsNetworkError) {
            throw new NetworkErrorException();

        }else {
            return new EndPointResult(EndpointResultStatus.SUCCESS, AUTH_TOKEN);
        }
    }


}
public static class AuthTokenCacheTd implements AuthTokenCache{
    String mAuthToken ="" ;
    @Override
    public void cacheAuthToken(String AuthToken){
        mAuthToken =AuthToken;
    }
    @Override
    public String getAuthToken(){
        return mAuthToken;
    }
}
public static class EventBusPosterTd implements EventBusPoster{
    public Object mEvent;
    public int mInteractionCount;

    @Override
    public void postEvent(Object event){
        mInteractionCount++;
        mEvent =event;
    }
}
}