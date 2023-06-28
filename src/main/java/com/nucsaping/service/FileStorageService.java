package com.nucsaping.service;

import com.nucsaping.message.ResponseFile;
import com.nucsaping.model.FileDB;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public interface FileStorageService {

    FileDB store(MultipartFile file) throws IOException;

    FileDB getFile(String id);

    Stream<FileDB> getAllFiles();

    FileDB deleteImageById(String id);

    Page<FileDB> getPaging(Integer page, Integer size);

    List<ResponseFile> getAllDownloadUrls();

    List<ResponseFile> getAllDisplayUrls();
}
