package com.retail.demo.service;

import java.math.BigDecimal;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.retail.demo.dao.CustomerRepository;
import com.retail.demo.dao.PurchaseRepository;
import com.retail.demo.dto.CustomerDTO;
import com.retail.demo.dto.PurchaseDTO;
import com.retail.demo.dto.ResponseData;
import com.retail.demo.dto.entity.CustomerEntity;
import com.retail.demo.dto.entity.ItemEntity;
import com.retail.demo.dto.entity.PurchaseEntity;


@Service
public class RewardsService {
	
	@Autowired
	CustomerRepository repo;
	
	@Autowired
	CacheService cacheService;
	
	@Autowired
	PurchaseRepository purchaseRepo;	 
	
	
	/**
	 * This method is used to register a new customer.
	 * @param customer
	 * @return
	 */
	public ResponseData registerCustomer(CustomerDTO customer) {
		ResponseData responseData = null;
		CustomerEntity entity = null;		
		entity = repo.findByPhoneNumber(customer.getPhoneNumber());
		if(entity!=null) {
			BeanUtils.copyProperties(entity, customer); 
			responseData = new ResponseData("Phone Number Already Exist.");
			responseData.setStatus(false);
			return responseData;
		} else {
			entity = new CustomerEntity();
			BeanUtils.copyProperties(customer, entity);	
			entity.setRegistrationDate(new Date());	
			entity = repo.save(entity);
			customer.setId(entity.getId());
			responseData = new ResponseData("Registration Successful.");
			responseData.setStatus(true);
		}
		return responseData;
	}

	
	/**
	 * This method is used to perform purchase transaction and calculate reward points
	 * @param purchase
	 * @return ResponseData
	 */
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public ResponseData purchaseItem(PurchaseDTO purchase) {
		ResponseData responseData = null;
		CustomerEntity custEntity = repo.findByPhoneNumber(purchase.getPhoneNumber());
		if (custEntity != null) {
			responseData = computeRewards(purchase, custEntity);
			return responseData;
		} else {
			responseData = new ResponseData("Phone Number not found");
			responseData.setStatus(false);
		}
		return responseData;
	}

	private ResponseData computeRewards(PurchaseDTO p, CustomerEntity custEntity) {
		ResponseData responseData;
		List<ItemEntity> l=  cacheService.getItems();
		Map<Object, Double> map = l.stream().collect(Collectors.toMap(f->f.getName(), f->f.getPrice()));
		//System.out.println(map);
		p.getItemList().forEach(c-> {
			if(map.keySet().contains(c.getName())) {
				double price = map.get(c.getName());
				//System.out.println(c.getName()+" "+price+" "+c.getQuantity());
				double totalPrice = p.getTotalPrice() + (c.getQuantity() * price);
				p.setTotalPrice(totalPrice);
			}
		});
		PurchaseEntity purchaseEntity = new PurchaseEntity();
		purchaseEntity.setCustomerId(p.getCustomerId());		
		purchaseEntity.setPurchaseDate(new Date());
		purchaseEntity.setTotalPrice(p.getTotalPrice());		
		int points = 0;
		if(p.getTotalPrice() > 100) {
			points = new BigDecimal((p.getTotalPrice()-100) * 2).intValue();
			points = points+50;			
		}else if(p.getTotalPrice() <= 100 && p.getTotalPrice()> 50) {
			points = 50;
		}
		p.setPoints(points);
		purchaseEntity.setPoints(points);
		purchaseEntity.setCustomerId(custEntity.getId());
		purchaseRepo.save(purchaseEntity);
		custEntity.setPointsBalance(custEntity.getPointsBalance()+p.getPoints());
		repo.save(custEntity);
		responseData = new ResponseData("Transaction successful.! Earned Rewards: "+custEntity.getPointsBalance());
		responseData.setStatus(true);
		return responseData;
	}

	/**
	 * This method is used to check the reward points
	 * @param phoneNumber
	 * @return ResponseData
	 */
	public ResponseData getBalance(String phoneNumber) {
		
		CustomerEntity custEntity =	repo.findByPhoneNumber(phoneNumber);
		
		if (custEntity != null) {
			List<PurchaseEntity> list = custEntity.getPurchaseList();

			CustomerDTO cust = new CustomerDTO();
			BeanUtils.copyProperties(custEntity, cust);
			cust.setPurchaseList(null);
			List<PurchaseDTO> purchaseList = new ArrayList<>();
			Map<String, Integer> map = list.stream()
					.collect(Collectors.toMap(f -> getMonth(f.getPurchaseDate()), v -> v.getPoints(), (x, y) -> x + y));
			map.put("Total", custEntity.getPointsBalance());

			cust.setPurchaseList(purchaseList);
			ResponseData responseData = new ResponseData("Purchase History");
			responseData.setData(map);
			return responseData;
		} else {
			ResponseData responseData = new ResponseData("Phone number not found");
			return responseData;
		}
	}

	private String getMonth(Date purchaseDate) {
		return Month.of(purchaseDate.getMonth()).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
	}
}
