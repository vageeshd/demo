package com.url.shortener.service;

import com.url.shortener.dtos.ClickEventDTO;
import com.url.shortener.dtos.UrlMappingDTO;
import com.url.shortener.model.ClickEvent;
import com.url.shortener.model.UrlMapping;
import com.url.shortener.model.User;
import com.url.shortener.repository.ClickEventRepository;
import com.url.shortener.repository.UrlMappingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UrlMappingService {

    private UrlMappingRepository urlMappingRepository;
    private ClickEventRepository clickEventRepository;

    public UrlMappingDTO createShortUrl(String originalUrl, User user){
        UrlMapping urlMapping = new UrlMapping();
        String shortUrl = generateShortUrl();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setUser(user);
        urlMapping.setDateTime(LocalDateTime.now());
        return converToDto(urlMapping);
    }

    private String generateShortUrl() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<8; i++){
            sb.append(chars.charAt(new Random().nextInt(chars.length())));
        }
        return sb.toString();
    }

    private UrlMappingDTO converToDto(UrlMapping urlMapping){
        UrlMappingDTO urlMappingDTO = new UrlMappingDTO();
        urlMappingDTO.setOriginalUrl(urlMapping.getOriginalUrl());
        urlMappingDTO.setShortUrl(urlMapping.getShortUrl());
        urlMappingDTO.setDateTime(urlMapping.getDateTime());
        urlMappingDTO.setId(urlMapping.getId());
        urlMappingDTO.setClickCount(urlMapping.getVisitCount());
        urlMappingDTO.setUserName(urlMapping.getUser().getEmail());
        return urlMappingDTO;
    }

    public List<UrlMappingDTO> getUrlsByUser(User user){
        return urlMappingRepository.findByUser(user).stream().map(this::converToDto).toList();
    }

    public List<ClickEventDTO> getClickEventsByDate(String shortUrl, LocalDateTime start, LocalDateTime end){
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if(Objects.nonNull(urlMapping)){
            return clickEventRepository.findByUrlMappingAndClickBetween(urlMapping, start, end).stream()
                    .collect(Collectors.groupingBy(entries-> entries.getDateTime().toLocalDate(), Collectors.counting()))
                    .entrySet().stream().map(entries->{
                        ClickEventDTO click = new ClickEventDTO();
                        click.setClickDate(entries.getKey());
                        click.setCount(entries.getValue());
                        return click;
                    }).toList();
        }
        return null;
    }

    public Map<LocalDate, Long> getTotalClicksByUserAndDate(User user, LocalDate startDt, LocalDate endDt){
        List<UrlMapping> urlMappings = urlMappingRepository.findByUser(user);
        List<ClickEvent> clicks = clickEventRepository.findByUrlMappingsInAndClickBetween(urlMappings, startDt.atStartOfDay(), endDt.plusDays(1).atStartOfDay());
        return clicks.stream().collect(Collectors.groupingBy(entries->entries.getDateTime().toLocalDate(), Collectors.counting()));
    }

    public UrlMapping getOriginalUrl(String shortUrl){
        UrlMapping originalUrl = urlMappingRepository.findByShortUrl(shortUrl);
        if(Objects.nonNull(originalUrl)){
            originalUrl.setVisitCount(originalUrl.getVisitCount() + 1);
            urlMappingRepository.save(originalUrl);

            ClickEvent clickEvent = new ClickEvent();
            clickEvent.setUrlMapping(originalUrl);
            clickEvent.setDateTime(LocalDateTime.now());
            clickEventRepository.save(clickEvent);
            return originalUrl;
        }
        return null;
    }
}
