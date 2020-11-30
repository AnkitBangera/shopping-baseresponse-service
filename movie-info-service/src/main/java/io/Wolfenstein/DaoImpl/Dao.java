package io.Wolfenstein.DaoImpl;
import io.Wolfenstein.Pojo.BaseResponse;
import io.Wolfenstein.Pojo.DelivaryAddress;
import io.Wolfenstein.Pojo.ProductIdList;
import io.Wolfenstein.Pojo.UserDetails;

public interface Dao {
	public UserDetails addUser(UserDetails userDetails);
	
	public BaseResponse employeeExists(UserDetails userDetails);
	
	public BaseResponse addToCast(ProductIdList productIdList);
	
	public BaseResponse upatePaymentStatus(ProductIdList productIdList);
	
	public BaseResponse addRating(long productId,long userId,int rating);
	
	public BaseResponse addDelivaryAddress(DelivaryAddress delivaryAddress);
}
