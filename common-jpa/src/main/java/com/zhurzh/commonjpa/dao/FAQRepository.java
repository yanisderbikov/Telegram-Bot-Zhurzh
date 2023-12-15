package com.zhurzh.commonjpa.dao;

import com.zhurzh.commonjpa.entity.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface FAQRepository extends JpaRepository<FAQ, Long> {

    @Query("SELECT f FROM FAQ f WHERE f.popularityScore < :popularityScore AND f.language = :language AND f.answer IS NOT NULL ORDER BY f.popularityScore DESC")
    Page<FAQ> findNextByPopularityScoreAndLanguage(@Param("popularityScore") int popularityScore, @Param("language") String language, Pageable pageable);
    List<FAQ> findByLanguageAndAnswerIsNotNullOrderByPopularityScoreDesc(String language, Pageable pageable);

//    @Query("SELECT f FROM FAQ f WHERE f.popularityScore < :popularityScore ORDER BY f.popularityScore DESC")
//    Optional<FAQ> findNextByPopularityScore(@Param("popularityScore") int popularityScore, Pageable pageable);
//    @Query("SELECT f FROM FAQ f WHERE f.popularityScore < :popularityScore AND f.language = :language AND f.answer IS NOT NULL ORDER BY f.popularityScore DESC")
//    Optional<FAQ> findNextPopularFAQWithNonNullAnswer(@Param("popularityScore") int popularityScore, @Param("language") String language, Pageable pageable);


    @Query("SELECT f FROM FAQ f WHERE f.popularityScore < :popularityScore AND f.language = :language AND f.answer IS NOT NULL ORDER BY f.popularityScore DESC")
    Page<FAQ> findNextPopularFAQWithNonNullAnswer(@Param("popularityScore") int popularityScore, @Param("language") String language, Pageable pageable);

}
