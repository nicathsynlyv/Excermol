package com.example.Excermol.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//    BCryptPasswordEncoder istifadəçilərin şifrələrini təhlükəsiz şəkildə hash etmək üçün istifadə olunur.
//    Şifrələr açıq mətn kimi deyil, hash olunmuş formada databasedə saxlanılır.
//    Login zamanı isə daxil edilən şifrə matches() metodu ilə hash olunmuş dəyərlə müqayisə edilir.
//    BCrypt təsadüfi salt istifadə etdiyi üçün eyni şifrə hər dəfə fərqli hash yaradır
//    və bu da rainbow table və brute-force hücumlarına qarşı daha yüksək təhlükəsizlik təmin edir.

@Component
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
