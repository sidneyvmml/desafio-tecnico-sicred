package com.desafio.sicred.votacao.application.core.common;

public record PageRequest(int page, int size) {

    public static final int DEFAULT_SIZE = 20;
    public static final int MAX_SIZE = 100;

    public PageRequest {
        if (page < 0) throw new IllegalArgumentException("Page must be >= 0");
        if (size < 1) throw new IllegalArgumentException("Size must be >= 1");
        if (size > MAX_SIZE) throw new IllegalArgumentException("Size must be <= " + MAX_SIZE);
    }

    public static PageRequest of(int page, int size) {
        return new PageRequest(page, size);
    }

    public static PageRequest ofDefault() {
        return new PageRequest(0, DEFAULT_SIZE);
    }
}
