package com.aslan.config;

import com.aslan.entity.Personel;
import com.aslan.repository.PersonelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class AppConfig {

    @Autowired
    private PersonelRepository personelRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            Personel personel = personelRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Personel bulunamadı: " + email));

            return new User(
                    personel.getEmail(),
                    personel.getPassword(),
                    personel.isActive(),
                    true,
                    true,
                    true,
                    getAuthorities(personel));
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private List<SimpleGrantedAuthority> getAuthorities(Personel personel) {
        if (personel.getRole() == null || personel.getRole().getName() == null) {
            return List.of();
        }

        String roleName = personel.getRole().getName().trim().toUpperCase();
        if (!roleName.startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName;
        }

        return List.of(new SimpleGrantedAuthority(roleName));
    }
}
