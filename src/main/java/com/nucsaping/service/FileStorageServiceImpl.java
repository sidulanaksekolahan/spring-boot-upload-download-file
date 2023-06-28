package com.nucsaping.service;

import com.nucsaping.exception.ImageNotFoundException;
import com.nucsaping.message.ResponseFile;
import com.nucsaping.model.FileDB;
import com.nucsaping.repository.FileDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Autowired
    private FileDBRepository fileDBRepository;

    @Override
    public FileDB store(MultipartFile file) throws IOException {

        // Get original file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // Get type
        String type = StringUtils.cleanPath(file.getContentType());

        // Get bytes
        byte[] data = file.getBytes();

        // Construct FileDB
        FileDB fileDB = new FileDB(fileName, type, data);

        // Save the first FileDB
        FileDB fileDBFirstSaved = fileDBRepository.save(fileDB);

        // Create fileDisplayImageUri
        String  fileDisplayImageUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/image/display/")
                .path(fileDBFirstSaved.getId())
                .toUriString();

        // Create fileDownloadImageUri
        String fileDownloadImageUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/image/filesDownload/")
                .path(fileDBFirstSaved.getId())
                .toUriString();

        // Set downloadImageUri
        fileDBFirstSaved.setDisplayImageUri(fileDisplayImageUri);

        // Set downloadImageUri
        fileDBFirstSaved.setDownloadImageUri(fileDownloadImageUri);

        // Save the updated FileDB
        FileDB fileDBFinalSaved = fileDBRepository.save(fileDBFirstSaved);

        // Return the result
        return fileDBFinalSaved;
    }

    @Override
    public FileDB getFile(String id) {

        Optional<FileDB> theFileDb = fileDBRepository.findById(id);

        if (!theFileDb.isPresent()) {
            throw new ImageNotFoundException("No image with specified id");
        }

        return theFileDb.get();
    }

    @Override
    public Stream<FileDB> getAllFiles() {

        return fileDBRepository.findAll().stream();
    }

    @Override
    public FileDB deleteImageById(String id) {

        FileDB fileDB = getFile(id);

        fileDBRepository.deleteById(fileDB.getId());

        return fileDB;
    }

    @Override
    public Page<FileDB> getPaging(Integer page, Integer size) {

        Pageable pageable = null;
        if ((page <= 0) || (size <= 0)) {
            pageable = PageRequest.of(0, 2);
            return fileDBRepository.findAll(pageable);
        }

        pageable = PageRequest.of(page - 1, size);

        return fileDBRepository.findAll(pageable);
    }

    @Override
    public List<ResponseFile> getAllDownloadUrls() {

        return fileDBRepository.findAll()
                .stream()
                .map(fileDB -> {
                    return new ResponseFile(
                            fileDB.getName(),
                            fileDB.getDownloadImageUri(),
                            fileDB.getType(),
                            fileDB.getData().length);
                }).collect(Collectors.toList());
    }

    @Override
    public List<ResponseFile> getAllDisplayUrls() {

        return fileDBRepository.findAll()
                .stream()
                .map(fileDB -> {
                    return new ResponseFile(
                            fileDB.getName(),
                            fileDB.getDisplayImageUri(),
                            fileDB.getType(),
                            fileDB.getData().length);
                }).collect(Collectors.toList());
    }
}
