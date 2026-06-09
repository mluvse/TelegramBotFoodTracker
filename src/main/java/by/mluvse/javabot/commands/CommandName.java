package by.mluvse.javabot.commands;

import lombok.Getter;

@Getter
public enum CommandName {
    START("START"),
    SETTINGS("SETTINGS"),
    CALCULATE_RATION("CALCULATE_DAILY_RATION"),
    CALCULATE_WITH_AI("CALCULATE_DISH_WITH_AI");

    private final String name;


    CommandName(String name) {
        this.name = name;
    }

}
