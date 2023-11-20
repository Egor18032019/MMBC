package com.mmbc.demo.store.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "files")
@Getter
@Setter
public class Movie {
    @Id
    @GeneratedValue
    private UUID id;
    @Column()
    String oldName;
    @Column()
    Boolean processing;
    @Column()
    String processingSuccess;


    public Movie(String oldName, Boolean processing, String processingSuccess) {
        this.oldName = oldName;
        this.processing = processing;
        this.processingSuccess = processingSuccess;
    }

    public Movie(String oldName) {
        this.oldName = oldName;
        processing = false;
        processingSuccess = "null";
    }

    public Movie() {

    }
}
