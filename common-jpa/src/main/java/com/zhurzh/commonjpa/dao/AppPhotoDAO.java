package com.zhurzh.commonjpa.dao;

import com.zhurzh.commonjpa.entity.AppPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppPhotoDAO extends JpaRepository<AppPhoto, Long> {
}
