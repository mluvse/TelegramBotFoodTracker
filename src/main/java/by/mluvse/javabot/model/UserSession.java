package by.mluvse.javabot.model;

import by.mluvse.javabot.enums.UserState;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name="user_session")
@Setter
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique= true, nullable= false)
    private Long chatId;

    @Getter
    @Enumerated(EnumType.STRING)
    private UserState state= UserState.IDLE;

    @Getter
    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> tempData=new HashMap<>();


}
