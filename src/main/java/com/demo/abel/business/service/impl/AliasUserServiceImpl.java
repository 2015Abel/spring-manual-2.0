package com.demo.abel.business.service.impl;

import com.alibaba.fastjson.JSON;
import com.demo.abel.business.dao.BookRepository;
import com.demo.abel.business.domain.BookEntity;
import com.demo.abel.business.service.UserService;
import com.demo.abel.framework.annotation.Autowire;
import com.demo.abel.framework.annotation.auto.clz.Service;

import java.util.Random;

/**
 * @description: AliasUserServiceImpl
 * @author: liuzijian
 * @create: 2018-11-15 10:19
 **/
@Service(name = "aliasUserService")
public class AliasUserServiceImpl implements UserService {

    @Autowire
    private BookRepository bookRepository;

    @Override
    public boolean readABook() {
        long bookId = new Random().nextInt(3);
        BookEntity bookEntity = bookRepository.queryById(bookId);
        if(bookEntity!=null){
            System.out.println("开始读书："+JSON.toJSONString(bookEntity));
        }else {
            System.out.println("i hava not book : id="+bookId);
        }
        return bookEntity!=null;
    }
}
