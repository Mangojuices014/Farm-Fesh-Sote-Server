package com.kira.farm_fresh_store.controller;

import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.response.ApiResponse;
import com.kira.farm_fresh_store.service.process.IProcessService;
import com.kira.farm_fresh_store.service.process.ProcessService;
import com.kira.farm_fresh_store.utils.FeedBackMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/process")
@RequiredArgsConstructor
public class ProcessController {

    private final IProcessService processService;

    private final String CAMUNDA_API_URL = "http://localhost:8182/engine-rest/task?taskDefinitionKey=Complete_Order";

    @GetMapping("/task")
    public ResponseEntity<Object> getTasksFromCamunda() {
        RestTemplate restTemplate = new RestTemplate();

        // Gửi request đến Camunda API
        ResponseEntity<Object> response = restTemplate.exchange(
                CAMUNDA_API_URL,
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                Object.class
        );

        return ResponseEntity.ok(response.getBody());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/order/approval/{taskId}")
    public ResponseEntity<ApiResponse<String>> approvalProcess(@PathVariable String taskId) {
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/order/reject/{taskId}")
    public ResponseEntity<ApiResponse<String>> rejectProcess(@PathVariable String taskId) {
        try {
            // Trả về ApiResponse với thông báo thành công
            return ResponseEntity.status(OK)
                    .body(new ApiResponse<>(FeedBackMessage.SUCCESS, processService.rejectOrder(taskId)));
        }catch (ResourceNotFoundException e){
            // Trả về ApiResponse với thông báo thành công
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }
}
