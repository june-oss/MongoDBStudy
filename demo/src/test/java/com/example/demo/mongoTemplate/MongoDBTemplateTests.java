package com.example.demo.mongoTemplate;

import com.example.demo.accessingdatamongodb.Customer;
import com.example.demo.DemoApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class MongoDBTemplateTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void test(){
        Customer cust1 = new Customer("son", "hyeonjung");
        Customer cust2 = new Customer("kim", "hyeonjung");

        //insert
        mongoTemplate.insertAll( Arrays.asList(cust1, cust2));

        //get
        List<Customer> result = mongoTemplate.find(Query.query(Criteria.where(Customer.FIRST_NAME).is("son")),Customer.class);

        System.out.println(result);

    }


}
