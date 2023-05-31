package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.controllers.abstracts.AbstractController;
import com.sleepypoem.commerceapp.domain.dto.CardDto;
import com.sleepypoem.commerceapp.domain.dto.PaginatedDto;
import com.sleepypoem.commerceapp.domain.entities.CardEntity;
import com.sleepypoem.commerceapp.domain.mappers.BaseMapper;
import com.sleepypoem.commerceapp.services.CardService;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequestMapping("cards")
public class CardController extends AbstractController<CardDto, CardEntity, Long> {

    private final CardService cardService;
    protected CardController(BaseMapper<CardEntity, CardDto> mapper, CardService cardService) {
        super(mapper);
        this.cardService = cardService;
    }

    @Override
    protected AbstractService<CardEntity, Long> getService() {
        return cardService;
    }

    @GetMapping
    public ResponseEntity<PaginatedDto<CardDto>> getAllPaginatedAndSorted(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                           @RequestParam(value = "size", defaultValue = "10") int size,
                                                                           @RequestParam(value = "sort-by", defaultValue = "id") String sortBy,
                                                                           @RequestParam(value = "sort-order", defaultValue = "asc") String sortOrder) {
        return ResponseEntity.ok().body(getAllPaginatedAndSortedInternal(page, size, sortBy, sortOrder, "cards"));
    }

    @GetMapping(params = {"user-id"}, produces = "application/json")
    public ResponseEntity<PaginatedDto<CardDto>> getByUserId(@RequestParam(value = "user-id") String userId,
                                                              @RequestParam(value = "page", defaultValue = "0") int page,
                                                              @RequestParam(value = "size", defaultValue = "10") int size,
                                                              @RequestParam(value = "sort-by", defaultValue = "id") String sortBy,
                                                              @RequestParam(value = "sort-order", defaultValue = "asc") String sortOrder) {
        //TODO: implement getByUserId functionality
        return ResponseEntity.ok().body(getAllPaginatedAndSortedInternal(page, size, sortBy, sortOrder, "cards"));
    }

    @PostMapping
    public ResponseEntity<CardDto> create(CardEntity card) throws Exception {
        return ResponseEntity.ok().body(createInternal(card));
    }
}
