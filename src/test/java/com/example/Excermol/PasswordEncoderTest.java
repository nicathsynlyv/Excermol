package com.example.Excermol;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
    @Test
    void encodePasswords() {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.println("Lera123 -> " + encoder.encode("Lera123"));
    }
}


//bu test classi evvel test ucun yazdigim userlerin passwordunu bcrypt formasina kecirmeye komek edir