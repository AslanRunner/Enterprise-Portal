package com.aslan.repository;

import com.aslan.entity.EquipmentAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentAssignmentRepository extends JpaRepository<EquipmentAssignment, Long> {
    List<EquipmentAssignment> findByPersonelId(Long personelId);
    List<EquipmentAssignment> findByEquipmentId(Long equipmentId);
    List<EquipmentAssignment> findByReturnDateIsNull();
    Optional<EquipmentAssignment> findByEquipmentIdAndReturnDateIsNull(Long equipmentId);
}
