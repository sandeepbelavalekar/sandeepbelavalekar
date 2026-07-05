package com.infosys.retail.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

import com.infosys.retail.dao.CustomerRepository;
import com.infosys.retail.dao.PurchaseRepository;
import com.infosys.retail.dto.CustomerDTO;
import com.infosys.retail.dto.ItemDTO;
import com.infosys.retail.dto.PurchaseDTO;
import com.infosys.retail.dto.ResponseData;
import com.infosys.retail.dto.entity.CustomerEntity;
import com.infosys.retail.dto.entity.ItemEntity;
import com.infosys.retail.dto.entity.PurchaseEntity;

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
		entity.setPointsBalance(BigDecimal.ZERO);
		
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
		
		assertEquals("Transaction successful.! Earned Rewards: 856.90",responseData.getMsg());
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
		entity.setPointsBalance(new BigDecimal(100));
		
		PurchaseEntity p1=new PurchaseEntity();
		p1.setCustomerId(1);
		p1.setId(1);
		p1.setPoints(new BigDecimal(12));
		p1.setTotalPrice(100.50);
		p1.setPurchaseDate(new Date());
		
		PurchaseEntity p2=new PurchaseEntity();
		p2.setCustomerId(1);
		p2.setId(2);
		p2.setPoints(new BigDecimal(14));
		p2.setTotalPrice(200.50);
		p2.setPurchaseDate(new Date());
		
		List<PurchaseEntity> list = new ArrayList<>();		
		list.add(p1); 
		list.add(p2);
		entity.setPurchaseList(list);
		when(repo.findByPhoneNumber("123-456")).thenReturn(entity);
		
		ResponseData responseData =	rewardService.getBalance("123-456");
		assertEquals("Purchase History",responseData.getMsg());		
		Map<String,BigDecimal> map = new HashMap<>();
		map.put("Jul/2026", new BigDecimal(26.00).setScale(2,RoundingMode.UP));
		map.put("Total", new BigDecimal(100.00).setScale(2,RoundingMode.UP));
		assertEquals(map, responseData.getData());
		
		when(repo.findByPhoneNumber("123-456")).thenReturn(null);
		
		responseData =	rewardService.getBalance("123-456");
		assertEquals("Phone number not found",responseData.getMsg());		
		
	}

}
