package io.Wolfenstein.DaoImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jongo.Jongo;
import org.springframework.stereotype.Repository;

import com.mongodb.WriteResult;

import io.Wolfenstein.Constants.MongoConstants;
import io.Wolfenstein.Pojo.BaseResponse;
import io.Wolfenstein.Pojo.CartItemDetails;
import io.Wolfenstein.Pojo.Counter;
import io.Wolfenstein.Pojo.DelivaryAddress;
import io.Wolfenstein.Pojo.ProductDetails;
import io.Wolfenstein.Pojo.ProductIdList;
import io.Wolfenstein.Pojo.UserDetails;
import io.Wolfenstein.Util.*;



@Repository
public class DaoImpl implements Dao {

	@Override
	public BaseResponse employeeExists(UserDetails userDetail) {
		UserDetails usrDetails = null;
		usrDetails = new Jongo(MongoConnectionUtil.getDB()).getCollection(MongoConstants.CN_USERDETAILS)
				.findOne("{$or:[{emailId:#},{phoneNo:#}]}", userDetail.getEmailId(), userDetail.getPhoneNo())
				.as(UserDetails.class);
		BaseResponse baseResponse = new BaseResponse();
		if (usrDetails != null) {
			baseResponse.setStatusCode(401);
			if (usrDetails.getEmailId().equals(userDetail.getEmailId())) {
				baseResponse.setStatusMessage("Email already exists");
			} else if (userDetail.getPhoneNo() == usrDetails.getPhoneNo()) {
				baseResponse.setStatusMessage("Phone Number already exists");
			}
		} else {
			baseResponse.setStatusCode(200);
			baseResponse.setStatusMessage("Successful");
		}
		return baseResponse;
	}





	@Override
	public BaseResponse addToCast(ProductIdList productIdList) {
		Iterator<ProductDetails> proIterator = null;
		List<ProductDetails> productDetails = null;
		BaseResponse baseResponse=new BaseResponse();
		proIterator = new Jongo(MongoConnectionUtil.getDB()).getCollection(MongoConstants.CN_PRODUCTDETAILS)
				.find("{productId:{$in:#}}",productIdList.getProductIdList()).as(ProductDetails.class);
		if(proIterator!=null)
		{
			productDetails=CommonUtil.convertFromIteratorToList(proIterator);
			if(productDetails!=null&&!productDetails.isEmpty())
			{
				for (ProductDetails productDetail : productDetails) {
					CartItemDetails cartItemDetail=new CartItemDetails(); 
					cartItemDetail.setProductDetails(productDetail);
					cartItemDetail.setPaymentStatus(false);
					cartItemDetail.setUserId(productIdList.getUserId());
					cartItemDetail.setRating(0);
					new Jongo(MongoConnectionUtil.getDB()).getCollection(MongoConstants.CN_CARTIREM).insert(cartItemDetail);	
					
				}
				baseResponse.setStatusCode(200);
				baseResponse.setStatusMessage("Successful");
			}
			
		}
		else {
			baseResponse.setStatusCode(401);
			baseResponse.setStatusMessage("Failure");
		}
		return baseResponse;
	}


	@Override
	public BaseResponse upatePaymentStatus(ProductIdList productIdList) {
		
		WriteResult writeResult = new Jongo(MongoConnectionUtil.getDB())
				.getCollection(MongoConstants.CN_CARTIREM)
				.update("{'productDetails.productId': {$in:#},'userId':# }",productIdList.getProductIdList(),productIdList.getUserId()).multi().with("{$set:{paymentStatus:true}}");
		BaseResponse baseResponse=new BaseResponse(); 
		if(writeResult.wasAcknowledged()) {
			baseResponse.setStatusCode(200);
			baseResponse.setStatusMessage("Success");
		}
		else {
			baseResponse.setStatusCode(401);
			baseResponse.setStatusMessage("Failure");
		}
		return baseResponse;
	}

	@Override
	public BaseResponse addRating(long productId,long userId, int rating) {
		WriteResult writeResult = new Jongo(MongoConnectionUtil.getDB())
				.getCollection(MongoConstants.CN_CARTIREM)
				.update("{'userId':#,'productDetails.productId':#,paymentStatus:true}",userId,productId).multi().with("{$set:{rating:#}}",rating);
		BaseResponse baseResponse=new BaseResponse(); 
		if(writeResult.wasAcknowledged()) {
			baseResponse.setStatusCode(200);
			baseResponse.setStatusMessage("Success");
		}
		else {
			baseResponse.setStatusCode(401);
			baseResponse.setStatusMessage("Failure");
		}
		return baseResponse;
	}
	public BaseResponse addDelivaryAddress(DelivaryAddress delivaryAddress) {
		delivaryAddress.setAddressType(delivaryAddress.getAddressType().toLowerCase());
		WriteResult writeResult = new Jongo(MongoConnectionUtil.getDB())
				.getCollection(MongoConstants.CN_DELIVARYADDRESS)
				.update("{'userId':#,'addressType':#}",delivaryAddress.getUserId(),delivaryAddress.getAddressType()).upsert().with(delivaryAddress);
		BaseResponse baseResponse=new BaseResponse(); 
		if(writeResult.wasAcknowledged()) {
			baseResponse.setStatusCode(200);
			baseResponse.setStatusMessage("Success");
		}
		else {
			baseResponse.setStatusCode(401);
			baseResponse.setStatusMessage("Failure");
		}
		return baseResponse;
		
	}


	@Override
	public UserDetails addUser(UserDetails userDetail) {
		new Jongo(MongoConnectionUtil.getDB()).getCollection(MongoConstants.CN_COUNTER).update("{'id':'empIdCounter'}")
				.with("{$inc:{userCounter:1}}");
		Counter counter = new Jongo(MongoConnectionUtil.getDB()).getCollection(MongoConstants.CN_COUNTER)
				.findOne("{'id':'empIdCounter'}").as(Counter.class);
		userDetail.setUserId(Double.valueOf(counter.getUserCounter()).longValue());
		new Jongo(MongoConnectionUtil.getDB()).getCollection(MongoConstants.CN_USERDETAILS).insert(userDetail);
		UserDetails userDetails = new Jongo(MongoConnectionUtil.getDB()).getCollection(MongoConstants.CN_USERDETAILS)
				.findOne("{'userId':#}", userDetail.getUserId()).as(UserDetails.class);
		if (userDetails != null) {
			try {
				EncryptDecrypt encryptDecrypt = new EncryptDecrypt();
				String password;
				password = encryptDecrypt.encrypt(userDetails.getEmailId().substring(0, 3) + 123);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userId", userDetails.getUserId());
				map.put("emailId", userDetails.getEmailId());
				map.put("password", password);
				new Jongo(MongoConnectionUtil.getDB()).getCollection(MongoConstants.CN_LOGINCREDENTIAL).insert(map);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return userDetails;
	}
}
