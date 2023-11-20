package com.mmbc.demo.store;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatusStoreRepository {
    private static final Map<UUID, StatusStore> STATUS_STORE = new HashMap<>();

    public void add(UUID id, StatusStore statusStore) {
        STATUS_STORE.put(id, statusStore);
    }
}
