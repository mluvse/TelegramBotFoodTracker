package by.mluvse.javabot.enums;

import lombok.Getter;


@Getter
public enum Goal {
    LOSS("LOSS", -250),
    MAINTAIN("MAINTAIN", 0),
    GAIN("GAIN", 200);

    private final String value;
    private final int calorieDelta;

    Goal(String value, int calorieDelta) {
        this.value = value;
        this.calorieDelta = calorieDelta;
    }

}
