/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hortonworks.amuise.cdrstorm.storm.spouts;

import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;
import com.hortonworks.amuise.cdrstorm.storm.bolts.LoggingBolt;
import com.hortonworks.amuise.cdrstorm.storm.utils.CDRStormContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author adammuise
 */
public class CDRScheme implements Scheme {

    Properties globalconfigs;
    private static final Logger logger = Logger.getLogger(CDRScheme.class);

    public CDRScheme() {
        CDRStormContext ctx = new CDRStormContext();
        this.globalconfigs = ctx.config;
    }

    @Override
    public List<Object> deserialize(byte[] bytes) {

        List<Object> cdrvals = new ArrayList<Object>();
        logger.debug("CDRScheme Bytes.toString: " + bytes.toString());
        
        try {
            String payload = new String(bytes, "UTF-8");
            logger.debug("CDRScheme payload (from Stirng constructor): " + payload);
            String[] items = payload.split("\\|");

            for (String item : items) {
                cdrvals.add(item);
            }

        } catch (Exception e) {
            return cdrvals;
        }

        return cdrvals;
    }

    @Override
    public Fields getOutputFields() {
        List<String> predefinedCDRScheme = new ArrayList<String>();
        String[] cdrFields = globalconfigs.getProperty("cdr.schema").split(",");
        for (String field : cdrFields) {
            predefinedCDRScheme.add(field);
        }

        Fields cdrSchemeFields = new Fields(predefinedCDRScheme);

        return cdrSchemeFields;
    }

}
