package com.zhurzh.commonjpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.zhurzh.commonjpa.entity.BinaryContent;

public interface BinaryContentDAO extends JpaRepository<BinaryContent, Long> {
}
