/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newt.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

/**
 *
 * @author shyams
 */
public class MySchemaOutputResolver extends SchemaOutputResolver {
    
    private String namespaceURI;

//    @Override
    public Result createOutput(String namespaceURI, String suggestedFileName) throws IOException {
        System.out.println("namespaceURI--->"+namespaceURI);
        System.out.println("suggestedFileName--->"+suggestedFileName);
        this.namespaceURI=namespaceURI;
        File file = new File(suggestedFileName);
        StreamResult result = new StreamResult(file);
        result.setSystemId(file.toURI().toURL().toString());
        System.out.println("result--->"+file.getAbsolutePath());
        System.out.println("result--->"+file.getCanonicalPath());
        asString(file);
        return result;
    }
    
    public String setNameSpaceUri(){
        return this.namespaceURI;
    }
    
    public void asString(File file){
        try {
    Scanner scanner = new Scanner(file);

    //now read the file line by line...
    int lineNum = 0;
    while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        System.out.println("line-->"+line);
    }
} catch(FileNotFoundException e) { 
    //handle this
}
    }

}
