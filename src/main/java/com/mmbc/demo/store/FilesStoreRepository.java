package com.mmbc.demo.store;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FilesStoreRepository extends JpaRepository<StoreFileName, Long> {
}
