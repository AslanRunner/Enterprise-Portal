package com.aslan.repository;

import com.aslan.entity.RoomReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomReservationRepository extends JpaRepository<RoomReservation, Long> {
    List<RoomReservation> findByPersonelId(Long personelId);
    List<RoomReservation> findByMeetingRoomId(Long roomId);

    @Query("SELECT COUNT(r) > 0 FROM RoomReservation r " +
            "WHERE r.meetingRoom.id = :roomId " +
            "AND r.startTime < :newEndTime " +
            "AND r.endTime > :newStartTime")
    boolean hasOverlappingReservation(@Param("roomId") Long roomId,
                                      @Param("newStartTime")LocalDateTime newStartTime,
                                      @Param("newEndTime")LocalDateTime newEndTime);

}
