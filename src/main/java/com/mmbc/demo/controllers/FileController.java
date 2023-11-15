package com.mmbc.demo.controllers;

import com.mmbc.demo.schemas.FileChangeRequest;
import com.mmbc.demo.schemas.FileChangeResponse;
import com.mmbc.demo.schemas.FileCreatResponse;
import com.mmbc.demo.schemas.FileStatusResponse;
import com.mmbc.demo.service.FileChangeService;
import com.mmbc.demo.service.FileStorageService;
import com.mmbc.demo.utils.EndPoint;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(EndPoint.file)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileController {

    FileChangeService fileChangeService;
    FileStorageService fileStorageService;

    public FileController(FileChangeService fileChangeService, FileStorageService fileStorageService) {
        this.fileChangeService = fileChangeService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping()
    @CrossOrigin(allowCredentials = "true", originPatterns = "*")
    public FileCreatResponse handleFileUpload(@RequestParam(value = "video", required = true) MultipartFile file) {

        if (file != null && !file.isEmpty()) {

            System.out.println("!= null ");

        } else {
            System.out.println("= null ");


        }

        return new FileCreatResponse();

    }


    @PatchMapping(value = {"id"})
    @CrossOrigin(allowCredentials = "true", originPatterns = "*")
    public FileChangeResponse handleFileChange(@RequestBody() FileChangeRequest fileChangeRequest) {

        System.out.println("fileChangeRequest ");


        return new FileChangeResponse();

    }

    @GetMapping(value = {"{id}"})
    @CrossOrigin(allowCredentials = "true", originPatterns = "*")
    public FileStatusResponse handleFileStatus(@PathVariable String id) {

        System.out.println("handleFileStatus");


        return new FileStatusResponse();

    }

    @DeleteMapping(value = {"{id}"})
    @CrossOrigin(allowCredentials = "true", originPatterns = "*")
    public FileChangeResponse handleFileDelete(@PathVariable String id) {

        System.out.println("handleFileDelete " + id);


        return new FileChangeResponse();

    }
}
