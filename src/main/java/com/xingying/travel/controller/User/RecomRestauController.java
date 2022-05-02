package com.xingying.travel.controller.User;

import com.xingying.travel.dao.RestaurantDao;
import com.xingying.travel.pojo.Restaurant;
import com.xingying.travel.service.RestaurantService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


@Controller
@CrossOrigin
@RequestMapping("/restaurant")
public class RecomRestauController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RestaurantDao restaurantDao;

    @RequestMapping("/AllRest")
    public String all_attrs(Model model){
        List<Restaurant> res=restaurantService.findAll();
        System.out.println("进入查询！"+res.toString());
        model.addAttribute("res",res);
        return "page/restaurant::table_refresh";
    }

    @RequestMapping("/page_restaurant")
    public String page_attrs(Model model,@RequestParam(value = "start" ,defaultValue = "0")Integer start,
                             @RequestParam(value = "limit" ,defaultValue = "6")Integer limit){
        start=start<0?0:start;
        Sort sort=new Sort(Sort.DEFAULT_DIRECTION,"id");
        Pageable pageable= PageRequest.of(start,limit,sort);
        Page<Restaurant> page=restaurantDao.findAll(pageable);
        model.addAttribute("restaurants",page);
        model.addAttribute("number",page.getNumber());
        model.addAttribute("numberOfElements",page.getNumberOfElements());
        model.addAttribute("size",page.getSize());
        model.addAttribute("totalElements",page.getTotalElements());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("first",page.isFirst());
        model.addAttribute("last",page.isLast());
        return "page/restaurant::table_refresh";
    }

    @RequestMapping("/oneRestaurant")
    public String One_attr(Model model,String id){
        System.out.println("id为:"+id);
        Restaurant restaurant=restaurantService.findById(id).get();
        System.out.println(restaurant.toString());
        model.addAttribute("oneAttr",restaurant);
        //return "page/product::table_refresh";
        return "page/restaurant_product.html";
    }

    @RequestMapping("/search_restaurant")
    public String search_attrs(Model model,@RequestParam(value = "start" ,defaultValue = "0")Integer start,
                               @RequestParam(value = "limit" ,defaultValue = "6")Integer limit,
                               @RequestParam String search_key){
        start=start<0?0:start;
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable=PageRequest.of(start,limit,sort);
        Specification specification=new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> scenics=new ArrayList<>();
                if (StringUtils.isNotBlank(search_key)){
                    scenics.add( criteriaBuilder.like(root.get("name"),"%"+search_key+"%"));
                }
                return criteriaBuilder.and(scenics.toArray(new Predicate[scenics.size()]));
            }
        };
        Page<Restaurant> page=restaurantDao.findAll(specification,pageable);

        model.addAttribute("attrs",page);
        model.addAttribute("number",page.getNumber());
        model.addAttribute("numberOfElements",page.getNumberOfElements());
        model.addAttribute("size",page.getSize());
        model.addAttribute("totalElements",page.getTotalElements());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("first",page.isFirst());
        model.addAttribute("last",page.isLast());
        return "page/restaurant";
    }

}
