package com.tvd12.ezydata.jpa.test.result;

import javax.persistence.NamedQuery;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
