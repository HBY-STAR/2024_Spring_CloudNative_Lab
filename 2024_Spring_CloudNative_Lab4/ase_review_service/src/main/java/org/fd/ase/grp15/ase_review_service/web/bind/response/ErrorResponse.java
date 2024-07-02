package org.fd.ase.grp15.ase_review_service.web.bind.response;


import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.fd.ase.grp15.ase_review_service.exception.code.ReviewServiceErrorCode;

@Getter
@Setter
public class ErrorResponse extends BaseResponse {

    private final ReviewServiceErrorCode code;

    public ErrorResponse(@NonNull String message, ReviewServiceErrorCode code) {
        super(false, message);
        this.code = code;
    }
}
