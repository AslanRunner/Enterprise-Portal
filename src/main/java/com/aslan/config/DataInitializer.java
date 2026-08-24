package com.aslan.config;

import com.aslan.entity.Personel;
import com.aslan.repository.PersonelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private PersonelRepository personelRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        List<Personel> personelsWithoutPassword = personelRepository.findAll().stream()
                .filter(p -> p.getPassword() == null || p.getPassword().isEmpty())
                .toList();

        if (!personelsWithoutPassword.isEmpty()) {
            String defaultPassword = passwordEncoder.encode("123456");
            for (Personel p : personelsWithoutPassword) {
                p.setPassword(defaultPassword);
                personelRepository.save(p);
            }
            System.out.println(personelsWithoutPassword.size() + " personel records updated with default password '123456'.");
        }
    }
}
