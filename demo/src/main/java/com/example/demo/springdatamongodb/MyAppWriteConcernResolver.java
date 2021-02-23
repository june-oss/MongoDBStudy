package com.example.demo.springdatamongodb;

import com.mongodb.WriteConcern;
import com.mongodb.WriteConcernResult;
import org.springframework.data.mongodb.core.MongoAction;
import org.springframework.data.mongodb.core.WriteConcernResolver;

public class MyAppWriteConcernResolver implements WriteConcernResolver {

    @Override
    public WriteConcern resolve(MongoAction action) {

        if(action.getClass().getSimpleName().contains("Audit")){
            return WriteConcern.ACKNOWLEDGED;
        } else if (action.getClass().getSimpleName().contains("Metadata")){
            return WriteConcern.JOURNALED;
        }
        return action.getDefaultWriteConcern();
    }
}
