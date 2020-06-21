package com.tvd12.ezydata.example.jpa.test;

import com.tvd12.ezydata.example.jpa.entity.User;
import com.tvd12.ezyfox.tool.EzySQLTableCreator;
import com.tvd12.ezyfox.tool.data.EzyCaseType;

public class TableScriptGenerator {
	
	public static void main(String[] args) {
		EzySQLTableCreator tableCreator = 
				new EzySQLTableCreator(User.class, EzyCaseType.NORMAL);
		System.out.print(tableCreator.createScriptToFolder("scripts"));
	}	

}
