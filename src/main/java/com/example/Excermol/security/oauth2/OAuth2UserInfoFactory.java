package com.example.Excermol.security.oauth2;

import java.util.Map;
//  OAuth2UserInfoFactory-nin əsas işi:
//  provider-in adına əsasən düzgün OAuth2UserInfo implementasiyasını seçib yaratmaqdır.
public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase("google")) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase("facebook")) {
            return new FacebookOAuth2UserInfo(attributes);
        } else {
            throw new IllegalArgumentException("Dəstəklənməyən OAuth2 provider: " + registrationId);
        }
    }
}
