package com.kira.farm_fresh_store.service.googleDrive;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

@Service
public class GoogleDriveService {

    private static final String APPLICATION_NAME = "FarmFreshStore";
    private static final String FOLDER_ID = "1u5RZsFoS9YrEkRB5k2moeg2n3P7vztm8";

    private Drive getDriveService() throws IOException {
        // Load credentials from JSON file
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                        Objects.requireNonNull(getClass().getResourceAsStream("/credential.json")))
                .createScoped(Collections.singleton(DriveScopes.DRIVE_FILE));
        return new Drive.Builder(new com.google.api.client.http.javanet.NetHttpTransport(),
                com.google.api.client.json.jackson2.JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public String uploadImageToDrive(MultipartFile file) throws IOException {
        Drive driveService = getDriveService();

        // Create a temporary file from MultipartFile
        java.io.File tempFile = java.io.File.createTempFile("temp", null);
        file.transferTo(tempFile);

        // Set metadata for the file
        File fileMetadata = new File();
        fileMetadata.setName(file.getOriginalFilename());
        fileMetadata.setParents(Collections.singletonList(FOLDER_ID));

        // Create FileContent and upload to Drive
        FileContent mediaContent = new FileContent(file.getContentType(), tempFile);
        File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id,webContentLink,webViewLink")
                .execute();

        // Clean up temporary file
        tempFile.delete();

        // Return the URL to access the file
        return uploadedFile.getWebViewLink();
    }
}

