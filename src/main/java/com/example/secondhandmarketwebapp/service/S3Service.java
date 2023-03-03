package com.example.secondhandmarketwebapp.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.example.secondhandmarketwebapp.exception.ImageFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import com.amazonaws.util.IOUtils;

@Service
public class S3Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(S3Service.class);

    private AmazonS3 s3client;

    //private String endpointUrl = "https://s3.us-east-1.amazonaws.com";
    private String endpointUrl = "https://s3.us-west-1.amazonaws.com";

    //private String bucketName = "s3bucket-xuehang";
    private String bucketName = "serenaliuawsbucket";

    //private String accessKey = "AKIAVEQKSQRMHK7YYM5G";
    private String accessKey = "AKIARHORSLJOXTQCKYRM";

    //private String secretKey = "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";
    private String secretKey = "9f7wlLUL04gIyR4tBsRoy6SmkUq22Wx7l4inz3zT";

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = new AmazonS3Client(credentials);
    }

    public String uploadFile(MultipartFile multipartFile) throws ImageFormatException{

        String fileName = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            fileName = generateFileName(multipartFile);
            String[] suffix = fileName.split("\\.");
            if (suffix[suffix.length-1].equals("jpg") || suffix[suffix.length-1].equals("jpeg")  || suffix[suffix.length-1].equals("png") ){
                //String fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
                uploadFileTos3bucket(fileName, file);
                file.delete();
            }else {
                throw new ImageFormatException("Image Format Invalid");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    @Async
    public void deleteFile(final String keyName) {
        LOGGER.info("Deleting file with name= " + keyName);
        final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, keyName);
        s3client.deleteObject(deleteObjectRequest);
        LOGGER.info("File deleted successfully.");
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }


    @Async
    public byte[] downloadFile(final String keyName) {
        byte[] content = null;
        LOGGER.info("Downloading an object with key= " + keyName);
        final S3Object s3Object = s3client.getObject(bucketName, keyName);
        final S3ObjectInputStream stream = s3Object.getObjectContent();
        try {
            content = IOUtils.toByteArray(stream);
            LOGGER.info("File downloaded successfully.");
            s3Object.close();
        } catch(final IOException ex) {
            LOGGER.info("IO Error Message= " + ex.getMessage());
        }
        return content;
    }

}