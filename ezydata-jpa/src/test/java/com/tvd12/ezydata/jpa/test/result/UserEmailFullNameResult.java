package com.tvd12.ezydata.jpa.test.result;

import lombok.*;

import javax.persistence.NamedQuery;

@Getter
@Setter
@ToString
@NamedQuery(
    name = "FindListOfUserEmailFullName",
    query = "select e.email, e.fullName from User e"
)
@NoArgsConstructor
@AllArgsConstructor
public class UserEmailFullNameResult {
    protected String email;
    protected String fullName;
}
