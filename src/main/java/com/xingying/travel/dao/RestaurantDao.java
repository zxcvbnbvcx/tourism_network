package com.xingying.travel.dao;

import com.xingying.travel.pojo.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RestaurantDao extends JpaRepository<Restaurant,String>, JpaSpecificationExecutor<Restaurant> {

    List<Restaurant> findByCityLike(String name);
}
