//package com.naabh.logistics.shippingRateCache;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//
//public interface ShippingRateCacheRepository extends JpaRepository<ShippingRateCache, Integer> {
//    ShippingRateCache findCachedRate(@Param("senderPostcode") String senderPostcode, @Param("receiverPostcode") String receiverPostcode, @Param("weight") double weight, @Param("length") double length, @Param("width") double width, @Param("height") double height);
//}