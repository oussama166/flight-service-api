package org.jetblue.jetblue.Service;

import org.jetblue.jetblue.Mapper.CreditCard.CreditCardRequest;
import org.jetblue.jetblue.Mapper.CreditCard.CreditCardResponse;

public interface CreditCardService {
    boolean validateCardNumber(String cardNumber);
    String getCardType(String cardNumber) throws Exception;
    CreditCardResponse addCreditCardToUser(String username, CreditCardRequest creditCardRequest) throws Exception;
}
