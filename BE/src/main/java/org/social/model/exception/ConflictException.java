package org.social.model.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
