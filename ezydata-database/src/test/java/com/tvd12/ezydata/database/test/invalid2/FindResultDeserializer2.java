package com.tvd12.ezydata.database.test.invalid2;

import com.tvd12.ezydata.database.annotation.EzyResultDeserialize;
import com.tvd12.ezydata.database.test.bean.Person;

@EzyResultDeserialize(Person.class)
public class FindResultDeserializer2 {
}
