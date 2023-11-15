package com.mmbc.demo.service;

import com.mmbc.demo.store.StoreFileName;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileServiceRepository {

    void init();

    StoreFileName save(MultipartFile multipartFile);

    Resource load(String fileName);

    Stream<Path> load();

    void clear();
}
