package com.zhurzh.commonjpa.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"id"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "faq_table")
public class FAQ {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String question;

    @Column(columnDefinition = "TEXT")
    private String answer;

    @Column(nullable = false)
    private String language;
    @Builder.Default
    private Integer popularityScore = 0;

    @Column(nullable = false)
    private Long fromUserId;
    @Builder.Default
    private Integer viewCount = 0;

    @CreationTimestamp
    private LocalDate creationDate;


}
