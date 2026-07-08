package ec.edu.ups.icc.fundamentos01.products.models;

import java.time.LocalDateTime;
import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;

public class Product {
    private Long id;
    private String name;
    private Double price;
    private Integer stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deleted;

    // Constructor vacío
    public Product() {}

    // --- 1. FACTORY METHODS (Crear el modelo desde otras fuentes) ---

    public static Product fromDto(CreateProductDto dto) {
        Product product = new Product();
        product.name = dto.getName();
        product.price = dto.getPrice();
        product.stock = dto.getStock();
        return product;
    }

    public static Product fromEntity(ProductEntity entity) {
        Product product = new Product();
        product.id = entity.getId();
        product.name = entity.getName();
        product.price = entity.getPrice();
        product.stock = entity.getStock();
        product.createdAt = entity.getCreatedAt();
        product.updatedAt = entity.getUpdatedAt();
        product.deleted = entity.isDeleted();
        return product;
    }

    // --- 2. CONVERSION METHODS (Exportar el modelo a otros formatos) ---

    public ProductEntity toEntity() {
        ProductEntity entity = new ProductEntity();
        if (this.id != null) {
            entity.setId(this.id);
        }
        entity.setName(this.name);
        entity.setPrice(this.price);
        entity.setStock(this.stock);
        // Nota: createdAt, updatedAt y deleted los maneja automáticamente 
        // tu BaseEntity de JPA, por eso no se setean aquí.
        return entity;
    }

    public ProductResponseDto toResponseDto() {
        return new ProductResponseDto(
            this.id,
            this.name,
            this.price,
            this.stock,
            null, // owner
            null, // category
            this.createdAt,
            this.updatedAt
        );
    }

    // --- 3. REGLAS DE ACTUALIZACIÓN ---

    public void update(UpdateProductDto dto) {
        this.name = dto.getName();
        this.price = dto.getPrice();
        this.stock = dto.getStock();
    }

    public void partialUpdate(PartialUpdateProductDto dto) {
        if (dto.getName() != null) {
            this.name = dto.getName();
        }
        if (dto.getPrice() != null) {
            this.price = dto.getPrice();
        }
        if (dto.getStock() != null) {
            this.stock = dto.getStock();
        }
    }

    // --- 4. GETTERS Y SETTERS TRADICIONALES ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
}
