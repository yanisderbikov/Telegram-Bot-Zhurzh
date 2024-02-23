package com.zhurzh.nodesearemservice.dao;

import com.zhurzh.nodesearemservice.entity.SeaRemFAQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeaRemFaqRepository extends JpaRepository<SeaRemFAQ, Long> {

}
