package com.fa.training.mockproject.socialsecurity.user;

import com.fa.training.mockproject.socialsecurity.model.AuthProvider;

import java.util.Map;

public class UserInfoFactory {

    public static UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.facebook.toString())) {
            return new FacebookOAuth2UserInfo(attributes);
        } else {
            System.out.println("Not found this provider");
            return null;
        }
    }
}
