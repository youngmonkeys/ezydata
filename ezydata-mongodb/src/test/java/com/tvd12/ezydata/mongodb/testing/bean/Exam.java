package com.tvd12.ezydata.mongodb.testing.bean;

import java.util.List;

import org.bson.BsonObjectId;
import org.bson.types.ObjectId;

import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.database.annotation.EzyCollection;

import lombok.Data;

@Data
@EzyCollection
public class Exam {

    @EzyId
    private ObjectId id;
    private ObjectId eventId;
    private String content;
    private List<ObjectId> refIds;
    private BsonObjectId secondId;


}
