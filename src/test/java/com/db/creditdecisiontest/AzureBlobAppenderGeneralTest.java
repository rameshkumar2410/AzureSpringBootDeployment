package com.db.creditdecisiontest;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AzureBlobAppenderGeneralTest {

    private AzureConfig _azureConfig;

    @BeforeEach
    public void prepare() throws JsonParseException, JsonMappingException, IOException {

        if (StringUtils.isNotEmpty(System.getProperty("azure-pipelines"))) {
            System.out.println("Use properties.");
        } else {
            System.out.println("Use config fie.");
            ObjectMapper mapper = new ObjectMapper();
            try (InputStream is = ClassLoader.getSystemResourceAsStream("azureconfig.json")) {
                _azureConfig = mapper.readValue(is, AzureConfig.class);
                System.setProperty("accountName", _azureConfig.accountName);
                System.setProperty("accountKey", _azureConfig.accountKey);
                System.setProperty("containerName", _azureConfig.containerName);
            }
        }
    }

    @Test
    public void test() {
        Logger logger = Configurator.initialize("test", "config-webapps.xml").getLogger(AzureBlobAppenderGeneralTest.class.getName());
        logger.debug("debug message");
        logger.info("info message");
        logger.warn("warn message");
        logger.error("error message", new IOException("test"));
        assertTrue(true);
    }
}