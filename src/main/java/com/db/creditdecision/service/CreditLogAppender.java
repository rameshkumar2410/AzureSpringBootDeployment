package com.db.creditdecision.service;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.stereotype.Service;

@Service
public class CreditLogAppender {
	
	public Logger getLogAppeneder() {
		return Configurator.initialize("test", "config-webapps.xml")
				.getLogger(CreditLogAppender.class.getName());
	}

}
