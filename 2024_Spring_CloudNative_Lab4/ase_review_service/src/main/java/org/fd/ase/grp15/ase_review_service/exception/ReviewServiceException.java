package org.fd.ase.grp15.ase_review_service.exception;

import lombok.Getter;
import lombok.NonNull;
import org.fd.ase.grp15.ase_review_service.exception.code.ReviewServiceErrorCode;

@Getter
public class ReviewServiceException extends RuntimeException{
    private final ReviewServiceErrorCode code;

    public ReviewServiceException(@NonNull ReviewServiceErrorCode code, @NonNull String message) {
        super(message);
        this.code = code;
    }
}
