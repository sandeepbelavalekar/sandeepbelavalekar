package com.infosys.retail.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
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

import com.infosys.retail.dao.CustomerRepository;
import com.infosys.retail.dao.PurchaseRepository;
import com.infosys.retail.dto.CustomerDTO;
import com.infosys.retail.dto.PurchaseDTO;
import com.infosys.retail.dto.ResponseData;
import com.infosys.retail.dto.entity.CustomerEntity;
import com.infosys.retail.dto.entity.ItemEntity;
import com.infosys.retail.dto.entity.PurchaseEntity;


@Service
public class RewardsService {
	
	@Autowired
	CustomerRepository repo;
	
	@Autowired
	CacheService cacheService;
	
	@Autowired
	PurchaseRepository purchaseRepo;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("MMM/yyyy");
	
	
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
			responseData.setStatus(true);
			return responseData;
		} else {
			responseData = new ResponseData("Phone Number not found");
			responseData.setStatus(false);
		}
		return responseData;
	}

	private ResponseData computeRewards(PurchaseDTO purchaseDTO, CustomerEntity custEntity) {
		ResponseData responseData;
		List<ItemEntity> l=  cacheService.getItems();
		Map<Object, Double> map = l.stream().collect(Collectors.toMap(f->f.getName(), f->f.getPrice()));		
		purchaseDTO.getItemList().forEach(c-> {
			if(map.keySet().contains(c.getName())) {
				double price = map.get(c.getName());				
				double totalPrice = purchaseDTO.getTotalPrice() + (c.getQuantity() * price);
				purchaseDTO.setTotalPrice(totalPrice);
			}
		});
		PurchaseEntity purchaseEntity = new PurchaseEntity();
		purchaseEntity.setCustomerId(purchaseDTO.getCustomerId());		
		purchaseEntity.setPurchaseDate(new Date());
		purchaseEntity.setTotalPrice(purchaseDTO.getTotalPrice());		
		BigDecimal points = BigDecimal.ZERO;
		if(purchaseDTO.getTotalPrice() > 100) {
			points = new BigDecimal((purchaseDTO.getTotalPrice()-100) * 2);
			points = points.add(new BigDecimal(50));
		}else if(purchaseDTO.getTotalPrice() <= 100 && purchaseDTO.getTotalPrice()> 50) {
			points = new BigDecimal(50);
		}
		purchaseDTO.setPoints(points);
		purchaseEntity.setPoints(points);
		purchaseEntity.setCustomerId(custEntity.getId());
		purchaseRepo.save(purchaseEntity);
		if(custEntity.getPointsBalance()!=null) {
			custEntity.setPointsBalance(custEntity.getPointsBalance().add(purchaseDTO.getPoints()));
		} else {
			custEntity.setPointsBalance(purchaseDTO.getPoints());
		}
		repo.save(custEntity);
		if(custEntity.getPointsBalance()!=null) {
		responseData = new ResponseData("Transaction successful.! Earned Rewards: "+custEntity.getPointsBalance().setScale(2, RoundingMode.UP));
		} else {
			responseData = new ResponseData("Transaction successful.! Earned Rewards: 0.0");
		}
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
			Map<String, BigDecimal> map = list.stream()
					.collect(Collectors.toMap(f -> getMonth(f.getPurchaseDate()), v -> v.getPoints().setScale(2, RoundingMode.UP), (x, y) -> x.add(y).setScale(2, RoundingMode.UP)));
			if(custEntity.getPointsBalance()!=null) {
				map.put("Total", custEntity.getPointsBalance().setScale(2,RoundingMode.UP));
			}

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
		return sdf.format(purchaseDate);
	}
}
