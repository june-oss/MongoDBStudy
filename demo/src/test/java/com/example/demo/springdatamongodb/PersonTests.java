package com.example.demo.springdatamongodb;

import com.example.demo.accessingdatamongodb.Customer;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.log4j.Log4j2;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.assertj.core.api.BDDAssertions.then;
@SpringBootTest
public class PersonTests {

    @Autowired
    MongoTemplate mongoTemplate;

    MongoOperations mongoOps = new MongoTemplate(MongoClients.create(),"database");

    Log log = LogFactory.getLog(PersonTests.class);
    @Test
    public void connectionTest(){

        mongoOps.insert(new Person("Lee", 19));

        log.info("----------------------------------------------");
        log.info(mongoOps.findOne(new Query(where("name").is("Lee")),Person.class));
        log.info("----------------------------------------------");

        mongoOps.dropCollection("person");


    }

    @Test
    public void crudTests(){
        Person p = new Person("Kim", 34);

        log.info("----------------------------------------------");
        mongoOps.insert(p);
        log.info("Insert : " + p);

        log.info("----------------------------------------------");
        p = mongoOps.findById(p.getId(), Person.class);
        log.info("Found : " + p);

        log.info("----------------------------------------------");
        mongoOps.updateFirst(Query.query(where("name").is("Kim")), Update.update("age", 28), Person.class);
        p = mongoOps.findOne(Query.query(where("name").is("Kim")), Person.class);
        log.info("Updated : " + p);

        mongoOps.remove(p);

        log.info("----------------------------------------------");
        List<Person> people = mongoOps.findAll(Person.class);
        log.info("Number of people = : " + people.size());

        //mongoOps.dropCollection(Person.class);


    }

    @Test
    public void upsertingTests(){

        mongoTemplate.update(Person.class)
                .matching(Query.query(where("ssn").is(1111).and("firstName").is("Joe").and("Fraizer").is("Update")))
                .apply(Update.update("address", "addrrrrrr"))
                .upsert();
    }

    @Test
    public void upsertTests2(){
        Query query = new Query(Criteria.where("name").is("Harry"));
        Update update = new Update().inc("age", 1);
//        mongoTemplate.insert(new Person("Tom", 21));
//        mongoTemplate.insert(new Person("Dick", 22));
//        mongoTemplate.insert(new Person("Harry", 23));
//
//
//        Person oldValue = mongoTemplate.update(Person.class)
//                .matching(query)
//                .apply(update)
//                .findAndModifyValue(); // return's old person object
//
//        log.info("==============================");
//        log.info(oldValue);
//        then(oldValue.getName()).isEqualTo("Harry");
//        then(oldValue.getAge()).isEqualTo(23);
//
//        Person newValue = mongoTemplate.query(Person.class)
//                .matching(query)
//                .firstValue();
//
//        log.info("==============================");
//        log.info(newValue);
//        then(newValue.getAge()).isEqualTo(24);
//
//        Person newestValue = mongoTemplate.update(Person.class)
//                .matching(query)
//                .apply(update)
//                .withOptions(FindAndModifyOptions.options().returnNew(true)) // Now return the newly updated document when updating
//                .findAndModifyValue();
//
//        log.info("==============================");
//        log.info(newestValue);
//        then(newestValue.getAge()).isEqualTo(25);

        Person upserted = mongoTemplate.update(Person.class)
                .matching(new Query(Criteria.where("name").is("Mary")))
                .apply(update)
                .withOptions(FindAndModifyOptions.options().upsert(true).returnNew(true))
                .findAndModifyValue();

        then(upserted.getName()).isEqualTo("Mary");
        then(upserted.getAge()).isOne();
    }

    @Test
    public void groupTests(){

        GroupOperation groupOperation = Aggregation.group("name").sum("age").as("age").avg("age").as("avg");
//sum , avg, 정규식으로 검
        SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, "age");

        ProjectionOperation projectionOperation = Aggregation.project().andInclude("name","age","avg");

        AggregationResults<HashMap> aggregate = mongoTemplate.aggregate(Aggregation.newAggregation(groupOperation, sortOperation, projectionOperation),Person.class,HashMap.class);

        log.info(aggregate.getMappedResults());
    }


    @Test
    public void bucketTests(){

        MongoOperations mongoOperations = new MongoTemplate(MongoClients.create(), "test");

        ProjectionOperation projectionOperation = Aggregation.project()
                .and("first_name").concat("last_name").as("full_name")
                .and("year_born").as("year_born");


        BucketOperation bucketOperation = Aggregation.bucket("year_born").withBoundaries(1840, 1850, 1860, 1870, 1880)
                .withDefaultBucket("Other").andOutputCount().as("count").andOutput("first_name").push().as("artists");


        AggregationResults<HashMap> aggregate = mongoOperations.aggregate(Aggregation.newAggregation(bucketOperation),"artists",HashMap.class);

        log.info("=================================================");
        log.info(aggregate.getMappedResults());
        log.info("=================================================");



    }

    @Test
    public void regexMathchTests(){

        log.info("=================================================");

        mongoTemplate.find(Query.query(where("firstName").regex("")), Customer.class).forEach(i -> log.info(i));

        log.info("=================================================");
    }
}
