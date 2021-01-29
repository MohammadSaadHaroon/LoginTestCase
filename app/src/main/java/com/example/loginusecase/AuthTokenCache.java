package com.example.loginusecase;

public interface AuthTokenCache {
    void cacheAuthToken(String AuthToken);
    String getAuthToken();
}
