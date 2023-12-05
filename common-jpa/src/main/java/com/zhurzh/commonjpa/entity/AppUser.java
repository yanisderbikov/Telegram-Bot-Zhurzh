package com.zhurzh.commonjpa.entity;

import com.zhurzh.commonjpa.enums.BranchStatus;
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
    private String language;
    @EqualsAndHashCode.Include
    private Long telegramUserId;
    @EqualsAndHashCode.Include
    private Long chatId;
    @CreationTimestamp
    private LocalDateTime firstLoginDate;
    private String telegramUserName;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private BranchStatus branchStatus = BranchStatus.START;
}