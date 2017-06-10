/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newt;

import com.newt.model.EmployeeDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author shyams
 */
@RestController
public class ParamPassingController {

    @RequestMapping(
            value = "/ex/foos",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    @ResponseBody
    public EmployeeDetails getFoosAsJsonFromREST() {
        return new EmployeeDetails();
    }
    
    @RequestMapping(value = "/ex/foos/{fooid}/bar/{barid}", method = RequestMethod.GET)
    @ResponseBody
    public String getFoosBySimplePathWithPathVariables(@PathVariable("fooid") long fooid, @PathVariable("barid") long barid) {
        return "Get a specific Bar with id=" + barid
                + " from a Foo with id=" + fooid;
    }
    
    
    @RequestMapping(
            value = "/ex/bars",
            params = {"id", "second"},
            method = RequestMethod.GET)
    @ResponseBody
    public String getBarBySimplePathWithExplicitRequestParams(
            @RequestParam("id") long id) {
        return "Narrow Get a specific Bar with id=" + id;
    }
}
