package org.apache.dolphinscheduler.graphql.controller;

import org.apache.dolphinscheduler.graphql.service.AlertGraoupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


public class TestDaoController {
    @Autowired
    private AlertGraoupService alertGraoupService;

    @GetMapping("/hhh")
    public String get() {


        return "true";
    }
}
