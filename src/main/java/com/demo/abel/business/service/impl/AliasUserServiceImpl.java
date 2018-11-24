package com.demo.abel.business.service.impl;

import com.demo.abel.business.dao.BookRepository;
import com.demo.abel.business.domain.BookEntity;
import com.demo.abel.business.service.UserService;
import com.demo.abel.framework.annotation.Autowire;
import com.demo.abel.framework.annotation.auto.clz.Service;

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
    public String speakBookName(Long bookId) {
        String bookName;
        BookEntity bookEntity = bookRepository.queryById(bookId);
        if(bookEntity!=null){
            bookName =  bookEntity.getName();
        }else {
            bookName = null;
        }
        return bookName;
    }
}
