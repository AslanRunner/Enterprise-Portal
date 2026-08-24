package com.aslan.repository;

import com.aslan.entity.Equipment;
import com.aslan.enums.EquipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    List<Equipment> findByStatus(EquipmentStatus status);
    boolean existsBySerialNumber(String serialNumber);
}
