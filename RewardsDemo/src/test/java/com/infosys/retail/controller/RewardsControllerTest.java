package com.infosys.retail.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosys.retail.dao.CustomerRepository;
import com.infosys.retail.dto.CustomerDTO;
import com.infosys.retail.dto.ResponseData;
import com.infosys.retail.service.CacheService;
import com.infosys.retail.service.RewardsService;

@org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
public class RewardsControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private RewardsService service;
	
	@MockitoBean
	private CustomerRepository repo;
	
	@MockitoBean
	private CacheService cacheService;
	
	@MockitoBean
	private ConcurrentMapCacheManager cacheManager;
	
	
	@Test
	public void testRegisterCustomer() throws Exception {
		CustomerDTO dto = new CustomerDTO();
		dto.setPhoneNumber("123-456");
		//dto.setName("sddsa");
		dto.setEmail("abc@gmail.com");
		
		ResponseData responseData = new ResponseData("Phone Number Already Exist.");
		//responseData.setStatus(true);
			
		when(service.registerCustomer(dto)).thenReturn(responseData);
		when(repo.findByPhoneNumber(null)).thenReturn(null);
		ObjectMapper objectMapper = new ObjectMapper();
		
		mockMvc.perform(post("/registerCustomer").contentType(MediaType. APPLICATION_JSON)
				  .content(objectMapper.writeValueAsString(dto)).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());		  
		 
	}
	
}
