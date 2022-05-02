package com.xingying.travel.service;

import com.xingying.travel.dao.RestaurantDao;
import com.xingying.travel.pojo.Restaurant;
import com.xingying.travel.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class RestaurantService {
    
    @Autowired
    private RestaurantDao restaurantDao;
    
    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;
    
    public List<Restaurant> findAll(){
        return restaurantDao.findAll();
    }

    public Page<Restaurant> findSearch(Map whereMap, int page, int size) {
        Specification<Restaurant> specification = createSpecification(whereMap);
        PageRequest pageRequest =  PageRequest.of(page-1, size);
        return restaurantDao.findAll(specification, pageRequest);
    }



    /**
     * 条件查询
     * @param whereMap
     * @return
     */
    public List<Restaurant> findSearch(Map whereMap) {
        Specification<Restaurant> specification = createSpecification(whereMap);
        return restaurantDao.findAll(specification);
    }



    /**
     * 根据ID查询实体
     * @param id
     * @return
     */
    public Optional<Restaurant> findById(String id) {
        return restaurantDao.findById(id);
    }

    /**
     * 增加
     * @param restaurant
     */
    public void add(Restaurant restaurant) {
        restaurant.setId( idWorker.nextId()+"" );
        //取到缓存中的文件url
        String fileurl = (String) redisTemplate.opsForValue().get("fileurl");

        restaurant.setImg("https://cstravel.oss-cn-beijing.aliyuncs.com/"+fileurl);
        restaurantDao.save(restaurant);
    }

    /**
     * 修改
     * @param restaurant
     */
    public void update(Restaurant restaurant) {
        restaurantDao.save(restaurant);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteById(String id) {
        restaurantDao.deleteById(id);
    }

    /**
     * 动态条件构建
     * @param searchMap
     * @return
     */
    private Specification<Restaurant> createSpecification(Map searchMap) {

        return new Specification<Restaurant>() {

            @Override
            public Predicate toPredicate(Root<Restaurant> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // id
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 景点图片
                if (searchMap.get("img")!=null && !"".equals(searchMap.get("img"))) {
                    predicateList.add(cb.like(root.get("img").as(String.class), "%"+(String)searchMap.get("img")+"%"));
                }
                // 景点名称
                if (searchMap.get("name")!=null && !"".equals(searchMap.get("name"))) {
                    predicateList.add(cb.like(root.get("name").as(String.class), "%"+(String)searchMap.get("name")+"%"));
                }

                return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }


    /**
     * 根据城市进行模糊查询
     * @return
     */
    public List<Restaurant> findByCountryLike(String city){
        return restaurantDao.findByCityLike(city);
    }
    
    
}
