package com.tvd12.ezydata.mongodb.testing.bean;

import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.database.annotation.EzyCollection;
import lombok.Data;
import org.bson.BsonObjectId;
import org.bson.types.ObjectId;

import java.util.List;

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
