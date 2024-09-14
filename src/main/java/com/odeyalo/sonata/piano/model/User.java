package com.odeyalo.sonata.piano.model;

import lombok.Value;

@Value
public class User {
    Email email;
    boolean activated;

    public boolean isActivated() {
        return activated;
    }
}
