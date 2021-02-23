package com.example.demo.accessingdatamongodb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class CustomerRepositoryTests {

    @Autowired
    private CustomerRepository repository;

    @Test
    public void createTest(){

//        repository.save(new Customer("lee", "hyeon"));
//        repository.save(new Customer("kim", "hyeon"));

        System.out.println("Customers found with findAll():");
        System.out.println("-------------------------------");
        for (Customer customer : repository.findAll()) {
            System.out.println(customer);
        }
        System.out.println();

        // fetch an individual customer
        System.out.println("Customer found with findByFirstName('lee'):");
        System.out.println("--------------------------------");
        System.out.println(repository.findByFirstName("lee"));

        System.out.println("Customers found with findByLastName('hyeon'):");
        System.out.println("--------------------------------");
        for (Customer customer : repository.findByLastName("hyeon")) {
            System.out.println(customer);
        }

    }

    @Test
    public void simple(){
        System.out.println("hello");
    }
}
