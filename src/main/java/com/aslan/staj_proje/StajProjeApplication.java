package com.aslan.staj_proje;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import jakarta.annotation.PostConstruct;

@EnableJpaRepositories(basePackages = { "com.aslan" })
@ComponentScan(basePackages = { "com.aslan" })
@EntityScan(basePackages = { "com.aslan" })
@SpringBootApplication
public class StajProjeApplication {

    @Autowired
    private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(StajProjeApplication.class, args);
	}

    @PostConstruct // Alter profile_photo column to TEXT to bypass character limit
    public void init() {
        try {
            jdbcTemplate.execute("ALTER TABLE jforce_db.personel ALTER COLUMN profile_photo TYPE TEXT;");
            System.out.println("Successfully altered profile_photo to TEXT");
        } catch (Exception e) {
            System.out.println("Alter table failed or already applied: " + e.getMessage());
        }
    }

}
