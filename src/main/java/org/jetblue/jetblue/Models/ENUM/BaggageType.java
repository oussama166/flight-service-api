package org.jetblue.jetblue.Models.ENUM;

import lombok.Getter;

@Getter
public enum BaggageType {
  CARRY_ON("Carry-On"),
  CHECKED("Checked"),
  OVERSIZED("Oversized"),
  SPECIAL_ITEM("Special Item");

  private final String displayName;

  BaggageType(String displayName) {
    this.displayName = displayName;
  }

  public static BaggageType fromString(String baggageType) {
    for (BaggageType type : BaggageType.values()) {
      if (type.name().equalsIgnoreCase(baggageType)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Unknown baggage type: " + baggageType);
  }

  public static String fromBaggageType(BaggageType baggageType) {
    return baggageType.name();
  }

  public static BaggageType fromDisplayName(String displayName) {
    for (BaggageType type : BaggageType.values()) {
      if (type.displayName.equalsIgnoreCase(displayName)) {
        return type;
      }
    }
    throw new IllegalArgumentException(
      "Unknown baggage display name: " + displayName
    );
  }
}
