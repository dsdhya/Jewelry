package ru.university.jewelry.repository.spec;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import ru.university.jewelry.model.Jewelry;
import ru.university.jewelry.model.JewelryStatus;
import ru.university.jewelry.model.MetalType;

public class JewelrySpecifications {
    public static Specification<Jewelry> withFilter(
            Long categoryId,
            MetalType metal,
            Integer hallmark,
            BigDecimal size,
            BigDecimal minPrice,
            BigDecimal maxPrice) {

        Specification<Jewelry> spec = (root, query, cb) -> cb.notEqual(root.get("status"),
                JewelryStatus.DISCONTINUED);
        return Specification.allOf(
                spec,
                categoryIdEquals(categoryId),
                metalEquals(metal),
                hallmarkEquals(hallmark),
                sizeEquals(size),
                priceGreaterThanOrEquals(minPrice),
                priceLessThanOrEquals(maxPrice));
    }

    private static Specification<Jewelry> categoryIdEquals(Long categoryId) {
        return (root, query, cb) -> categoryId == null ? null
                : cb.equal(root.get("category").get("id"),
                        categoryId);
    }

    private static Specification<Jewelry> metalEquals(MetalType metal) {
        return (root, query, cb) -> metal == null ? null : cb.equal(root.get("metal"), metal);
    }

    private static Specification<Jewelry> hallmarkEquals(Integer hallmark) {
        return (root, query, cb) -> hallmark == null ? null : cb.equal(root.get("hallmark"), hallmark);
    }

    private static Specification<Jewelry> sizeEquals(BigDecimal size) {
        return (root, query, cb) -> size == null ? null : cb.equal(root.get("size"), size);
    }

    private static Specification<Jewelry> priceGreaterThanOrEquals(BigDecimal minPrice) {
        return (root, query, cb) -> minPrice == null ? null
                : cb.greaterThanOrEqualTo(root.get("price"),
                        minPrice);
    }

    private static Specification<Jewelry> priceLessThanOrEquals(BigDecimal maxPrice) {
        return (root, query, cb) -> maxPrice == null ? null
                : cb.lessThanOrEqualTo(root.get("price"),
                        maxPrice);
    }
}
