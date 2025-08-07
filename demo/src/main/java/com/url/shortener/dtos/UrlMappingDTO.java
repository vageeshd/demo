package com.url.shortener.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UrlMappingDTO {
    private String originalUrl;
    private String shortUrl;
    private LocalDateTime dateTime;
    private String userName;
    private Integer clickCount;
    private Long id;
}
