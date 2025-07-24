package com.url.shortener.model;

import jakarta.persistence.*;

@Entity
public class ClickEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private  UrlMapping urlMapping;

}
