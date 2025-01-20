package com.kira.farm_fresh_store.service.photo;

import com.kira.farm_fresh_store.entity.user.User;
import com.kira.farm_fresh_store.repository.PhotoRepository;
import com.kira.farm_fresh_store.repository.UserRepository;
import com.kira.farm_fresh_store.service.googleDrive.GoogleDriveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhotoService implements IPhotoService{


    private final PhotoRepository photoRepository;

    private final GoogleDriveService googleDriveService;

}
