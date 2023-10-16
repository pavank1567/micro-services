package com.stalwart.product.service;

import com.stalwart.product.Repository.ProductRepo;
import com.stalwart.product.dto.ProductRequest;
import com.stalwart.product.dto.ProductResponse;
import com.stalwart.product.exceptions.ProductNotFoundException;
import com.stalwart.product.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepo productRepository;

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream().map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

    public Optional<ProductResponse> getProductById(UUID id) {
        return productRepository.findById(id).map(this::mapToProductResponse);
    }

    public void createProduct(ProductRequest request) {

        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();
        productRepository.save(product);
        log.info("Product {} is Saved", product.getId());
    }

    public void updateProduct(ProductRequest request, UUID id) {

        if(!existsWithId(id))
            throw new ProductNotFoundException("Product Not Found");

        ProductResponse oldProduct = getProductById(id).get();

        if(request.getName()==null)
            request.setName(oldProduct.getName());

        if(request.getDescription()==null)
            request.setDescription(oldProduct.getDescription());

        if(request.getPrice()!= oldProduct.getPrice())
            request.setPrice(oldProduct.getPrice());

        Product newProduct = Product.builder()
                .id(id)
                .price(request.getPrice())
                .description(request.getDescription())
                .name(request.getName())
                .build();


        productRepository.save(newProduct);
    }

    private boolean existsWithId(UUID id) {
        return productRepository.existsById(id);
    }

    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }
}
