package ru.university.jewelry.report;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.university.jewelry.model.Category;
import ru.university.jewelry.model.Jewelry;
import ru.university.jewelry.model.JewelryStatus;
import ru.university.jewelry.model.MetalType;
import ru.university.jewelry.repository.CategoryRepository;
import ru.university.jewelry.repository.JewelryRepository;

@Component
@RequiredArgsConstructor
public class JewelryRowImporter {
    private final JewelryRepository repository;
    private final CategoryRepository categoryRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void importRow(String article, String name, String categoryName,
            MetalType metal, int hallmark, BigDecimal weightGrams,
            BigDecimal price, int stockQuantity) {
        Category category = categoryRepository.findByName(categoryName).orElseGet(
                () -> categoryRepository.save(
                        Category.builder().name(categoryName).build()));
        repository.save(Jewelry.builder()
                .article(article)
                .name(name)
                .category(category)
                .metal(metal)
                .hallmark(hallmark)
                .weightGrams(weightGrams)
                .price(price)
                .stockQuantity(stockQuantity)
                .status(stockQuantity > 0 ? JewelryStatus.AVAILABLE : JewelryStatus.OUT_OF_STOCK)
                .build());
    }
}
