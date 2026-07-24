package com.example.Excermol.security.oauth2;

import java.util.Map;

//FacebookOAuth2UserInfo, OAuth2UserInfo-dan miras alır
// və onun abstract metodlarını Facebook-auyğun şəkildə implement edir
public class FacebookOAuth2UserInfo extends OAuth2UserInfo {

    public FacebookOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
}
