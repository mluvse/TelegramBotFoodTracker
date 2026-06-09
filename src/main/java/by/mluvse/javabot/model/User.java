package by.mluvse.javabot.model;

import by.mluvse.javabot.enums.ActivityType;
import by.mluvse.javabot.enums.Gender;
import by.mluvse.javabot.enums.Goal;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long chatId;

    private String name;
    private BigDecimal weight;
    private BigDecimal height;
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Goal goal;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ActivityType activityType;
}
