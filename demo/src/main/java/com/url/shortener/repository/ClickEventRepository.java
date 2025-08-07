package com.url.shortener.repository;

import com.url.shortener.model.ClickEvent;
import com.url.shortener.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {
    List<ClickEvent> findByUrlMappingAndClickBetween(UrlMapping urlMapping, LocalDateTime startDt, LocalDateTime endDt);
    List<ClickEvent> findByUrlMappingsInAndClickBetween(List<UrlMapping> urlMappings, LocalDateTime startDt, LocalDateTime endDt);
}
