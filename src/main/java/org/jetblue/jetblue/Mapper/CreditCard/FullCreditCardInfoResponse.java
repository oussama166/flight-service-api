package org.jetblue.jetblue.Mapper.CreditCard;

import lombok.Builder;

@Builder
public record FullCreditCardInfoResponse(
        String userName,
        String cardHolderName,
        String cardNumber,
        String cardType,
        String expirationDate,
        String billingAddress,
        String cvv
) {
}
