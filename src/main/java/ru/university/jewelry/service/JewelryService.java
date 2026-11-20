package ru.university.jewelry.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import ru.university.jewelry.dto.JewelryFilter;
import ru.university.jewelry.dto.JewelryRequest;
import ru.university.jewelry.dto.JewelryResponse;
import ru.university.jewelry.exception.BusinessException;
import ru.university.jewelry.exception.NotFoundException;
import ru.university.jewelry.model.Jewelry;
import ru.university.jewelry.model.JewelryStatus;
import ru.university.jewelry.repository.JewelryRepository;
import ru.university.jewelry.repository.spec.JewelrySpecifications;
import ru.university.jewelry.storage.StorageService;

@Service
@RequiredArgsConstructor
public class JewelryService {

    private final JewelryRepository repository;
    private final CategoryService categoryService;
    private final StorageService storageService;

    @Cacheable("jewelry")
    public Page<JewelryResponse> findAll(JewelryFilter filter, Pageable pageable) {
        return repository.findAll(JewelrySpecifications.withFilter(filter.categoryId(),
                filter.metal(), filter.hallmark(), filter.size(), filter.minPrice(), filter.maxPrice()), pageable)
                .map(this::toResponse);
    }

    @Cacheable("jewelry")
    public JewelryResponse findById(Long id) {
        return toResponse(getEntity(id));
    }

    @CacheEvict(value = "jewelry", allEntries = true)
    public JewelryResponse create(JewelryRequest request) {
        if (repository.existsByArticle(request.article())) {
            throw new BusinessException("Изделие с артикулом " + request.article() + " уже есть");
        }
        Jewelry jewelry = new Jewelry();
        fillFromRequest(jewelry, request);
        return toResponse(repository.save(jewelry));
    }

    @CacheEvict(value = "jewelry", allEntries = true)
    public JewelryResponse update(JewelryRequest request, Long id) {
        Jewelry jewelry = getEntity(id);
        if (!jewelry.getArticle().equals(request.article()) && repository.existsByArticle(request.article())) {
            throw new BusinessException("Изделие с артикулом " + request.article() + " уже есть");
        }
        fillFromRequest(jewelry, request);
        return toResponse(repository.save(jewelry));
    }

    @CacheEvict(value = "jewelry", allEntries = true)
    public void delete(Long id) {
        Jewelry jewelry = getEntity(id);
        jewelry.setStatus(JewelryStatus.DISCONTINUED);
        repository.save(jewelry);
    }

    @Transactional
    @CacheEvict(value = "jewelry", allEntries = true)
    public String uploadPhoto(Long id, MultipartFile file) {
        Jewelry jewelry = getEntity(id);
        String url = storageService.store("jewelry-photos", file);
        jewelry.setPhotoUrl(url);
        return url;
    }

    public Jewelry getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Изделие с id=" + id + " не найдено"));
    }

    @CacheEvict(value = "jewelry", allEntries = true)
    @Transactional
    public void changeStock(Jewelry jewelry, int delta) {
        jewelry.setStockQuantity(jewelry.getStockQuantity() + delta);
        refreshStatus(jewelry);
        repository.save(jewelry);
    }

    private void fillFromRequest(Jewelry jewelry, JewelryRequest request) {
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
                jewelry.getStatus(),
                jewelry.getPhotoUrl());
    }
}
