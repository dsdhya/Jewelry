package ru.university.jewelry.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.university.jewelry.dto.JewelryRequest;
import ru.university.jewelry.dto.UserRequest;
import ru.university.jewelry.model.Category;
import ru.university.jewelry.model.MetalType;
import ru.university.jewelry.repository.CategoryRepository;
import ru.university.jewelry.service.JewelryService;
import ru.university.jewelry.service.UserService;

// демо-данные: данные хранятся в памяти, поэтому заполняем их при каждом запуске
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final JewelryService jewelryService;
    private final UserService userService;

    @Override
    public void run(String... args) {
        // данные теперь хранятся в базе: заполняем только пустую базу
        if (categoryRepository.count() > 0) {
            return;
        }
        Long rings = categoryRepository.save(Category.builder().name("Кольца").build()).getId();
        Long earrings = categoryRepository.save(Category.builder().name("Серьги").build()).getId();
        Long chains = categoryRepository.save(Category.builder().name("Цепочки").build()).getId();

        jewelryService.create(new JewelryRequest("R-585-0001", "Помолвочное кольцо с бриллиантом", rings,
                MetalType.WHITE_GOLD, 585, new BigDecimal("2.45"), new BigDecimal("17.0"), "бриллиант",
                new BigDecimal("54990"), 3));
        jewelryService.create(new JewelryRequest("R-925-0002", "Кольцо с фианитами", rings,
                MetalType.SILVER, 925, new BigDecimal("1.80"), new BigDecimal("18.0"), "фианит",
                new BigDecimal("2490"), 10));
        jewelryService.create(new JewelryRequest("E-750-0003", "Серьги с сапфирами", earrings,
                MetalType.GOLD, 750, new BigDecimal("3.10"), null, "сапфир",
                new BigDecimal("78500"), 4));
        jewelryService.create(new JewelryRequest("C-585-0004", "Цепочка якорного плетения", chains,
                MetalType.GOLD, 585, new BigDecimal("4.20"), new BigDecimal("50"), null,
                new BigDecimal("32900"), 5));

        userService.register(new UserRequest("Иванова Анна", "anna@example.com", "+79990000001"));
    }
}
