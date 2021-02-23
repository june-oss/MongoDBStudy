package com.example.demo.mongoTemplate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
public class PetTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void insertTest() {
        PetEntity pet = new PetEntity();
        pet.setKind("CAT");
        pet.setName("나비");
        pet.setAge(2);


        mongoTemplate.insert(pet);

//        Criteria criteria = new Criteria("name");
//        criteria.is("나비");
//        Query query = new Query(criteria);
//        Query query = new Query(Criteria.where("name").is("나비"));
        Query query = new Query(Criteria.where("_id").is(pet.getId()));

        PetEntity findpet = mongoTemplate.findOne(query, PetEntity.class, "pets");

        then(pet.getId()).isEqualTo(findpet.getId());
        then(pet.getKind()).isEqualTo(findpet.getKind());
        then(pet.getName()).isEqualTo(findpet.getName());
        then(pet.getAge()).isEqualTo(findpet.getAge());

    }

    @Test
    public void insertTest2(){

        IntStream.rangeClosed(1,50).forEach(i->{
            PetEntity pet = new PetEntity();
            pet.setKind("DOG");
            pet.setName("Test Name......."+i);
            pet.setAge(i);

            mongoTemplate.insert(pet);
        });
    }

    @Test
    public void updateTest(){
        int targetAge = 2;

        Query query = new Query(Criteria.where("age").is(targetAge));

        PetEntity pet = mongoTemplate.findOne(query, PetEntity.class);

        Update update = Update.update("name", "Doggy").inc("age",10);

        mongoTemplate.updateFirst(query, update, PetEntity.class);

        PetEntity updataPet = mongoTemplate.findOne(Query.query(Criteria.where("id").is(pet.getId())),PetEntity.class);

        then(updataPet.getName()).isEqualTo("Doggy");

    }

    @Test
    public void updateMultiTest(){

        Query query = new Query(Criteria.where("age").is(2));

        Update update = Update.update("name", "Multi").inc("age",10);

        mongoTemplate.updateMulti(query, update, PetEntity.class);

    }

    @Test
    public void upsertTest(){
        //upsert??? 있으면 바꾸고 없으면 추가하고??
        PetEntity pet = new PetEntity();
        pet.setKind("DOG");
        pet.setName("Upsert Test ");
        pet.setAge(4);

        Query query = new Query(Criteria.where("age").is(15));

        mongoTemplate.upsert(query, Update.update("name","upsert").inc("age",12),PetEntity.class);

    }

    @Test
    public void removeTests(){
        List<PetEntity> pets = mongoTemplate.findAll(PetEntity.class);

        for(PetEntity pet : pets){
            mongoTemplate.remove(pet);
        }
    }

    @Test
    public void insertTests(){
        PetEntity pet = new PetEntity();

        List<PetEntity> siblings = new ArrayList<>();

        IntStream.rangeClosed(1,10).forEach(i-> {
            PetEntity sibling = new PetEntity();
            sibling.setKind("DOG");
            sibling.setName("sibling......."+i);
            sibling.setAge(i);

            siblings.add(sibling);
        });
        pet.setKind("DOG");
        pet.setName("Upsert Test ");
        pet.setAge(451);
        pet.setSibling(siblings);

        mongoTemplate.insert(pet);
    }

    @Test
    public void insertObjectTests(){

        PetEntity pet = new PetEntity();

        pet.setKind("DOG");

        pet.setName("Upsert Test ");
        pet.setAge(21);

        PocketEntity pocket = new PocketEntity();
        pocket.setQty(10);
        pocket.setStatus("E");

        pet.setPocket(pocket);

        mongoTemplate.save(pet);


    }


}
