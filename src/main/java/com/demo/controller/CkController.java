package com.demo.controller;

import com.demo.domain.entity.User;
import com.demo.domain.service.CkService;
import com.demo.infrastructure.util.page.PageResult;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@RestController
@RequestMapping(value = "/ckDmlApi")
public class CkController {

    private final CkService ckService;

    public CkController(CkService ckService) {
        this.ckService = ckService;
    }


    @RequestMapping(value = "/testMsg",method = RequestMethod.GET)
    public String testMsg() throws UnknownHostException {
        System.out.println("host docker ip : " + InetAddress.getLocalHost().getHostAddress());
        return "hello 2023 world!";
    }
    @RequestMapping(value = "/getUserInfoById",method = RequestMethod.GET)
    public List<User> getUserInfoById(String id) {
        List<User> userList= ckService.findById(Long.valueOf(id));
        return userList;
    }

    @RequestMapping(value = "/getUserInfoLimitPage",method=RequestMethod.POST)
    public PageResult<User> getUserInfoLimitPage(@RequestBody User user){
        Integer page=1;
        Integer limit=3;
        PageResult<User> userList = ckService.page(user, page, limit);
        return userList;
    }

    @PostMapping("insertUserInfo")
    public String insertUserInfo(@RequestBody User user){
        ckService.create(user);
        return "insert successful";
    }

    @GetMapping("deleteUserById")
    public String deleteUserById(String id){
        ckService.delete(Long.valueOf(id));
        return "delete successful";
    }

    @RequestMapping(value = "/updateUserInfo",method=RequestMethod.POST)
    public String updateUserInfo(@RequestBody User user){
        if(ckService.update(user)){
            return "update successful";
        }else {
            return "update fail";
        }
    }

}
