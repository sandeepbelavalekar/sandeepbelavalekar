package com.infosys.retail.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
		System.out.println(cacheManager.getClass().getName());		
		putItems();
		
	}
	
	//@Cacheable(cacheNames = "items", key = "all_items_list" )
	public List<ItemEntity> getItems() {
		System.out.println("Cache Names");
		Cache c =	cacheManager.getCache("items");
		System.out.println(c.getName());
		return (List<ItemEntity>)c.get("all_items_list").get();
	}
	
	//@CachePut(cacheNames = "items", key = "all_items_list")
	public List<ItemEntity> putItems() {
		List<ItemEntity> list = itemRepo.findAll();
		Cache c = cacheManager.getCache("items");
		c.put("all_items_list", list);
		return list;
	}

	
	
}
