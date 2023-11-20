package com.mmbc.demo.store.entities;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FilesStoreRepository extends JpaRepository<Movie, UUID> {
}
