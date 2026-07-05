package com.infosys.retail.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import com.infosys.retail.dao.ItemRepository;
import com.infosys.retail.dto.entity.ItemEntity;

@Component
public class CacheService implements CommandLineRunner{
	
	@Autowired
	CacheManager cacheManager;
	
	@Autowired
	ItemRepository itemRepo;

	@Override
	public void run(String... args) throws Exception {
		saveItems();
		putItems();
	}
	
	private void saveItems() {
		ItemEntity item1 = new ItemEntity();
		item1.setName("Computer");
		item1.setPrice(100);

		ItemEntity item2 = new ItemEntity();
		item2.setName("Keyboard");
		item2.setPrice(20);

		ItemEntity item3 = new ItemEntity();
		item3.setName("Mouse");
		item3.setPrice(10);

		ItemEntity item4 = new ItemEntity();
		item4.setName("Headphones");
		item4.setPrice(10);

		List<ItemEntity> alist = new ArrayList<>();

		alist.add(item1);
		alist.add(item2);
		alist.add(item3);
		alist.add(item4);

		itemRepo.saveAll(alist); 
	}

	@SuppressWarnings("unchecked")
	public List<ItemEntity> getItems() {
		System.out.println("Cache Names");
		Cache c =	cacheManager.getCache("items");
		System.out.println(c.getName());
		return (List<ItemEntity>)c.get("all_items_list").get();
	}	
	
	public List<ItemEntity> putItems() {		
		List<ItemEntity> list = itemRepo.findAll();
		Cache c = cacheManager.getCache("items");
		c.put("all_items_list", list);
		return list;
	}
	
}
