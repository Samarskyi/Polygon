package com.global.training.polygon.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by eugenii.samarskyi on 14.11.2014.
 */

@Root(name = "tr totalforday cf")
public class WorksTime {

    @Attribute(name="td td1")
    String day;

//    @ElementMap(entry="property", key="key", attribute=true, inline=true)
//    private Map<String, String> map;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

}
