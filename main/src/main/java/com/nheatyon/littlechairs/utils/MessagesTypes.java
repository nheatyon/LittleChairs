package com.nheatyon.littlechairs.utils;

import lombok.Getter;

public enum MessagesTypes {

    DEFAULT("\n\n&r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &6[&aLittle&6Chairs &e1.0 by nheatyon&6] \n"
            + "&r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r  \n"
            + "&r &r &r &r &r &r &r &r &r &r &r &3/lc reload &b> &eReload the plugin configuration\n"
            + "&r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r  \n"),
    CONFIG_ERROR("[LittleChairs] Error occurred while reloading config. Disabling..."),
    VERSION_ERROR("[LittleChairs] Error selecting the appropriate plugin version - Cause: %err");

    @Getter private final String value;

    MessagesTypes(final String value) {
        this.value = value;
    }
}
