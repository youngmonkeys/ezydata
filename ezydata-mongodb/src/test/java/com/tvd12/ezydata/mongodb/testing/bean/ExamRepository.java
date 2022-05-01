package com.tvd12.ezydata.mongodb.testing.bean;

import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezyfox.database.annotation.EzyRepository;
import org.bson.types.ObjectId;

@EzyRepository
public interface ExamRepository extends EzyMongoRepository<ObjectId, Exam> {
}
