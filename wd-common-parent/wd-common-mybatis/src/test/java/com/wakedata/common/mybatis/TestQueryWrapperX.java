package com.wakedata.common.mybatis;

import com.alibaba.fastjson.JSONObject;
import com.wakedata.common.mybatis.mapper.BargainActivityMapper;
import com.wakedata.common.mybatis.model.BargainActivityDO;
import com.wakedata.common.mybatis.plus.query.QueryWrapperX;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Desc QueryWrapperX单元测试
 * @Author zkz
 * @Date 2022/1/17
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication
@MapperScan("com.wakedata.common.mybatis.mapper")
public class TestQueryWrapperX {

    @Resource
    private BargainActivityMapper bargainActivityMapper;

    @Test
    public void selectList() {
//      SELECT COUNT( * ) FROM mt_bargain_activity
        QueryWrapperX x = new QueryWrapperX();
        Long i = bargainActivityMapper.selectCount(x);
        System.out.println("数量="+i);
        Assert.assertTrue(i>0);
    }

     @Test
    public void testEqIfPresent() {
//      SELECT id,activity_no,tenant_id,app_bu_id,activity_name,suit_store_type,suit_store_ids,sale_limit_type,sale_limit_num,sale_limit_remain,sale_store_limit,sale_price,lowest_price,lowest_price_buy,start_time,end_time,bargain_duration,bargain_people,virtual_people,activity_status,bargain_times_period,bargain_times_day,assist_times_period,assist_times_day,update_time,create_time,create_by,update_by FROM mt_bargain_activity
//      WHERE (activity_name = ?)
        QueryWrapperX x = new QueryWrapperX();
        String val = "砍价活动";
        x.eqIfPresent("activity_name", val);
        x.eqIfPresent("activity_no", null);
        BargainActivityDO bargainActivityDO = bargainActivityMapper.selectOne(x);
        System.out.println(JSONObject.toJSONString(bargainActivityDO));
        Assert.assertTrue(bargainActivityDO.getActivityName().equals(val));
    }
     @Test
    public void testLikeIfPresent() {
//      SELECT id,activity_no,tenant_id,app_bu_id,activity_name,suit_store_type,suit_store_ids,sale_limit_type,sale_limit_num,sale_limit_remain,sale_store_limit,sale_price,lowest_price,lowest_price_buy,start_time,end_time,bargain_duration,bargain_people,virtual_people,activity_status,bargain_times_period,bargain_times_day,assist_times_period,assist_times_day,update_time,create_time,create_by,update_by FROM mt_bargain_activity
//      WHERE (activity_name = ?)
        QueryWrapperX x = new QueryWrapperX();
        String val = "价活";
        x.likeIfPresent("activity_name", val);
        x.likeIfPresent("activity_no", null);
        BargainActivityDO bargainActivityDO = bargainActivityMapper.selectOne(x);
        System.out.println(JSONObject.toJSONString(bargainActivityDO));
        Assert.assertTrue(bargainActivityDO.getActivityName().contains(val));
    }
    @Test
    public void testInIfPresent() {
//      SELECT id,activity_no,tenant_id,app_bu_id,activity_name,suit_store_type,suit_store_ids,sale_limit_type,sale_limit_num,sale_limit_remain,sale_store_limit,sale_price,lowest_price,lowest_price_buy,start_time,end_time,bargain_duration,bargain_people,virtual_people,activity_status,bargain_times_period,bargain_times_day,assist_times_period,assist_times_day,update_time,create_time,create_by,update_by FROM mt_bargain_activity
//      WHERE (activity_name = ?)
        QueryWrapperX x = new QueryWrapperX();
        String val = "砍价活动";
        x.inIfPresent("activity_name", val);
        x.inIfPresent("activity_no", new ArrayList<>());
        BargainActivityDO bargainActivityDO = bargainActivityMapper.selectOne(x);
        System.out.println(JSONObject.toJSONString(bargainActivityDO));
        Assert.assertTrue(bargainActivityDO.getActivityName().contains(val));
    }

