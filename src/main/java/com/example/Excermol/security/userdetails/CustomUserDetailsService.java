package com.example.Excermol.security.userdetails;

import com.example.Excermol.entity.User;
import com.example.Excermol.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//Spring Security login zamanı "bu email-li istifadəçi var mı, varsa gətir" deyəndə,
// bu sorğunu sənin UserRepository-nə yönləndirir və nəticəni UserPrincipal-a çevirir.

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("İstifadəçi tapılmadı: " + email));

        return new UserPrincipal(user);
    }
}