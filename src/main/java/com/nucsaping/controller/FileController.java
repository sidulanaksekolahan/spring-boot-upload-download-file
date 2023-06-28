package com.nucsaping.controller;

import com.nucsaping.message.ResponseFile;
import com.nucsaping.message.ResponseMessage;
import com.nucsaping.model.FileDB;
import com.nucsaping.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/image")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(value = "/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {

        String message = "";
        ResponseMessage responseMessage = null;

        if (file.isEmpty()) {
            message = "No content provided!";
            responseMessage = new ResponseMessage(
                    HttpStatus.NOT_ACCEPTABLE.value(),
                    message,
                    System.currentTimeMillis());

            return new ResponseEntity<>(responseMessage, HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            FileDB result = fileStorageService.store(file);

            message = "Uploaded the file successfully: " + result.getName();
            responseMessage = new ResponseMessage(
                    HttpStatus.ACCEPTED.value(),
                    message,
                    System.currentTimeMillis()
            );

            return new ResponseEntity<>(responseMessage, HttpStatus.ACCEPTED);

        } catch (Exception e) {

            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            responseMessage = new ResponseMessage(
                    HttpStatus.EXPECTATION_FAILED.value(),
                    message,
                    System.currentTimeMillis()
            );

            return new ResponseEntity<>(responseMessage, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping(value = "/downloadUrls")
    public ResponseEntity<ResponseMessage> downloadUrls() {

        List<ResponseFile> fileDownloadUrl = fileStorageService.getAllDownloadUrls();

        ResponseMessage responseMessage = new ResponseMessage(
                HttpStatus.OK.value(),
                fileDownloadUrl,
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @GetMapping(value = "/displayUrls")
    public ResponseEntity<ResponseMessage> displayUrls() {

        List<ResponseFile> fileDownloadUrl = fileStorageService.getAllDisplayUrls();

        ResponseMessage responseMessage = new ResponseMessage(
                HttpStatus.OK.value(),
                fileDownloadUrl,
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @GetMapping(value = "/filesDownload/{id}")
    public ResponseEntity<byte[]> fileDownloadById(@PathVariable String id) {

        ResponseMessage responseMessage = null;

        FileDB fileDB = fileStorageService.getFile(id);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"");

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(fileDB.getData());
    }

    @GetMapping(
            value = "/display/{id}",
            produces = {
                    MediaType.IMAGE_PNG_VALUE,
                    MediaType.IMAGE_JPEG_VALUE
            })
    public ResponseEntity<byte[]> displayImage(@PathVariable String id) {

        ResponseMessage responseMessage = null;

        FileDB fileDB = fileStorageService.getFile(id);

        return new ResponseEntity<>(fileDB.getData(), HttpStatus.FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> deleteImageById(@PathVariable String id) {

        FileDB result = fileStorageService.deleteImageById(id);
        String message = "Deleted image " +  result.getName() + " successfuly";

        ResponseMessage responseMessage = new ResponseMessage(
                HttpStatus.OK.value(),
                message,
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    // Pageable firstPageWithTwoElements = PageRequest.of(0, 2);
    // Pageable sortedByName =
    //  PageRequest.of(0, 3, Sort.by("name"));

    @GetMapping("/paging")
    public ResponseEntity<ResponseMessage> pagination(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "2") Integer size) {

        Page<FileDB> fileDBPage = fileStorageService.getPaging(page, size);

        ResponseMessage responseMessage = new ResponseMessage(
                HttpStatus.OK.value(),
                fileDBPage,
                System.currentTimeMillis());

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

}























