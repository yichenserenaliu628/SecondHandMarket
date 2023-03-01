package com.example.secondhandmarketwebapp.controller;

import com.example.secondhandmarketwebapp.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/storage/")
public class S3Controller {

    private S3Service amazonClient;

    @Autowired
    S3Controller(S3Service amazonClient) {
        this.amazonClient = amazonClient;
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file) {
        return this.amazonClient.uploadFile(file);
    }

/*    @GetMapping("/getFile")
    public ResponseEntity<Resource> getFile(@RequestParam(value = "fileUrl") String fileUrl) {
        // Download the file from Amazon S3
        Resource file = amazonClient.getFileFromS3Bucket(fileUrl);

        // Set the headers for the HTTP response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(file.getFilename()).build());

        // Return the file as an HTTP response
        return ResponseEntity.ok().headers(headers).body(file);
    }*/


    @DeleteMapping("/deleteFile")
        public String deleteFile(@RequestPart(value = "url") String fileUrl) {
        return this.amazonClient.deleteFileFromS3Bucket(fileUrl);
    }
}