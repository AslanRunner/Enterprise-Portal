package com.aslan.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DtoPersonelRequest {

    @NotBlank(message = "Ad alanı boş bırakılamaz")
    @Size(max = 50, message = "Ad en fazla 50 karakter olabilir")
    private String firstName;

    @NotBlank(message = "Soy ad alanı boş bırakılamaz")
    @Size(max = 50, message = "Soy ad en fazla 50 karakter olabilir")
    private String lastName;

    @NotNull(message = "Doğum tarihi zorunludur")
    private LocalDate birthOfDate;

    @NotBlank(message = "E-posta alanı boş bırakılamaz")
    @Email(message = "Lütfen geçerli bir e-posta adresi giriniz")
    private String email;

    @Size(min = 6, message = "Şifre en az 6 karakter olmalıdır")
    private String password;

    @NotNull(message = "Departman seçimin zorunludur")
    private Long departmentId;

    @NotNull(message = "Rol seçimi zorunludur")
    private Long roleId;

    private List<Long> skillIds;

    private String profilePhoto;
}
