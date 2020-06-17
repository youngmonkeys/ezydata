package com.tvd12.ezydata.example.jpa.result;

import javax.persistence.NamedQuery;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NamedQuery(
		name = "FindListOfUserEmailFullName",
		query = "select e.email, e.fullName from User e"
)
public class UserEmailFullNameResult {

	protected String email;
	protected String fullName;
	
}
