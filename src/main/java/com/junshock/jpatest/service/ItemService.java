package com.junshock.jpatest.service;

import com.junshock.jpatest.domain.item.Book;
import com.junshock.jpatest.domain.item.Item;
import com.junshock.jpatest.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional // 준영속 엔티티 변경감지(더티체크) 방식으로 수정, em.merge()와 동작방식 동일
    public Item updateItem(Long itemId, Book param){
        Item findItem = itemRepository.findOne(itemId); // 바뀐걸 jpa가 아는 시점
        findItem.setPrice(param.getPrice());
        findItem.setName(param.getName());
        findItem.setStockQuantity(param.getStockQuantity());
        return findItem;
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

}
