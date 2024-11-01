package inu.helloforeigner.common;

import java.time.LocalDateTime;

// Soft delete interface
public interface SoftDeletable {
    LocalDateTime getDeletedAt();
    void delete();
    void restore();
}