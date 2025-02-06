package com.kira.farm_fresh_store.controller;

import com.kira.farm_fresh_store.entity.user.User;
import com.kira.farm_fresh_store.repository.UserRepository;
import com.kira.farm_fresh_store.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/jasper")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    private final UserRepository userRepository;

    @GetMapping("/generate-pdf")
    public ResponseEntity<byte[]> generatePdf() {
        try {
            List<User> users = userRepository.findAll();
            byte[] pdfContent = reportService.generatePdfListUser(users);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=report.pdf");
            headers.setContentType(MediaType.APPLICATION_PDF);
            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new byte[0]);
        } catch (JRException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new byte[0]);
        }
    }

    @GetMapping("/generate-excel")
    public ResponseEntity<byte[]> generateExcel() {
        try {
            List<User> users = userRepository.findAll();
            byte[] excelContent = reportService.generateExcelListUser(users);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=report.xlsx");
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            return new ResponseEntity<>(excelContent, headers, HttpStatus.OK);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new byte[0]);
        } catch (JRException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new byte[0]);
        }
    }
}
