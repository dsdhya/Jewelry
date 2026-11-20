package ru.university.jewelry.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "jewelry")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Jewelry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String article;          // артикул, например "R-585-0001"

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetalType metal;

    @Column(nullable = false)
    private int hallmark;            // проба: 585, 750, 925 ...

    @Column(nullable = false)
    private BigDecimal weightGrams;

    private BigDecimal size;         // размер кольца или длина цепочки; null - безразмерное

    private String stone;            // вставка: "бриллиант", "фианит"; null - без камня

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private int stockQuantity;       // остаток на складе

    private String photoUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private JewelryStatus status = JewelryStatus.AVAILABLE;
}
