package com.lec.blog.repository;

import org.springframework.stereotype.Component;

import com.lec.blog.utils.factory.BlogFactory;

import javax.annotation.PostConstruct;

@Component
public class DbInit {
    private final BlogUserRepository userRepository;

    public DbInit(BlogUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        BlogFactory.BLOG_USERS.forEach(userRepository::save);
        userRepository.save(BlogFactory.generateBlogAdmin());
    }
}
