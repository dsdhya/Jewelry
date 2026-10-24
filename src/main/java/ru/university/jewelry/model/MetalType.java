package ru.university.jewelry.model;

import java.util.Set;

public enum MetalType {
    GOLD(Set.of(375, 500, 585, 750, 958, 999)),
    WHITE_GOLD(Set.of(375, 500, 585, 750, 958, 999)),
    ROSE_GOLD(Set.of(375, 500, 585, 750, 958, 999)),
    SILVER(Set.of(800, 830, 875, 925, 960, 999)),
    PLATINUM(Set.of(585, 850, 900, 950));

    // допустимые пробы для металла (ГОСТ / Пробирная палата РФ)
    private final Set<Integer> allowedHallmarks;

    MetalType(Set<Integer> allowedHallmarks) {
        this.allowedHallmarks = allowedHallmarks;
    }

    public Set<Integer> getAllowedHallmarks() {
        return allowedHallmarks;
    }

    public boolean isValidHallmark(int hallmark) {
        return allowedHallmarks.contains(hallmark);
    }
}
