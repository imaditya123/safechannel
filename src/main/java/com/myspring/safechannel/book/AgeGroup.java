package com.myspring.safechannel.book;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AgeGroup {
    INFANT_TODDLER("Infant/Toddler"),
    PRESCHOOLER("Preschooler"),
    CHILD("Child"),
    TEENAGER_ADOLESCENT("Teenager/Adolescent"),
    YOUNG_ADULT("Young Adult"),
    ADULT("Adult"),
    SENIOR_CITIZEN("Senior/Senior Citizen/Elderly");

    private final String displayName;

    AgeGroup(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
    
    
    @JsonValue
    public String getValue() {
        return displayName;
    } 
}
