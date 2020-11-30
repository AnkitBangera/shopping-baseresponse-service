package io.Wolfenstein.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import io.Wolfenstein.DaoImpl.Dao;
import io.Wolfenstein.Pojo.BaseResponse;
import io.Wolfenstein.Pojo.DelivaryAddress;
import io.Wolfenstein.Pojo.ProductIdList;
import io.Wolfenstein.Pojo.UserDetails;
import io.Wolfenstein.Util.EmailUtil;


@org.springframework.stereotype.Service
public class Service {
	@Autowired
	Dao dao;
	@Autowired
	EmailUtil email;

	@HystrixCommand(fallbackMethod = "getFallBackBaseResponse", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "500") })
	public BaseResponse addUser(UserDetails userDetails) {
		BaseResponse baseResponse = dao.employeeExists(userDetails);
		if (baseResponse.getStatusCode() == 200) {
			UserDetails details = dao.addUser(userDetails);
			if (details != null) {
				try {
					email.sendmail(userDetails.getEmailId().substring(0, 3) + 123, userDetails.getEmailId(),
							userDetails.getFirstName() + " " + userDetails.getLastName(), "");
				} catch (Exception e) {
					e.printStackTrace();
				}
				return baseResponse;
			}
		}
		return baseResponse;
	}
	@HystrixCommand(fallbackMethod = "getFallBackBaseResponse", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "500") })
	public BaseResponse addToCart(ProductIdList productIdList) {
		return dao.addToCast(productIdList);
	}
	@HystrixCommand(fallbackMethod = "getFallBackBaseResponse", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "500") })
	public BaseResponse paymentUpdation(ProductIdList productIdList) {
		return dao.upatePaymentStatus(productIdList);
	}
	@HystrixCommand(fallbackMethod = "getFallBackBaseResponse", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "500") })
	public BaseResponse addReview(long productId, long userId, int rating) {
		return dao.addRating(productId, userId, rating);
	}
	@HystrixCommand(fallbackMethod = "getFallBackBaseResponse", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "500") })
	public BaseResponse addDelivaryAddress(DelivaryAddress delivaryAddress) {
		return dao.addDelivaryAddress(delivaryAddress);
	}

	public BaseResponse getFallBackBaseResponse() {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatusCode(400);
		baseResponse.setStatusMessage("Try Again Later");
		return baseResponse;
	}
}
