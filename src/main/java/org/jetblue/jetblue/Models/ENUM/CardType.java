package org.jetblue.jetblue.Models.ENUM;

public enum CardType {
  VISA,
  MASTERCARD,
  AMERICAN_EXPRESS,
  DISCOVER,
  JCB,
  DINERS_CLUB,
  UNIONPAY;

  public static CardType fromString(String cardType) {
    for (CardType type : CardType.values()) {
      if (type.name().equalsIgnoreCase(cardType)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Unknown card type: " + cardType);
  }

  public static String fromCardType(CardType cardType) {
    return cardType.name();
  }
}
