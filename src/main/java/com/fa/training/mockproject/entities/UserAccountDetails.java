package com.fa.training.mockproject.entities;

import com.fa.training.mockproject.entities.dto.UserAccountsLoginDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class UserAccountDetails implements UserDetails, OAuth2User, OidcUser {

    private int userId;
    private String email;
    private String password;
    private OidcIdToken idToken;
    private OidcUserInfo userInfo;
    private List<GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public UserAccountDetails(String email, String password,
                              List<GrantedAuthority> authorities) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public UserAccountDetails(int userID, String email, String password,
                              List<GrantedAuthority> authorities) {
        this.userId = userID;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    // (1) Create UserAccountDetails with authorities(null) --> CustomOAuth2UserService/ CustomOidcUserService --> add new authorities
    public static UserAccountDetails create(UserAccountsLoginDTO user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        return new UserAccountDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }


    // Call back (1) UserAccountDetails create(UserAccountsLoginDTO user)
    public static UserAccountDetails create(UserAccountsLoginDTO user, Map<String, Object> attributes) {
        UserAccountDetails userAccountDetails = UserAccountDetails.create(user);
        userAccountDetails.setAttributes(attributes);
        return userAccountDetails;
    }


    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Oidc -- GOOGLE

    @Override
    public String getName() {
        return String.valueOf(userId);
    }

    @Override
    public Map<String, Object> getClaims() {
        return attributes;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return userInfo;
    }

    @Override
    public OidcIdToken getIdToken() {
        return idToken;
    }
}
