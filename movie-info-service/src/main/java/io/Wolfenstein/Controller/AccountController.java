package io.Wolfenstein.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.Wolfenstein.Pojo.BaseResponse;
import io.Wolfenstein.Pojo.DelivaryAddress;
import io.Wolfenstein.Pojo.ProductIdList;
import io.Wolfenstein.Pojo.UserDetails;
import io.Wolfenstein.Service.Service;



@RestController
@RequestMapping("account")
public class AccountController {
	@Autowired
	Service service;
	@Autowired 
	private RestTemplate restTemplate;


	@PostMapping("registration")
	public BaseResponse getRespoString(@RequestBody UserDetails userDetails) {
		return service.addUser(userDetails);
	}

	@PostMapping("addToCart")
	public BaseResponse addToCart(@RequestBody ProductIdList productIdList) {
		return service.addToCart(productIdList);

	}


	@PostMapping("payment")
	public BaseResponse updatePayment(@RequestBody ProductIdList productIdList) {
		return service.paymentUpdation(productIdList);

	}
	
	@GetMapping("addReview")
	public BaseResponse addReview(@RequestParam long userId,@RequestParam long productId,@RequestParam int rating) {
		return service.addReview(productId, userId, rating);
	}
	
	@PostMapping("addDelivaryAddress")
	public BaseResponse addDelivaryAddress(@RequestBody DelivaryAddress delivaryAddress) {
		return service.addDelivaryAddress(delivaryAddress);

	}
	
}
