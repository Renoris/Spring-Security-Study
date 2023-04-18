package com.example.demo.provider.userinfo;

import java.util.Map;

public class GoogleUserinfo implements OAuth2UserInfo{

    private Map<String, Object> attributes; //getAttributes()를 받을것

    public GoogleUserinfo( Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
