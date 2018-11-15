package com.demo.abel.business.service.impl;

import com.demo.abel.business.service.UserService;
import com.demo.abel.business.service.WorkService;
import com.demo.abel.framework.annotation.auto.clz.Service;

/**
 * @description: UserServiceImpl
 * @author: liuzijian
 * @create: 2018-11-15 10:14
 **/
@Service
public class UserServiceImpl implements UserService,WorkService {

    @Override
    public boolean readABook() {
        return false;
    }
}
