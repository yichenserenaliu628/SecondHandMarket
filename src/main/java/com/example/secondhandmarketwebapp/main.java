package com.example.secondhandmarketwebapp;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import java.io.File;

public class main {
    public static void main(String[] args) {
        // Define your AWS credentials and S3 endpoint URL
        String accessKeyId = "AKIAVEQKSQRMGOXVWP5M";
        String secretKey = "rAd8uKKgE5MZaxwFRvVFi4YSg9xqPcBisNb12smg";
        String endpointUrl = "https://s3bucket-xuehang.s3-website-us-east-1.amazonaws.com";
        String regionName = "us-east-1";
        String bucketName = "s3bucket-xuehang";

        // Create a client using the AWS SDK and your credentials
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretKey);
        System.out.println(1);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpointUrl, regionName))
                .build();

        try {
            // Define the file you want to upload
            File file = new File("C:\\Users\\57054\\OneDrive\\桌面\\download.jpg");
            System.out.println(2);
            // Upload the file to your S3 bucket
            PutObjectRequest request = new PutObjectRequest(bucketName, file.getName(), file);
            System.out.println(request.getFile().getName());
            PutObjectResult result = s3Client.putObject(request);
            System.out.println("File uploaded successfully: " + result.getETag());
        } catch (AmazonServiceException e) {
            System.err.println("Error uploading file to S3: " + e.getErrorMessage());
        } catch (SdkClientException e) {
            System.err.println("Error connecting to S3: " + e.getMessage());
        }
    }
}
