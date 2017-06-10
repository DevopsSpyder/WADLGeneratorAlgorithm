/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newt.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author shyams
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "employeedetails", propOrder = {
    "id",
    "name"
}, namespace = "http://www.example.org/employee")
//@XmlRootElement
public class EmployeeDetails {
    
    private String id;
    private String name;
    
//    @XmlAttribute
//    public String getName() {
//        return name;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    @XmlAttribute
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
    
    
    
}
