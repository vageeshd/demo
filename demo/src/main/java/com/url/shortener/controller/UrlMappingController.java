package com.url.shortener.controller;

import com.url.shortener.dtos.ClickEventDTO;
import com.url.shortener.dtos.UrlMappingDTO;
import com.url.shortener.model.User;
import com.url.shortener.service.UrlMappingService;
import com.url.shortener.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/url")
@AllArgsConstructor
public class UrlMappingController {

    private UserService userService;
    private UrlMappingService urlMappingService;


    @PostMapping("/shorten")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createShortUrl(@RequestBody Map<String, String> rq, Principal principal){
        String originalUrl = rq.get("originalUrl");
        User user = userService.findbyUserName(principal.getName());
        UrlMappingDTO mappingDTO = urlMappingService.createShortUrl(originalUrl, user);
        return ResponseEntity.ok(mappingDTO);
    }

    @GetMapping("/myurls")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserUrls(Principal principal){
        User user = userService.findbyUserName(principal.getName());
        List<UrlMappingDTO> urlMappingsList = urlMappingService.getUrlsByUser(user);
        return ResponseEntity.ok(urlMappingsList);
    }

    @GetMapping("/analytics/{shortUrl}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUrlAnalytics(@PathVariable String shortUrl, @RequestParam("startDate") String startDate,
                                             @RequestParam("endDate") String endDate){
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        List<ClickEventDTO> clickEventDTOS = urlMappingService.getClickEventsByDate(shortUrl, start, end);
        return ResponseEntity.ok(clickEventDTOS);
    }

    @GetMapping("/totalClicks")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getTotalClicksByDate(Principal principal, @RequestParam("startDate") String start,
                                                  @RequestParam("endDate") String end){
        String userName = principal.getName();
        User user = userService.findbyUserName(userName);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate startDt = LocalDate.parse(start, formatter);
        LocalDate endDt = LocalDate.parse(end, formatter);
        Map<LocalDate, Long> map = urlMappingService.getTotalClicksByUserAndDate(user, startDt, endDt);
        return ResponseEntity.ok(map);
    }

}
