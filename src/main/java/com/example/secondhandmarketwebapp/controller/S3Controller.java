package com.example.secondhandmarketwebapp.controller;

import com.example.secondhandmarketwebapp.clients.AmazonS3Client;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class S3Controller {


    private AmazonS3Client s3Service;

    public S3Controller(AmazonS3Client s3Service) {
        this.s3Service = s3Service;
    }


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        try {
            String fileName = s3Service.uploadFile(file);
            model.addAttribute("message", "File uploaded successfully");
            model.addAttribute("fileName", fileName);
        } catch (Exception e) {
            model.addAttribute("message", "Failed to upload file: " + e.getMessage());
        }
        return "uploadResult";
    }

}
