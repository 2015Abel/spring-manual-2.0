package com.demo.abel.framework.mvc;

import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description: 封装原HttpServletRequest、HttpServletResponse
 * @author: liuzijian
 * @date: 2018-11-22 11:17
 */
@Getter
@Setter
public class WebServletRequest {
    private HttpServletRequest request;
    private HttpServletResponse response;

    public WebServletRequest(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
}
