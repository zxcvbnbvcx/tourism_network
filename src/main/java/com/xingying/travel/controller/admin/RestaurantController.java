package com.xingying.travel.controller.admin;

import com.xingying.travel.common.PageResult;
import com.xingying.travel.common.Result;
import com.xingying.travel.common.StatusCode;
import com.xingying.travel.pojo.Restaurant;
import com.xingying.travel.service.RestaurantService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@CrossOrigin
@RequestMapping("/restaurant")
@Api(value = "Restaurant", tags = "Restaurant", description = "restaurantTest")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;


    /**
     * 查询全部数据
     * @return
     */
    @ResponseBody
    @RequestMapping(method= RequestMethod.GET)
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",restaurantService.findAll());
    }

    /**
     * 根据ID查询
     * @param id ID
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/{id}",method= RequestMethod.GET)
    public Result findById(@PathVariable String id){
        return new Result(true,StatusCode.OK,"查询成功",restaurantService.findById(id));
    }


    /**
     * 分页+多条件查询
     * @param searchMap 查询条件封装
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @ResponseBody
    @RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
        Page<Restaurant> pageList = restaurantService.findSearch(searchMap, page, size);
        return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Restaurant>(pageList.getTotalElements(), pageList.getContent()) );
    }


    /**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",restaurantService.findSearch(searchMap));
    }

    /**
     * 增加
     * @param restaurant
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.POST)
    public Result add(@RequestBody Restaurant restaurant){
        restaurantService.add(restaurant);
        return new Result(true,StatusCode.OK,"增加成功");
    }

    /**
     * 修改
     * @param restaurant
     */
    @ResponseBody
    @RequestMapping(value="/{id}",method= RequestMethod.PUT)
    public Result update(@RequestBody Restaurant restaurant, @PathVariable String id ){
        restaurant.setId(id);
        restaurantService.update(restaurant);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 删除
     * @param id
     */
    @ResponseBody
    @RequestMapping(value="/{id}",method= RequestMethod.DELETE)
    public Result delete(@PathVariable String id ){
        restaurantService.deleteById(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    @RequestMapping(value = "/restaurantList")
    public String restaurantList(){
        return "admin/restaurantmanage/restaurantList";
    }

    @RequestMapping(value = "/restaurantAdd")
    public String restaurantAdd(){
        return "admin/restaurantmanage/restaurantAdd";
    }


}
