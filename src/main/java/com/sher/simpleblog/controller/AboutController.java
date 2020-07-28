package com.sher.simpleblog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Title AboutController
 * @Package com.sher.simpleblog.controller
 * @Description handle request /about
 * @Author sher
 * @Date 2020/07/27 2:14 PM
 */
@Controller
@Slf4j
@RequestMapping("/about")
public class AboutController {

    @GetMapping
    public String about() {

        return "about";
    }
}
