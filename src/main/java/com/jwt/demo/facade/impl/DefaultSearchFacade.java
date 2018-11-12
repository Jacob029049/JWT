package com.jwt.demo.facade.impl;

import com.jwt.demo.facade.SearchFacade;
import com.jwt.demo.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "searchFacade")
public class DefaultSearchFacade implements SearchFacade {

    @Autowired
    private SearchService searchService;
    @Override
    public void query() {
        searchService.query();
    }
}
