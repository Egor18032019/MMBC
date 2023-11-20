package com.mmbc.demo.store;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class StatusStore {
    String status;
    Long frame;
    double processingSuccess;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatusStore that = (StatusStore) o;
        return Double.compare(that.processingSuccess, processingSuccess) == 0 && Objects.equals(status, that.status) && Objects.equals(frame, that.frame);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, frame, processingSuccess);
    }
}
