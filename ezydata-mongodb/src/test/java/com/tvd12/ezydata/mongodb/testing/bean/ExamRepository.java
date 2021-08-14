package com.tvd12.ezydata.mongodb.testing.bean;

import org.bson.types.ObjectId;

import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezyfox.database.annotation.EzyRepository;

@EzyRepository
public interface ExamRepository extends EzyMongoRepository<ObjectId, Exam> {
}
