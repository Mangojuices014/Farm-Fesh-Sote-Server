package com.kira.farm_fresh_store.controller;

import com.kira.farm_fresh_store.service.process.ProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/process")
@RequiredArgsConstructor
public class ProcessController {

    private final ProcessService processService;

    @PostMapping("/order")
    public String startProcess() {
        return processService.processStart();
    }
}
