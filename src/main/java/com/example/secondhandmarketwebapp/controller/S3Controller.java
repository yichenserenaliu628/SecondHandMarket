package com.example.secondhandmarketwebapp.controller;

import com.example.secondhandmarketwebapp.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
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


    @GetMapping(value= "/download")
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam(value= "keyName") final String keyName) {
        final byte[] data = amazonClient.downloadFile(keyName);
        final ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + keyName + "\"")
                .body(resource);
    }

    @DeleteMapping(value= "/delete")
    public ResponseEntity<String> deleteFile(@RequestParam(value= "fileName") final String keyName) {
        amazonClient.deleteFile(keyName);
        final String response = "[" + keyName + "] deleted successfully.";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}