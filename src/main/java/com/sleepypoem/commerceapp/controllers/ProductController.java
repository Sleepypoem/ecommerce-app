package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.domain.dto.ProductDto;
import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import com.sleepypoem.commerceapp.services.AbstractService;
import com.sleepypoem.commerceapp.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
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

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getOneById(@PathVariable Long id) {
        Optional<ProductDto> searchedProduct = getOneByIdInternal(id);
        if (searchedProduct.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(searchedProduct.get());
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody ProductEntity product) throws URISyntaxException {
        return ResponseEntity.created(new URI("/products")).body(createInternal(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @RequestBody ProductEntity product) {
        return ResponseEntity.ok().body(updateInternal(id, product));
    }
}