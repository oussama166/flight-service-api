package org.jetblue.jetblue.Service;

import org.jetblue.jetblue.Mapper.CreditCard.CreditCardRequest;
import org.jetblue.jetblue.Mapper.CreditCard.CreditCardResponse;
import org.jetblue.jetblue.Mapper.CreditCard.FullCreditCardInfoResponse;

import java.util.List;

public interface CreditCardService {
    boolean validateCardNumber(String cardNumber);
    String getCardType(String cardNumber) throws Exception;
    CreditCardResponse addCreditCardToUser(String username, CreditCardRequest creditCardRequest) throws Exception;
    List<FullCreditCardInfoResponse> CreditCardByUsername(String username);
    boolean DeleteCreditCardByUsername(String username, String lastFourDigits);
}
