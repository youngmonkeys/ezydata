package com.tvd12.ezydata.database.naming;

import static com.tvd12.ezydata.database.naming.EzyNamingCase.CAMEL;
import static com.tvd12.ezydata.database.naming.EzyNamingCase.DASH;
import static com.tvd12.ezydata.database.naming.EzyNamingCase.DOT;
import static com.tvd12.ezydata.database.naming.EzyNamingCase.LOWER;
import static com.tvd12.ezydata.database.naming.EzyNamingCase.UNDERSCORE;
import static com.tvd12.ezydata.database.naming.EzyNamingCase.UPPER;
import static com.tvd12.properties.file.util.PropertiesUtil.getPropertyNameInDotCase;

import com.tvd12.ezyfox.builder.EzyBuilder;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class EzySimpleNameTranslator implements EzyNameTranslator {

	@Getter
	protected final EzyNamingCase namingCase;
	protected final String ignoredSuffix;
	
	@Override
	public String translate(String originalName) {
		String startName = originalName;
		if(originalName.endsWith(ignoredSuffix)) {
			if(originalName.length() > ignoredSuffix.length()) {
				startName = originalName
					.substring(0, originalName.length() - ignoredSuffix.length());
			}
		}
		if(namingCase == UPPER)
			return startName.toUpperCase();
		if(namingCase == LOWER)
			return startName.toLowerCase();
		if(namingCase == CAMEL) {
			if(startName.length() < 2)
				return startName.toLowerCase();
			return startName.substring(0, 1).toLowerCase() + startName.substring(1);
		}
		String dotCaseName = getPropertyNameInDotCase(startName)
				.replace(DASH.getSign(), DOT.getSign())
				.replace(UNDERSCORE.getSign(), DOT.getSign())
				.replace(DOT.getSign() + DOT.getSign(), DOT.getSign());
		if(namingCase == DOT)
			return dotCaseName;
		if(namingCase == DASH)
			return dotCaseName.replace(DOT.getSign(), DASH.getSign());
		if(namingCase == UNDERSCORE)
			return dotCaseName.replace(DOT.getSign(), UNDERSCORE.getSign());
		return startName;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzySimpleNameTranslator> {
		protected EzyNamingCase namingCase = EzyNamingCase.NATURE;
		protected String ignoredSuffix = "";
		
		public Builder namingCase(EzyNamingCase namingCase) {
			if(namingCase != null)
				this.namingCase = namingCase;
			return this;
		}
		
		public Builder ignoredSuffix(String ignoredSuffix) {
			if(ignoredSuffix != null)
				this.ignoredSuffix = ignoredSuffix;
			return this;
		}
		
		@Override
		public EzySimpleNameTranslator build() {
			return new EzySimpleNameTranslator(namingCase, ignoredSuffix);
		}
	}
	
}
