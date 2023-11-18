package com.mmbc.demo.service;

import com.mmbc.demo.exception.BadRequestException;
import com.mmbc.demo.store.FilesStoreRepository;
import com.mmbc.demo.store.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileServiceService implements FileServiceRepository {
    static final Logger log =
            LoggerFactory.getLogger(FileServiceService.class);
    private final Path path = Paths.get(System.getProperty("user.dir") + "/fileStorage");
    final FilesStoreRepository filesStoreRepository;

    public FileServiceService(FilesStoreRepository filesStoreRepository) {
        this.filesStoreRepository = filesStoreRepository;
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(path);
            log.info("Инициализация каталога " + path);
        } catch (IOException e) {
            log.error("Невозможно инициализировать каталог " + path);
            throw new RuntimeException("Cold not initialize folder for upload");
        }
    }

    @Override
    public Movie save(MultipartFile multipartFile) {
        if (!Files.exists(this.path)) {
            this.init();
        }
        try {
            // получаем расширение файла
            String extension = "";
            String oldNameFile = URLEncoder.encode(Objects.requireNonNull(multipartFile.getOriginalFilename()), "UTF-8");
            int index = Objects.requireNonNull(oldNameFile).lastIndexOf('.');
            if (index > 0) {
                extension = oldNameFile.substring(index + 1);
            }
            // проверка на MP4
            if (!extension.equals("mp4")) {
                System.out.println("не то расширение");
                throw new BadRequestException("Files with the mp.4 extension are required");
            }
            Movie storeFile = new Movie(oldNameFile);
            filesStoreRepository.save(storeFile);

            String newFileName = storeFile.getId() + "." + extension;
            Path newPath = Path.of(String.valueOf(this.path), newFileName);
            Files.copy(multipartFile.getInputStream(), newPath);

            log.info("Сохраняем файл" + multipartFile.getOriginalFilename());
            return storeFile;
        } catch (IOException e) {
            log.error("Невозможно сохранить файл" + multipartFile.getOriginalFilename());
            throw new RuntimeException("Could not store this file. Error" + e.getMessage());
        }
    }


    @Override
    public Resource load(String fileName) {
        Path file = path.resolve(fileName);
        try {
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                log.info("Читаем файл из хранилища");
                return resource;
            } else {
                throw new RuntimeException("Could not read this file");
            }
        } catch (MalformedURLException e) {
            log.error("Ошибка чтения файла");
            throw new RuntimeException("Error:" + e.getMessage());
        }
    }

    @Override
    public Stream<Path> load() {
        try {
            return Files.walk(this.path, 1)
                    .filter(path -> !path.equals(this.path))
                    .map(this.path::relativize);
        } catch (IOException e) {
            log.error("Невозможно загрузить файл");
            throw new RuntimeException("Could not load the files");
        }
    }

    @Override
    public void clear() {
        FileSystemUtils.deleteRecursively(path.toFile());
    }

    @Override
    public boolean delete(String id) {
        UUID idForBD = UUID.fromString(id);
        filesStoreRepository.deleteById(idForBD);
        File fileStorage = new File("fileStorage");
        File[] files = fileStorage.listFiles();
        boolean result = false;
        if (files != null) {
            for (File file : files) {
                if (file.getName().startsWith(id)) {
                    result = file.delete();
                    break;
                }
            }
        }

        return result;
    }


}
