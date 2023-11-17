package com.mmbc.demo.store;

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
    String status;

    @Column()
    Long frame;
    @Column()
    String processingSuccess;


    public Movie(String oldName, String status, Long frame, String processingSuccess) {
        this.oldName = oldName;
        this.status = status;
        this.frame = frame;
        this.processingSuccess = processingSuccess;
    }

    public Movie(String oldName) {
        this.oldName = oldName;
        status = "creat";
        frame = 0L;
        processingSuccess = "null";
    }

    public Movie() {

    }
}
