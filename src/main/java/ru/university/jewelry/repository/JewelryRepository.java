package ru.university.jewelry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import ru.university.jewelry.model.Jewelry;
import ru.university.jewelry.model.JewelryStatus;

public interface JewelryRepository extends JpaRepository<Jewelry, Long>, 
JpaSpecificationExecutor<Jewelry> {

    boolean existsByArticle(String article);
    List<Jewelry> findByStatusNotOrderById(JewelryStatus status);
}
