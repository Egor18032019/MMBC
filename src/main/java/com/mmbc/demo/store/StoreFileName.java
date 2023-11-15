package com.mmbc.demo.store;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "files")
@Getter
@Setter
public class StoreFileName {
    @Id
    @GeneratedValue
    private UUID id;
    @Column()
    String oldName;


    public StoreFileName(String oldName) {
        this.oldName = oldName;

    }

    public StoreFileName() {

    }
}
