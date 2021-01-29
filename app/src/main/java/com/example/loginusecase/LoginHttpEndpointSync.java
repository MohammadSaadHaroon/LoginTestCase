package com.example.loginusecase;

import android.accounts.NetworkErrorException;

public interface LoginHttpEndpointSync {
 EndPointResult loginSync(String username, String password) throws NetworkErrorException;
 enum EndpointResultStatus{
     SUCCESS,
     AUTH_ERROR,
     SERVER_ERROR,
     GENERAL_ERROR
 }
 class EndPointResult{
     private final EndpointResultStatus mStatus;
     private final String mAuthToken;

     public EndPointResult(EndpointResultStatus status, String authToken) {
         mStatus = status;
         mAuthToken = authToken;
     }
     public EndpointResultStatus getmStatus(){
         return mStatus;
     }

     public String getmAuthToken() {
         return mAuthToken;
     }
 }

}
