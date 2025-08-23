package org.jetblue.jetblue.Mapper.CreditCard;

import jakarta.validation.constraints.*;

import java.math.BigInteger;

public record CreditCardRequest(
        @NotBlank(message = "Card number is required")
        @Size(min = 16, max = 16, message = "Card number must be 16 digits")
        @Pattern(regexp = "\\d{16}", message = "Card number must contain only digits")
        String cardNumber,
        @NotNull(message = "Card holder name cannot be null")
        String cardHolderName,
        @NotNull(message = "Expiration date cannot be null")
        @Pattern(regexp = "^(0[1-9]|1[0-2])/(\\d{2}|\\d{4})$", message = "Expiration date must be in the format MM/YY or MM/YYYY")
        String expirationDate,
        @Min(value = 3, message = "CVV need to be 3 digits")
        @Max(value = 3, message = "CVV need to be 3 digits")
        String cvv,
        @NotNull
        String billingAddress,
        @NotNull
        @Pattern(regexp = "VISA|MASTERCARD|AMEX|DISCOVER", message = "Card type must be VISA, MASTERCARD, AMEX, or DISCOVER")
        String cardType

) {
}
