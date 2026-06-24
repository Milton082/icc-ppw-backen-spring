package ec.edu.ups.icc.fundamentos01.products.services;

import java.util.List;

import org.springframework.stereotype.Service;

import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.products.mappers.ProductMapper;
import ec.edu.ups.icc.fundamentos01.products.models.ProductModel;
import ec.edu.ups.icc.fundamentos01.products.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(
            ProductRepository productRepository) {

        this.productRepository = productRepository;
    }

    /*
     * Obtener todos los productos.
     */
    @Override
    public List<ProductResponseDto> findAll() {

        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toModelFromEntity)
                .map(ProductMapper::toResponse)
                .toList();
    }

    /*
     * Obtener un producto por ID.
     */
    @Override
    public ProductResponseDto findOne(Long id) {

        return productRepository.findById(id)
                .map(ProductMapper::toModelFromEntity)
                .map(ProductMapper::toResponse)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Product not found"));
    }

    /*
     * Crear un producto.
     */
    @Override
    public ProductResponseDto create(
            CreateProductDto dto) {

        ProductModel model =
                ProductMapper.toModelFromDto(dto);

        ProductEntity entity =
                ProductMapper.toEntityFromModel(model);

        ProductEntity savedEntity =
                productRepository.save(entity);

        ProductModel savedModel =
                ProductMapper.toModelFromEntity(
                        savedEntity);

        return ProductMapper.toResponse(savedModel);
    }

    /*
     * Actualización completa.
     */
    @Override
    public ProductResponseDto update(
            Long id,
            UpdateProductDto dto) {

        ProductEntity entity =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Product not found"));

        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setStock(dto.getStock());

        ProductEntity savedEntity =
                productRepository.save(entity);

        ProductModel model =
                ProductMapper.toModelFromEntity(
                        savedEntity);

        return ProductMapper.toResponse(model);
    }

    /*
     * Actualización parcial.
     */
    @Override
    public ProductResponseDto partialUpdate(
            Long id,
            PartialUpdateProductDto dto) {

        ProductEntity entity =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Product not found"));

        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }

        if (dto.getPrice() != null) {
            entity.setPrice(dto.getPrice());
        }

        if (dto.getStock() != null) {
            entity.setStock(dto.getStock());
        }

        ProductEntity savedEntity =
                productRepository.save(entity);

        ProductModel model =
                ProductMapper.toModelFromEntity(
                        savedEntity);

        return ProductMapper.toResponse(model);
    }

    /*
     * Eliminación lógica.
     */
    @Override
    public void delete(Long id) {

        ProductEntity entity =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Product not found"));

        entity.setDeleted(true);

        productRepository.save(entity);
    }
}