package com.mmbc.demo.controllers;

import com.mmbc.demo.schemas.FileChangeResponse;
import com.mmbc.demo.schemas.FileCreatResponse;
import com.mmbc.demo.schemas.FileStatusResponse;
import com.mmbc.demo.service.FileChangeService;
import com.mmbc.demo.service.FileServiceService;
import com.mmbc.demo.store.StoreFileName;
import com.mmbc.demo.utils.EndPoint;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = EndPoint.file)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileController {

    FileChangeService fileChangeService;
    FileServiceService fileStorageService;

    public FileController(FileChangeService fileChangeService, FileServiceService fileStorageService) {
        this.fileChangeService = fileChangeService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping()
    @CrossOrigin(allowCredentials = "true", originPatterns = "*")
    public ResponseEntity<FileCreatResponse> handleFileUpload(@RequestParam(value = "file", required = true) MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            System.out.println(file.getOriginalFilename());

            StoreFileName storeFileName = fileStorageService.save(file);
            return new ResponseEntity<FileCreatResponse>(new FileCreatResponse(storeFileName.getId()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


    }


    @PatchMapping(value = {"{id}"})
    @CrossOrigin(allowCredentials = "true", originPatterns = "*")
    public FileChangeResponse handleFileChange(@PathVariable String id) {

        System.out.println("handleFileChange path " + id);


        return new FileChangeResponse();

    }

    @GetMapping(value = {"{id}"})
    @CrossOrigin(allowCredentials = "true", originPatterns = "*")
    public FileStatusResponse handleFileStatus(@PathVariable String id) {

        System.out.println("handleFileStatus " + id);


        return new FileStatusResponse(id, "file", true, "pro");

    }

    @DeleteMapping(value = {"{id}"})
    @CrossOrigin(allowCredentials = "true", originPatterns = "*")
    public FileChangeResponse handleFileDelete(@PathVariable String id) {

        System.out.println("handleFileDelete " + id);


        return new FileChangeResponse();

    }
}
