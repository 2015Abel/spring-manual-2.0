package com.demo.abel.business.dao;

import com.demo.abel.business.domain.BookEntity;
import com.demo.abel.framework.annotation.auto.clz.Repository;

import java.util.Random;

/**
 * @description: BookRepository
 * @author: liuzijian
 * @create: 2018-11-15 10:23
 **/
@Repository
public class BookRepository {

    public BookEntity queryById(long id){
        BookEntity entity = null;
        if(id == 1){
            entity = new BookEntity();
            entity.setId(1L);
            entity.setName("XXBook");
            entity.setAuthor("able");
            entity.setPages(new Random().nextInt(5000));
        }

        return entity;
    }
}
