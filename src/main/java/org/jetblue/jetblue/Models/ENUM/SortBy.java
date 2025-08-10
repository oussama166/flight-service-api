package org.jetblue.jetblue.Models.ENUM;

import org.springframework.data.domain.Sort;

public enum SortBy {
    DEPARTURE_TIME,
    ARRIVAL_TIME,
    PRICE;

    public Sort getSort() {
        return switch (this) {
            case DEPARTURE_TIME -> Sort.by("departureTime");
            case ARRIVAL_TIME -> Sort.by("arrivalTime");
            case PRICE -> Sort.by("price");
        };
    }
}
