package org.jetblue.jetblue.Models.ENUM;

public enum PaymentMethod {
    CREDIT_CARD,
    DEBIT_CARD,
    PAYPAL_BALANCE,
    WALLET,
    BANK_TRANSFER,
    APPLE_PAY,
    GOOGLE_PAY;

    static PaymentMethod fromString(String string) {
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.name().equalsIgnoreCase(string)) {
                return method;
            }
        }
        throw new IllegalArgumentException("No enum constant " + PaymentMethod.class.getCanonicalName() + "." + string);
    }

    static String fromPaymentMethodToString(PaymentMethod paymentMethod) {
        return paymentMethod.name();
    }
}
