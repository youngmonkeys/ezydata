package com.tvd12.ezydata.jpa.test.result;

import javax.persistence.NamedNativeQuery;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NamedNativeQuery(
        name = "FindListOfUserEmailFullName2",
        query = "select e.email, e.fullName from User e"
)
public class UserEmailFullNameResult2 {

    protected String email;
    protected String fullName;

}
