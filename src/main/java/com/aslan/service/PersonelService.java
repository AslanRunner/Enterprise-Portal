package com.aslan.service;

import com.aslan.dto.DtoPersonelRequest;
import com.aslan.dto.DtoPersonelResponse;
import java.util.List;

public interface PersonelService {

    DtoPersonelResponse createPersonel(DtoPersonelRequest requestDto);

    List<DtoPersonelResponse> getAllPersonel();

    DtoPersonelResponse getPersonelById(Long id);

    DtoPersonelResponse updatePersonel(Long id, DtoPersonelRequest personelDetails);

    List<DtoPersonelResponse> getActivePersonel();

    void deletePersonel(Long id);

    void changePassword(Long id, String oldPassword, String newPassword);
}
