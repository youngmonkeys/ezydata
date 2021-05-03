package com.tvd12.ezydata.database.test.bean;

import com.tvd12.ezydata.database.converter.EzyResultDeserializer;
import com.tvd12.ezyfox.database.annotation.EzyResultDeserialized;

@SuppressWarnings("rawtypes")
@EzyResultDeserialized(FindResult.class)
public class FindResultDeserializer implements EzyResultDeserializer {
}
