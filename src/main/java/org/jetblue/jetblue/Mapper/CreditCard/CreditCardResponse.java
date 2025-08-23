package org.jetblue.jetblue.Mapper.CreditCard;


import lombok.Builder;

@Builder
public record CreditCardResponse(
        String id,
        String cardType,
        String cardHolderName,
        String expirationDate,
        String billingAddress,
        String lastFourDigits,
        String userName
) {
}
