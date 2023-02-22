package com.example.secondhandmarketwebapp.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    String store(MultipartFile file) throws Exception;

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

    byte[] getByteArrayFromFile(String url) throws IOException;

    void deleteFromS3(List<String> uuids);

    void deleteFiles();
}
