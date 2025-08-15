package com.example.reservation;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper //Mybatis에서 이 인터페이스를 sql 매퍼로 인식하도록 함
public interface ReservationMapper {
//    List<Reservation> getAll();
    List<Reservation> findByEmail(String userEmail);
    void deleteByIdAndUserEmail(@Param("id") int id, @Param("userEmail") String userEmail);
    void insert(Reservation reservation);
    Reservation findById(int id);
    void updateReservation(Reservation reservation);
}


