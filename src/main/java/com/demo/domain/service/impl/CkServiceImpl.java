package com.demo.domain.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.entity.User;
import com.demo.domain.mapper.UserMapper;
import com.demo.domain.service.CkService;
import com.demo.infrastructure.util.page.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CkServiceImpl extends ServiceImpl<UserMapper, User> implements CkService {

    @Autowired
    UserMapper userMapper;

    @Override
    public List<User> findById(long uid) {
        return userMapper.findById(uid);
    }

    @Override
    public PageResult<User> page(User user, Integer current, Integer limit) {
        Page<User> page = new Page<>(current, limit);
        Page<User> res = userMapper.page(page, user);
        return new PageResult(res);
    }

    @Override
    public boolean create(User user) {
        return this.save(user);
    }

    @Override
    public boolean update(User user) {
        return userMapper.updateByIdClickHouse(user);
    }

    @Override
    public void delete(Long id) {
        userMapper.deleteByIdClickHouse(id);
    }
}
