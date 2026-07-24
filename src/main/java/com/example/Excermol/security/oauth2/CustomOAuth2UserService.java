package com.example.Excermol.security.oauth2;

import com.example.Excermol.entity.User;
import com.example.Excermol.enums.AuthProvider;
import com.example.Excermol.enums.UserRole;
import com.example.Excermol.enums.UserStatus;
import com.example.Excermol.repository.UserRepository;
import com.example.Excermol.security.userdetails.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
//OAuth2 login prosesinin əsas biznes məntiqini idarə edir:
//Google/Facebook-dan user məlumatını alır → email-i tapır →database-də user-i axtarır → varsa yeniləyir → yoxdursa yaradır → Spring Security-yə authenticated user qaytarır.
//DefaultOAuth2UserService-Onun əsas işi provider-dən user məlumatlarını almaqdır.
public class CustomOAuth2UserService  extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomOAuth2UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (Exception ex) {
            log.error("OAuth2 istifadəçi emalında xəta", ex);
            throw new OAuth2AuthenticationException("OAuth2 girişində xəta baş verdi");
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // "google" və ya "facebook"

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());

        if (userInfo.getEmail() == null || userInfo.getEmail().isBlank()) {
            throw new OAuth2AuthenticationException("Email tapılmadı, OAuth2 provider email icazəsi vermədi");
        }

        User user = userRepository.findByEmail(userInfo.getEmail())
                .map(existingUser -> updateExistingUser(existingUser, registrationId))
                .orElseGet(() -> registerNewUser(userInfo, registrationId));

        return new UserPrincipal(user,oAuth2User.getAttributes());
    }

    // Mövcud user tapılıbsa (auto-merge) - authProvider-i yenilə
//    email artıq DB-də varsa (LOCAL və ya əvvəlki OAuth2), sadəcə authProvider-i yeniləyir, giriş imkanını qorumaq üçün heç nəyi bloklamır — bu, sənin "auto-merge" seçiminin tətbiqidir
    private User updateExistingUser(User existingUser, String registrationId) {
        existingUser.setAuthProvider(AuthProvider.valueOf(registrationId.toUpperCase()));
        return userRepository.save(existingUser);
    }

    // Yeni user yarat (OAuth2 ilə ilk dəfə giriş)
//    email tapılmayıbsa, yeni user yaradır, dummy parol (UUID.randomUUID() + BCrypt) təyin edir ki, DB-nin nullable=false constraint-i pozulmasın, amma bu parol heç vaxt real istifadə olunmayacaq
    private User registerNewUser(OAuth2UserInfo userInfo, String registrationId) {
        User user = new User();
        user.setFullName(userInfo.getName());
        user.setEmail(userInfo.getEmail());
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString())); // dummy parol, heç vaxt istifadə olunmayacaq
        user.setRole(UserRole.USER);
        user.setStatus(UserStatus.ACTIVE);
        user.setAuthProvider(AuthProvider.valueOf(registrationId.toUpperCase()));

        User savedUser = userRepository.save(user);
        log.info("Yeni OAuth2 istifadəçi yaradıldı: {} ({})", savedUser.getEmail(), registrationId);
        return savedUser;
    }
}


//Google Login
//     ↓
//Google user məlumatları
//     ↓
//CustomOAuth2UserService
//     ↓
//Email ilə DB-də user axtarılır
//     ↓
// ┌───────────────┐
// │ Var           │ → mövcud user update olunur
// │ Yoxdur        │ → yeni user yaradılır
// └───────────────┘
//         ↓
//UserPrincipal
//     ↓
//Spring Security Authentication
