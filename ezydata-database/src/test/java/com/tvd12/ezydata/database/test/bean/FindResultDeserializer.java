package com.tvd12.ezydata.database.test.bean;

import com.tvd12.ezydata.database.annotation.EzyResultDeserialized;
import com.tvd12.ezydata.database.converter.EzyResultDeserializer;

@SuppressWarnings("rawtypes")
@EzyResultDeserialized(FindResult.class)
public class FindResultDeserializer implements EzyResultDeserializer {
}
