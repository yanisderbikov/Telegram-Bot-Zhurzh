package com.zhurzh.commonjpa.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
//@EqualsAndHashCode(exclude = {"id", "state"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "app_user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String len;
    @EqualsAndHashCode.Include
    private Long telegramUserId;
    private Long chatId;
    @CreationTimestamp
    private LocalDateTime firstLoginDate;
    private String telegramUserName;
    @Enumerated(EnumType.STRING)
    private UserState state;


//    private List<String> userFreeDays;


//    @Override
//    public String toString() {
//        return String.format("ID %s %s %s %s [@%s] location : %s",
//                id, lastName, firstName, fatherName, telegramUserName, location.getStreet());
//    }
}

//Как сдеать так чтобы  Locations location отображались в формате toString?