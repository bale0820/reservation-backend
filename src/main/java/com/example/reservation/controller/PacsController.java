package com.example.reservation.controller;

import com.example.reservation.dto.PacsUserDto;
import com.example.reservation.service.PacsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pacs")
public class PacsController {

    private final PacsService pacsService;

    @GetMapping
    public List<PacsUserDto> getPacsData() {
        return pacsService.getPacsData();
    }
}
