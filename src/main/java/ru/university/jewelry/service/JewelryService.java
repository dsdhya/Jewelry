package ru.university.jewelry.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.university.jewelry.dto.JewelryFilter;
import ru.university.jewelry.dto.JewelryRequest;
import ru.university.jewelry.dto.JewelryResponse;
import ru.university.jewelry.exception.BusinessException;
import ru.university.jewelry.exception.NotFoundException;
import ru.university.jewelry.model.Jewelry;
import ru.university.jewelry.model.JewelryStatus;
import ru.university.jewelry.repository.JewelryRepository;

@Service
@RequiredArgsConstructor
public class JewelryService {

    private final JewelryRepository repository;
    private final CategoryService categoryService;

    public List<JewelryResponse> findAll(JewelryFilter filter) {
        return repository.findByStatusNotOrderById(JewelryStatus.DISCONTINUED).stream()
                .filter(j -> filter.categoryId() == null || filter.categoryId().equals(j.getCategory().getId()))
                .filter(j -> filter.metal() == null || filter.metal() == j.getMetal())
                .filter(j -> filter.hallmark() == null || filter.hallmark() == j.getHallmark())
                .filter(j -> filter.size() == null
                        || (j.getSize() != null && j.getSize().compareTo(filter.size()) == 0))
                .filter(j -> filter.minPrice() == null || j.getPrice().compareTo(filter.minPrice()) >= 0)
                .filter(j -> filter.maxPrice() == null || j.getPrice().compareTo(filter.maxPrice()) <= 0)
                .map(this::toResponse)
                .toList();
    }

    public JewelryResponse findById(Long id) {
        return toResponse(getEntity(id));
    }

    public JewelryResponse create(JewelryRequest request) {
        if (repository.existsByArticle(request.article())) {
            throw new BusinessException("Изделие с артикулом " + request.article() + " уже есть");
        }
        Jewelry jewelry = new Jewelry();
        fillFromRequest(jewelry, request);
        return toResponse(repository.save(jewelry));
    }

    public JewelryResponse update(JewelryRequest request, Long id) {
        Jewelry jewelry = getEntity(id);
        if (!jewelry.getArticle().equals(request.article()) && repository.existsByArticle(request.article())) {
            throw new BusinessException("Изделие с артикулом " + request.article() + " уже есть");
        }
        fillFromRequest(jewelry, request);
        return toResponse(repository.save(jewelry));
    }

    // товар не удаляем физически: на него ссылаются заказы
    public void delete(Long id) {
        Jewelry jewelry = getEntity(id);
        jewelry.setStatus(JewelryStatus.DISCONTINUED);
        repository.save(jewelry);
    }

    public Jewelry getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Изделие с id=" + id + " не найдено"));
    }

    // вызывается внутри транзакции OrderService, изменения сохранятся вместе с заказом
    @Transactional
    public void changeStock(Jewelry jewelry, int delta) {
        jewelry.setStockQuantity(jewelry.getStockQuantity() + delta);
        refreshStatus(jewelry);
        repository.save(jewelry);
    }

    private void fillFromRequest(Jewelry jewelry, JewelryRequest request) {
        // правило: проба должна соответствовать металлу
        if (!request.metal().isValidHallmark(request.hallmark())) {
            throw new BusinessException("Проба " + request.hallmark() + " недопустима для металла "
                    + request.metal() + ". Допустимые: " + request.metal().getAllowedHallmarks());
        }
        jewelry.setArticle(request.article());
        jewelry.setName(request.name());
        jewelry.setCategory(categoryService.getEntity(request.categoryId()));
        jewelry.setMetal(request.metal());
        jewelry.setHallmark(request.hallmark());
        jewelry.setWeightGrams(request.weightGrams());
        jewelry.setSize(request.size());
        jewelry.setStone(request.stone());
        jewelry.setPrice(request.price());
        jewelry.setStockQuantity(request.stockQuantity());
        refreshStatus(jewelry);
    }

    private void refreshStatus(Jewelry jewelry) {
        if (jewelry.getStatus() == JewelryStatus.DISCONTINUED) {
            return;
        }
        jewelry.setStatus(jewelry.getStockQuantity() > 0 ? JewelryStatus.AVAILABLE : JewelryStatus.OUT_OF_STOCK);
    }

    private JewelryResponse toResponse(Jewelry jewelry) {
        return new JewelryResponse(
                jewelry.getId(),
                jewelry.getArticle(),
                jewelry.getName(),
                categoryService.toResponse(jewelry.getCategory()),
                jewelry.getMetal(),
                jewelry.getHallmark(),
                jewelry.getWeightGrams(),
                jewelry.getSize(),
                jewelry.getStone(),
                jewelry.getPrice(),
                jewelry.getStockQuantity(),
                jewelry.getStatus());
    }
}
