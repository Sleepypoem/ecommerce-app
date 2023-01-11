package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.controllers.abstracts.AbstractController;
import com.sleepypoem.commerceapp.domain.dto.ProductDto;
import com.sleepypoem.commerceapp.domain.dto.ResourceAddedResponseDto;
import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import com.sleepypoem.commerceapp.exceptions.MyResourceNotFoundException;
import com.sleepypoem.commerceapp.services.ProductService;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("products")
public class ProductController extends AbstractController<ProductDto, ProductEntity> {

    @Autowired
    ProductService service;

    @Override
    protected AbstractService<ProductDto, ProductEntity> getService() {
        return service;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAll() {
        return ResponseEntity.ok().body(getAllInternal());
    }

    @PostMapping
    public ResponseEntity<ResourceAddedResponseDto> create(@RequestBody ProductEntity product) throws Exception {
        ProductDto created = createInternal(product);
        String message = "Product created with id " + created.getId();
        String url = "GET : /api/products/" + created.getId();
        return ResponseEntity
                .created(URI.create("/api/products/" + created.getId()))
                .body(new ResourceAddedResponseDto(String.valueOf(created.getId()), message, url));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findOneById(@PathVariable Long id) {
        Optional<ProductDto> searched = getOneByIdInternal(id);
        if (searched.isEmpty()) {
            throw new MyResourceNotFoundException("Product with id " + " not found.");
        }
        return ResponseEntity.ok().body(searched.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @RequestBody ProductEntity product) throws Exception {
        return ResponseEntity.ok().body(updateInternal(id, product));
    }
}
