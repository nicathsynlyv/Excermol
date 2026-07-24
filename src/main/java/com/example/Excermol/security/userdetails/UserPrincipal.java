package com.example.Excermol.security.userdetails;

import com.example.Excermol.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

///İndi Spring Security-nin öz User-ini tanıması üçün adapter (bağlayıcı) class yaradırıq.Mövcud User entity-ni heç dəyişmirik
//bunun əvəzinə Spring Security-nin tələb etdiyi UserDetails interfeysini implementasiya edən ayrı bir class yazırıq.
//Buna Adapter Pattern de deye bilerik
//
// OAuth2User əlavə edildi - bu, eyni class-ın həm adi login (UserDetails),
// həm də OAuth2 login (OAuth2User) üçün istifadə oluna bilməsinə imkan verir.
// Beləliklə, JwtUtil, JwtAuthenticationFilter kimi mövcud kodlar dəyişmədən
// OAuth2 istifadəçiləri ilə də işləyə bilir.
public class UserPrincipal implements UserDetails, OAuth2User {

    private final User user;
    private Map<String, Object> attributes;

    public UserPrincipal(User user) {
        this.user = user;
    }

    public UserPrincipal(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    //    Bu metod istifadəçinin rolunu Spring Security-yə verir.
    //    SimpleGrantedAuthority spring securitye deyir ki bu istifadecinin rolu budur
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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
        return user.isActive();
    }

    // === OAuth2User interfeysinin tələb etdiyi metodlar ===

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return user.getEmail();
    }
}