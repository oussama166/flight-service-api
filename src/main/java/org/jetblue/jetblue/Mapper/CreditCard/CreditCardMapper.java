package org.jetblue.jetblue.Mapper.CreditCard;

import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Models.DAO.CreditCard;
import org.jetblue.jetblue.Utils.EncryptInfoUtils;
import org.springframework.stereotype.Component;

@Component
public class CreditCardMapper {

    static EncryptInfoUtils encryptInfoUtils;
    public CreditCardMapper(EncryptInfoUtils encryptInfoUtils) {
        CreditCardMapper.encryptInfoUtils = encryptInfoUtils;
    }

    public static CreditCardResponse toCreditCardResponse(CreditCard save) {
        return CreditCardResponse
                .builder()
                .id(save.getId())
                .cardType(save.getCardType().name())
                .cardHolderName(save.getCardHolderName())
                .expirationDate(save.getExpirationDate())
                .billingAddress(save.getBillingAddress())
                .lastFourDigits(String.format("**** **** **** %s", save.getLastFourDigits()))
                .userName(save.getUser().getUsername())
                .build();
    }

    public static FullCreditCardInfoResponse toFullCreditCardInfoResponse(CreditCard creditCard) throws Exception {
        try {
            return FullCreditCardInfoResponse
                    .builder()
                    .userName(creditCard.getUser().getUsername())
                    .cardHolderName(creditCard.getCardHolderName())
                    .cardNumber(String.format("**** **** **** %s", creditCard.getLastFourDigits()))
                    .cvv(encryptInfoUtils.decrypt(creditCard.getCvv()))
                    .cardType(creditCard.getCardType().name())
                    .expirationDate(creditCard.getExpirationDate())
                    .billingAddress(creditCard.getBillingAddress())
                    .build();
        } catch (Exception e) {
            throw new Exception("Error decrypting CVV: " + e.getMessage());
        }
    }
}