     @Test
    public void testEqIfPresentLambda() {
//      SELECT id,activity_no,tenant_id,app_bu_id,activity_name,suit_store_type,suit_store_ids,sale_limit_type,sale_limit_num,sale_limit_remain,sale_store_limit,sale_price,lowest_price,lowest_price_buy,start_time,end_time,bargain_duration,bargain_people,virtual_people,activity_status,bargain_times_period,bargain_times_day,assist_times_period,assist_times_day,update_time,create_time,create_by,update_by FROM mt_bargain_activity
//      WHERE (activity_name = ?)
        QueryWrapperX<BargainActivityDO> x = new QueryWrapperX();
        String val = "砍价活动";
         x.likeIfPresent(BargainActivityDO::getActivityName, val);
         x.likeIfPresent(BargainActivityDO::getActivityNo, null);
        BargainActivityDO bargainActivityDO = bargainActivityMapper.selectOne(x);
        System.out.println(JSONObject.toJSONString(bargainActivityDO));
        Assert.assertTrue(bargainActivityDO.getActivityName().equals(val));
    }
     @Test
    public void testLikeIfPresentLambda() {
//      SELECT id,activity_no,tenant_id,app_bu_id,activity_name,suit_store_type,suit_store_ids,sale_limit_type,sale_limit_num,sale_limit_remain,sale_store_limit,sale_price,lowest_price,lowest_price_buy,start_time,end_time,bargain_duration,bargain_people,virtual_people,activity_status,bargain_times_period,bargain_times_day,assist_times_period,assist_times_day,update_time,create_time,create_by,update_by FROM mt_bargain_activity
//      WHERE (activity_name = ?)
        QueryWrapperX<BargainActivityDO> x = new QueryWrapperX();
        String val = "价活";
        x.likeIfPresent(BargainActivityDO::getActivityName, val);
        x.likeIfPresent(BargainActivityDO::getActivityNo, null);
        BargainActivityDO bargainActivityDO = bargainActivityMapper.selectOne(x);
        System.out.println(JSONObject.toJSONString(bargainActivityDO));
        Assert.assertTrue(bargainActivityDO.getActivityName().contains(val));
    }
    @Test
    public void testInIfPresentLambda() {
//      SELECT id,activity_no,tenant_id,app_bu_id,activity_name,suit_store_type,suit_store_ids,sale_limit_type,sale_limit_num,sale_limit_remain,sale_store_limit,sale_price,lowest_price,lowest_price_buy,start_time,end_time,bargain_duration,bargain_people,virtual_people,activity_status,bargain_times_period,bargain_times_day,assist_times_period,assist_times_day,update_time,create_time,create_by,update_by FROM mt_bargain_activity
//      WHERE (activity_name = ?)
        QueryWrapperX<BargainActivityDO> x = new QueryWrapperX();
        String val = "砍价活动";
        List<String> objects = new ArrayList<>();
        objects.add(val);
        x.inIfPresent(BargainActivityDO::getActivityName, objects);
        x.inIfPresent(BargainActivityDO::getActivityNo, new ArrayList<>());
        BargainActivityDO bargainActivityDO = bargainActivityMapper.selectOne(x);
        System.out.println(JSONObject.toJSONString(bargainActivityDO));
        Assert.assertTrue(bargainActivityDO.getActivityName().contains(val));
    }

    /**
     * lambda和字段名组合测试
     */
    @Test
    public void testLambdaCombination() {
//      SELECT id,activity_no,tenant_id,app_bu_id,activity_name,suit_store_type,suit_store_ids,sale_limit_type,sale_limit_num,sale_limit_remain,sale_store_limit,sale_price,lowest_price,lowest_price_buy,start_time,end_time,bargain_duration,bargain_people,virtual_people,activity_status,bargain_times_period,bargain_times_day,assist_times_period,assist_times_day,update_time,create_time,create_by,update_by FROM mt_bargain_activity
//      WHERE (activity_name IN (?) AND activity_no = ? AND id = ?)
        QueryWrapperX<BargainActivityDO> x = new QueryWrapperX();
        String val = "砍价活动";
        List<String> objects = new ArrayList<>();
        objects.add(val);
        x.inIfPresent("activity_name", objects);
        x.eqIfPresent(BargainActivityDO::getActivityNo, 4334);
        x.eq("id", 1);

        BargainActivityDO bargainActivityDO = bargainActivityMapper.selectOne(x);
        System.out.println(JSONObject.toJSONString(bargainActivityDO));
        Assert.assertTrue(bargainActivityDO.getActivityName().contains(val));
    }


    /**
     * 占位符测试
     */
    @Test
    public void testPlaceholder() {
//      SELECT id,activity_no,tenant_id,app_bu_id,activity_name,suit_store_type,suit_store_ids,sale_limit_type,sale_limit_num,sale_limit_remain,sale_store_limit,sale_price,lowest_price,lowest_price_buy,start_time,end_time,bargain_duration,bargain_people,virtual_people,activity_status,bargain_times_period,bargain_times_day,assist_times_period,assist_times_day,update_time,create_time,create_by,update_by FROM mt_bargain_activity
//      WHERE (activity_name IN (?) AND activity_no = ? AND DATE_FORMAT(start_time, '%Y-%m-%d') <= ?)
        QueryWrapperX<BargainActivityDO> x = new QueryWrapperX();
        String val = "砍价活动";
        List<String> objects = new ArrayList<>();
        objects.add(val);
        x.inIfPresent("activity_name", objects);
        x.eqIfPresent(BargainActivityDO::getActivityNo, 4334);
        x.le("DATE_FORMAT(?, '%Y-%m-%d')", BargainActivityDO::getStartTime, "2022-01-18");

        BargainActivityDO bargainActivityDO = bargainActivityMapper.selectOne(x);
        System.out.println(JSONObject.toJSONString(bargainActivityDO));
        Assert.assertTrue(bargainActivityDO.getActivityName().contains(val));
    }


}
