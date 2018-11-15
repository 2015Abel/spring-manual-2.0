package com.demo.abel.business.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @description: TODO 类描述
 * @author: liuzijian
 * @create: 2018-11-15 10:24
 **/
@Setter
@Getter
public class BookEntity extends AbstractEntity {

    private String name;
    private String author;
    private Integer pages;

}
