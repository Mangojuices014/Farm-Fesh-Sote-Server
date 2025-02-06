package com.kira.farm_fresh_store.service.report;

import com.kira.farm_fresh_store.dto.UserDto;
import com.kira.farm_fresh_store.entity.user.User;
import com.kira.farm_fresh_store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService{

    private final UserRepository userRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public byte[] generatePdfListUser(List<User> users) throws JRException, FileNotFoundException {
        try {
            List<UserDto> listUser = users.stream()
                    .map(user -> new UserDto(
                            user.getId(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getEmail(),
                            user.getUsername()
                    ))
                    .collect(Collectors.toList());

            InputStream reportStream = getClass().getClassLoader().getResourceAsStream("reports/JasperReportFarmStore.jrxml");
            if (reportStream == null) {
                throw new FileNotFoundException("Jasper report file not found in classpath");
            }
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listUser);
            Map<String, Object> parameters = new HashMap<>();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            return outputStream.toByteArray();
        }catch (JRException e) {
            System.out.println("JRException: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public byte[] generateExcelListUser(List<User> users) throws JRException, FileNotFoundException {
        try {
            List<UserDto> listUser = users.stream()
                    .map(user -> new UserDto(
                            user.getId(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getEmail(),
                            user.getUsername()
                    ))
                    .collect(Collectors.toList());

            InputStream reportStream = getClass().getClassLoader().getResourceAsStream("reports/JasperReportFarmStore.jrxml");
            if (reportStream == null) {
                throw new FileNotFoundException("Jasper report file not found in classpath");
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listUser);
            Map<String, Object> parameters = new HashMap<>();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // Xuất báo cáo sang Excel
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

            // Cấu hình xuất Excel (tùy chọn)
            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configuration.setOnePagePerSheet(false); // Cho phép nhiều trang trên một sheet
            configuration.setRemoveEmptySpaceBetweenRows(true); // Loại bỏ khoảng trắng thừa
            exporter.setConfiguration(configuration);

            exporter.exportReport();

            return outputStream.toByteArray();
        } catch (JRException e) {
            System.out.println("JRException: " + e.getMessage());
            throw e;
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }
}
