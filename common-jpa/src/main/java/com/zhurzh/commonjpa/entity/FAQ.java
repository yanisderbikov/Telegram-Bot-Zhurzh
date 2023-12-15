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

    @Column(nullable = false)
    private String answer;

    @Column(nullable = false)
    private String language;
    private Integer popularityScore;

    private Integer viewCount;

    @CreationTimestamp
    private LocalDate creationDate;

    // Методы для обновления популярности
    public void incrementViewCount() {
        this.viewCount += 1;
    }

    public void updatePopularityScore(int score) {
        this.popularityScore = score;
    }
}
