package com.retail.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.retail.demo.dao.CustomerRepository;
import com.retail.demo.dao.PurchaseRepository;
import com.retail.demo.dto.CustomerDTO;
import com.retail.demo.dto.ItemDTO;
import com.retail.demo.dto.PurchaseDTO;
import com.retail.demo.dto.ResponseData;
import com.retail.demo.dto.entity.CustomerEntity;
import com.retail.demo.dto.entity.ItemEntity;
import com.retail.demo.dto.entity.PurchaseEntity;

@ExtendWith(MockitoExtension.class)
public class RewardServiceTest {
	
	@Mock
	private CustomerRepository repo;
	
	@Mock
	CacheService cacheService;
	
	@Mock
	PurchaseRepository purchaseRepo;
	
	@InjectMocks
    private RewardsService rewardService; 	
	
	@Test
	public void testRegisterCustomer() {
		
		CustomerEntity entity = new CustomerEntity();
		entity.setAddress("ABC");
		entity.setEmail("sandeepbel@gmail.com");
		entity.setId(1);
		
		when(repo.findByPhoneNumber("123-456")).thenReturn(entity);		
		
		CustomerDTO dto = new CustomerDTO();
		dto.setPhoneNumber("123-456");
		ResponseData responseData =	rewardService.registerCustomer(dto);
		
		assertEquals("Phone Number Already Exist.", responseData.getMsg());
		assertEquals(false, responseData.isStatus());
		
		CustomerEntity entity1 = new CustomerEntity();
		entity1.setAddress("ABC");
		entity1.setEmail("sandeepbel@gmail.com");
		entity1.setId(2);
		
		when(repo.findByPhoneNumber("123-678")).thenReturn(null);
		when(repo.save(any(CustomerEntity.class))).thenReturn(entity1);
		
		dto = new CustomerDTO();
		dto.setPhoneNumber("123-678");
		
		responseData =	rewardService.registerCustomer(dto);
		
		assertEquals("Registration Successful.", responseData.getMsg());
		assertEquals(true, responseData.isStatus());
	}
	
	@Test
	public void testPurchaseItem() {
		
		ItemDTO item1 = new ItemDTO();
		item1.setName("Keypad");
		item1.setQuantity(10);
		
		ItemDTO item2 = new ItemDTO();
		item2.setName("Headsets");
		item2.setQuantity(5);
		
		List<ItemDTO> alist = new ArrayList<>();
		alist.add(item1); alist.add(item2);
		
		PurchaseDTO dto = new PurchaseDTO();
		
		dto.setCustomerId(2);
		dto.setPhoneNumber("123-456");
		dto.setItemList(alist);
		
		CustomerEntity entity = new CustomerEntity();
		entity.setAddress("ABC");
		entity.setEmail("sandeepbel@gmail.com");
		entity.setId(1);
		
		List<ItemEntity> itemList = new ArrayList<>();
		ItemEntity i1 = new ItemEntity();
		i1.setId(1);
		i1.setName("Keypad");
		i1.setPrice(23.23);

		ItemEntity i2 = new ItemEntity();
		i2.setId(2);
		i2.setName("Headsets");
		i2.setPrice(54.23);
		itemList.add(i1);
		itemList.add(i2);
		
		PurchaseEntity purchaseEntity = new PurchaseEntity();		
		purchaseEntity.setCustomerId(2);
		purchaseEntity.setId(4);
		purchaseEntity.setPurchaseDate(new Date());
		purchaseEntity.setTotalPrice(100);
		
		when(repo.findByPhoneNumber("123-456")).thenReturn(entity);
		when(cacheService.getItems()).thenReturn(itemList);
		when(purchaseRepo.save(any(PurchaseEntity.class))).thenReturn(purchaseEntity);
		when(repo.save(any(CustomerEntity.class))).thenReturn(entity);
		ResponseData responseData =	rewardService.purchaseItem(dto);
		
		assertEquals("Transaction successful.! Earned Rewards: "+856,responseData.getMsg());
		assertEquals(true, responseData.isStatus());
		
		when(repo.findByPhoneNumber("123-456")).thenReturn(null);
		
		PurchaseDTO purchaseDTO = new PurchaseDTO();
		
		purchaseDTO.setCustomerId(2);
		purchaseDTO.setPhoneNumber("123-456");
		purchaseDTO.setItemList(alist);
		
		responseData = rewardService.purchaseItem(purchaseDTO);
		assertEquals("Phone Number not found", responseData.getMsg());		
	}
	
	@Test
	public void testGetBalance() {		
		CustomerEntity entity = new CustomerEntity();
		entity.setAddress("ABC");
		entity.setEmail("sandeepbel@gmail.com");
		entity.setId(1);
		entity.setPointsBalance(100);
		
		PurchaseEntity p1=new PurchaseEntity();
		p1.setCustomerId(1);
		p1.setId(1);
		p1.setPoints(12);
		p1.setTotalPrice(100.50);
		p1.setPurchaseDate(new Date());
		
		PurchaseEntity p2=new PurchaseEntity();
		p2.setCustomerId(1);
		p2.setId(2);
		p2.setPoints(14);
		p2.setTotalPrice(200.50);
		p2.setPurchaseDate(new Date());
		
		List<PurchaseEntity> list = new ArrayList<>();		
		list.add(p1); 
		list.add(p2);
		entity.setPurchaseList(list);
		when(repo.findByPhoneNumber("123-456")).thenReturn(entity);
		
		ResponseData responseData =	rewardService.getBalance("123-456");
		assertEquals("Purchase History",responseData.getMsg());		
		Map<String,Integer> map = new HashMap<>();
		map.put("June", 26);
		map.put("Total", 100);
		assertEquals(map, responseData.getData());
		
		when(repo.findByPhoneNumber("123-456")).thenReturn(null);
		
		responseData =	rewardService.getBalance("123-456");
		assertEquals("Phone number not found",responseData.getMsg());		
		
	}

}
