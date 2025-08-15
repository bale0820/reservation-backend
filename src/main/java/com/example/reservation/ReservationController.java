package com.example.reservation;


import com.example.reservation.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationMapper reservationMapper;

    public ReservationController(ReservationMapper reservationMapper){
        this.reservationMapper = reservationMapper;
    }

    @Autowired
    private JwtUtil jwtUtil;

//    @GetMapping
//    public List<Reservation> getAllReservations() {
//        return reservationMapper.getAll();
//    }


    @GetMapping("/my")
    public List<Reservation> getMyReservation(@RequestHeader("Authorization") String authHeader) {
        System.out.println("✅ 내 예약 조회 API 호출됨");
        String token = authHeader.replace("Bearer ", "");
        String userEmail = jwtUtil.extractEmail(token);
        return reservationMapper.findByEmail(userEmail);
    }

//    @PostMapping
//    public  String addReservation(@RequestBody Reservation reservation) {
//        reservationMapper.insert(reservation);
//        return "예약이 완료되었습니다!";
//    }

    @PostMapping
    public ResponseEntity<ApiResponse<Reservation>> addReservation(@RequestBody Reservation reservation,
                                 @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userEmail = jwtUtil.extractEmail(token);
        reservation.setUserEmail(userEmail);

        reservationMapper.insert(reservation);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "예약이 성공적으로 등록되었습니다!", reservation));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReservaition(@PathVariable int id,
                                     @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ","");
        String userEmail = jwtUtil.extractEmail(token);
        System.out.println("삭제 요청: id=" + id + ", user=" + userEmail);
        Reservation reservation = reservationMapper.findById(id);
        System.out.println("DB 조회 결과: " + reservation);
        if(reservation == null) {
            return ResponseEntity.status(404).body(new ApiResponse(false, "해당 예약을 찾을 수 없습니다", null));
        }
        if(!userEmail.equals(reservation.getUserEmail())) {
            return ResponseEntity.status(403)
                    .body(new ApiResponse<>(false, "본인의 예약만 취소할 수 있습니다.", null));
        }
        reservationMapper.deleteByIdAndUserEmail(id, userEmail);
        return ResponseEntity.ok(new ApiResponse<>(true, "예약이 성공적으로 취소되었습니다.", null));
    }





    @PutMapping("/{id}")
    public String updateReservation(@PathVariable int id,
                                    @RequestBody Reservation updatedReservation,
                                    @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        String userEmail = jwtUtil.extractEmail(token);
        Reservation existing = reservationMapper.findById(id);

        if(existing == null) {
            return "해당 예약을 찾을 수 없습니다.";
        }
        if(!userEmail.equals(existing.getUserEmail())) {
            return "본인의 예약만 수정 할 수 있습니다.";
        }


        updatedReservation.setId(id);
        updatedReservation.setUserEmail(userEmail);
        reservationMapper.updateReservation(updatedReservation);

        return "예약이 성공적으로 수정되었습니다.";
    }

}

