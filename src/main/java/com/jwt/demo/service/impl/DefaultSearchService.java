package com.jwt.demo.service.impl;

import com.jwt.demo.service.SearchService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "searchService")
public class DefaultSearchService implements SearchService {

    @Autowired
    private SolrClient solrClient;

    @Override
    public void query() {
        try {
            SolrQuery params = new SolrQuery();
            //查询条件
            params.set("q", "*:*");
            //这里的分页和mysql分页一样
            params.set("wt", "json");
            QueryResponse query = solrClient.query(params);
            //查询结果
            SolrDocumentList results = query.getResults();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
