package com.demo.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.domain.entity.User;
import com.demo.infrastructure.util.page.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public interface CkService extends IService<User> {

    /**
     * 主键查询
     *
     * @param uid
     * @return
     */
    List<User> findById(long uid);


    /**
     * 分页查询
     *
     * @param user
     * @param limit
     * @return
     */
    PageResult<User> page(User user, Integer current, Integer limit);

    /**
     * 创建
     *
     * @param user
     * @return
     */
    boolean create(User user);


    /**
     * 创建
     *
     * @param user
     * @return
     */
    boolean update(User user);


    /**
     * 删除
     *
     * @param id
     * @return
     */
    void delete(Long id);

}
