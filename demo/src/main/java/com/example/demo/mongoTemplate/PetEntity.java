package com.example.demo.mongoTemplate;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "pets")
public class PetEntity {

    @Id
    private ObjectId id;

    private String kind;

    private String name;

    private int age;

    private List<PetEntity> sibling;

    private PocketEntity pocket;

    public PetEntity() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<PetEntity> getSibling() {
        return sibling;
    }

    public void setSibling(List<PetEntity> sibling) {
        this.sibling = sibling;
    }

    public PocketEntity getPocket() {
        return pocket;
    }

    public void setPocket(PocketEntity pocket) {
        this.pocket = pocket;
    }
}
