package by.mluvse.javabot.enums;

import lombok.Getter;

@Getter
public enum ActivityType {
    SEDENTARY(1.2),
    STEPS(1.2),
    STRENGTH(1.2),
    MIXED(1.2);

    private final double baseCoefficient;

    ActivityType(double baseCoefficient) {
        this.baseCoefficient=baseCoefficient;
    }

}
