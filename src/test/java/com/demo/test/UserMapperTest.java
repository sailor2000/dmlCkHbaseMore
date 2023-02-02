package com.demo.test;

import com.demo.App;
import com.demo.domain.entity.User;
import com.demo.domain.service.CkService;
import com.demo.infrastructure.util.page.PageResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class UserMapperTest {

    @Autowired
    CkService ckService;


    @Test
    public void findById_Test() {
        List<User> userInfo = ckService.findById(1);
        System.out.println("查询用户ID=1信息：" + userInfo);
    }

    @Test
    public void page_Test() {
        User user = new User();
        user.setUserName("张三");
        Integer page = 1;
        Integer limit = 2;
        PageResult<User> userList = ckService.page(user, page, limit);
        System.out.println("查询用户信息分页：" + userList);
    }

    @Test
    public void create_Test() {
        User user = new User();
        user.setId(888l);
        user.setUserName("张三");
        user.setPassWord("123");
        user.setPhone("12312222");
        user.setEmail("326427540@qq.com");
        ckService.create(user);
        System.out.println("创建：" + user);
    }

    @Test
    public void update_Test() {
        User user = new User();
        user.setId(888l);
        user.setUserName("小李飞刀");
        user.setPassWord("123");
        user.setPhone("12312222");
        user.setEmail("88888888@qq.com");
        ckService.update(user);
        System.out.println("创建：" + user);
    }


//    @Test
//    public void delete_Test() {
//        ckService.delete(888l);
//        System.out.println("删除：" + 888l);
//    }

}
