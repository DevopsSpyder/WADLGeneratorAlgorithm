/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newt.model;

import java.io.IOException;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;

/**
 *
 * @author shyams
 */
public class VarudhoIllayo {
    public static void main(String[] args) throws JAXBException, IOException  {
        VarudhoIllayo varudhoIllayo=new VarudhoIllayo();
        varudhoIllayo.illaya(EmployeeDetails.class);
        
    }
    
    public void illaya(Class returnType) throws JAXBException, IOException {
        JAXBContext jaxbContext = JAXBContext.newInstance(returnType);
        SchemaOutputResolver sor = new MySchemaOutputResolver();
//        sor.createOutput("http://www.example.org/EmployeeDetails","employee.xsd");
        jaxbContext.generateSchema(sor);
//        System.out.println("asString--->"+asSCtring(jaxbContext,null));
    }
    
//    public String asString(JAXBContext pContext, 
//                        Object pObject)
//                            throws 
//                                JAXBException {
//
//    java.io.StringWriter sw = new StringWriter();
//
//    Marshaller marshaller = pContext.createMarshaller();
//    marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
//    marshaller.marshal(new EmployeeDetails(), sw);
//
//    return sw.toString();
//}
}
