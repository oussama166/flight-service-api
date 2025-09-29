package org.jetblue.jetblue.Mapper.RefundUserRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RefundUserRequestReq(
        @NotBlank(message = "Payment ID cannot be blank")
        String paymentId,

        @NotNull(message = "Reason title cannot be null")
        @Pattern(
                regexp = "Flight Cancelled|Weather Conditions|Maintenance Issues|Air Traffic Control|Crew Availability|Security Concerns|Death of a Family Member|Medical Emergency|Other",
                message = "Reason type must be one of: Flight Cancelled, Weather Conditions, Maintenance Issues, Air Traffic Control, Crew Availability, Security Concerns, Death of a Family Member, Medical Emergency, Other"
        )
        String reasonTitle,

        @NotBlank(message = "Description cannot be blank")
        String description
) {
}
