package com.brawl.stars.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Rare {

    INITIAL("INITIAL"),
    WAY_TO_GLORY("WAY_TO_GLORY"),
    RARE("RARE"),
    SUPER_RARE("SUPER_RARE"),
    EPIC("EPIC"),
    MYTHICAL("MYTHICAL"),
    LEGENDARY("LEGENDARY"),
    CHROMATIC("CHROMATIC");

    private final String value;
}
