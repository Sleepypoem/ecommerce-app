package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.annotations.security.IsAdminOrSuperUser;
import com.sleepypoem.commerceapp.controllers.abstracts.AbstractController;
import com.sleepypoem.commerceapp.domain.dto.PaginatedDto;
import com.sleepypoem.commerceapp.domain.dto.ResourceStatusResponseDto;
import com.sleepypoem.commerceapp.domain.dto.entities.ProductDto;
import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import com.sleepypoem.commerceapp.domain.mappers.BaseMapper;
import com.sleepypoem.commerceapp.services.ProductService;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
@RequestMapping("products")
@Slf4j
public class ProductController extends AbstractController<ProductDto, ProductEntity, Long> {

    @Autowired
    ProductService service;

    protected ProductController(BaseMapper<ProductEntity, ProductDto> mapper) {
        super(mapper);
    }

    @Override
    public AbstractService<ProductEntity, Long> getService() {
        return service;
    }

    @GetMapping
    public ResponseEntity<PaginatedDto<ProductDto>> getAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                                           @RequestParam(value = "size", defaultValue = "10") int size,
                                                           @RequestParam(value = "sort-by", defaultValue = "id") String sortBy,
                                                           @RequestParam(value = "sort-order", defaultValue = "asc") String sortOrder) {
        return ResponseEntity.ok().body(getAllPaginatedAndSortedInternal(page, size, sortBy, sortOrder, "products"));
    }

    @PostMapping
    @IsAdminOrSuperUser
    public ResponseEntity<ResourceStatusResponseDto> create(@RequestBody ProductEntity product) {
        ProductDto created = createInternal(product);
        String message = "Product created with id " + created.getId();
        String url = "GET : /api/products/" + created.getId();
        return ResponseEntity
                .created(URI.create("/api/products/" + created.getId()))
                .body(new ResourceStatusResponseDto(String.valueOf(created.getId()), message, url));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findOneById(@PathVariable Long id) {
        return ResponseEntity.ok().body(getOneByIdInternal(id));
    }

    @PutMapping("/{id}")
    @IsAdminOrSuperUser
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @RequestBody ProductEntity product) {
        return ResponseEntity.ok().body(updateInternal(id, product));
    }
}
