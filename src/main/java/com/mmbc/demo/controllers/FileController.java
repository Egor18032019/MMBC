package com.mmbc.demo.controllers;

import com.mmbc.demo.schemas.FileChangeRequest;
import com.mmbc.demo.schemas.FileChangeResponse;
import com.mmbc.demo.schemas.FileCreatResponse;
import com.mmbc.demo.schemas.FileStatusResponse;
import com.mmbc.demo.service.FileChangeService;
import com.mmbc.demo.service.FileServiceService;
import com.mmbc.demo.store.Movie;
import com.mmbc.demo.utils.EndPoint;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

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
            Movie movie = fileStorageService.save(file);
            return new ResponseEntity<FileCreatResponse>(new FileCreatResponse(movie.getId()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PatchMapping(value = {"{id}"})
    @CrossOrigin(allowCredentials = "true", originPatterns = "*")
    public ResponseEntity<FileChangeResponse> handleFileChange(@PathVariable String id, @RequestBody() FileChangeRequest request) throws IOException {

        System.out.println("handleFileChange path " + id);
        int width = request.getWidth();
        int height = request.getHeight();
        if (width > 20 || height > 20 || width % 2 == 0 || height % 2 == 0) {

            fileChangeService.changeResolution(id, width, height);
            return new ResponseEntity<FileChangeResponse>(new FileChangeResponse(true), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


    }

    @GetMapping(value = {"{id}"})
    @CrossOrigin(allowCredentials = "true", originPatterns = "*")
    public FileStatusResponse handleFileStatus(@PathVariable String id) {

        try {
            Movie movie = fileChangeService.getStatus(id);
            String status = movie.getStatus();
            Boolean sts = Objects.equals(status, "continue");

            return new FileStatusResponse(id, movie.getOldName(), sts, movie.getProcessingSuccess());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @DeleteMapping(value = {"{id}"})
    @CrossOrigin(allowCredentials = "true", originPatterns = "*")
    public FileChangeResponse handleFileDelete(@PathVariable String id) {

        boolean result = fileStorageService.delete(id);

        return new FileChangeResponse(result);

    }
}
