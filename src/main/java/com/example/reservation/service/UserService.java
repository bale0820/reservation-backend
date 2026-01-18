package com.example.reservation.service;

import com.example.reservation.dto.StaffDto;
import com.example.reservation.entity.Staff;
import com.example.reservation.entity.User;
import com.example.reservation.repository.StaffRepository;
import com.example.reservation.repository.UserRepository;
import com.example.reservation.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final StaffRepository staffRepository;

    public UserService(UserRepository userRepository, StaffRepository staffRepository) {
        this.userRepository = userRepository;
        this.staffRepository = staffRepository;
    }


    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        users.forEach(System.out::println);
       return users.stream().map((u) -> {
            UserDto  ud = new UserDto();
            ud.setId(u.getId());
            ud.setUserId(u.getUserId());
            ud.setName(u.getName());
            ud.setEmail(u.getEmail());
            ud.setPhone(u.getPhone());
            return ud;
        }).collect(Collectors.toList());

    }

    public List<StaffDto> findAllStaff() {
        List<Staff> staffs = staffRepository.findAll();
        staffs.forEach(System.out::println);
        return staffs.stream().map((u) -> {
            StaffDto  ud = new StaffDto();
            ud.setId(u.getId());
            ud.setUserId(u.getLoginId());
            ud.setName(u.getName());
            ud.setEmail(u.getEmail());
            ud.setPhone(u.getPhone());
            ud.setDepartment(u.getDepartment());
            ud.setRole(u.getRole());
            return ud;
        }).collect(Collectors.toList());

    }
}


