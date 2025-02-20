package com.kira.farm_fresh_store.controller;

import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.response.ApiResponse;
import com.kira.farm_fresh_store.service.process.IProcessService;
import com.kira.farm_fresh_store.service.process.ProcessService;
import com.kira.farm_fresh_store.utils.FeedBackMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/process")
@RequiredArgsConstructor
public class ProcessController {

    private final IProcessService processService;

    @PostMapping("/order/approval/{taskId}")
    public ResponseEntity<ApiResponse<String>> startProcess(@PathVariable String taskId) {
        try {
            // Trả về ApiResponse với thông báo thành công
            return ResponseEntity.status(OK)
                    .body(new ApiResponse<>(FeedBackMessage.SUCCESS, processService.approveOrder(taskId)));
        }catch (ResourceNotFoundException e){
            // Trả về ApiResponse với thông báo thành công
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }
}
