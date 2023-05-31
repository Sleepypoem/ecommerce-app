package com.sleepypoem.commerceapp.domain.mappers;

import com.sleepypoem.commerceapp.domain.dto.CardDto;
import com.sleepypoem.commerceapp.domain.entities.CardEntity;
import org.springframework.stereotype.Component;

@Component
public class CardMapper extends SimpleMapper<CardDto, CardEntity> {
    @Override
    public CardDto getDtoInstance() {
        return new CardDto();
    }

    @Override
    public CardEntity getEntityInstance() {
        return new CardEntity();
    }
}

