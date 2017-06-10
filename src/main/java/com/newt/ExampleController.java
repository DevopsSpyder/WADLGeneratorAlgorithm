/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newt;

//import com.netflix.discovery.DiscoveryClient;
import com.newt.model.EmployeeDetails;
import io.swagger.annotations.ApiOperation;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author shyams
 */
@RestController
@EnableSwagger2
@RequestMapping("/first")
public class ExampleController {

    @Autowired
    private DiscoveryClient discoveryClient;
    
//    public static void main(String[] args) throws MalformedURLException, UnknownHostException {
//        new ExampleController().helloWorld();
//    }

    @RequestMapping(value = "/{fooid}", method = RequestMethod.GET)
    @ApiOperation(value = "SwaggerAttempt")
    public List<ServiceInstance> helloWorld(@PathVariable("fooid") String fooid) throws MalformedURLException, UnknownHostException {
        List<String> listOfUri = new ArrayList<String>();
        for (String serviceName : this.discoveryClient.getServices()) {
            List<ServiceInstance> serviceInstanceList = this.discoveryClient.getInstances(serviceName);
            for (ServiceInstance serviceInstance : serviceInstanceList) {
                InetAddress address = InetAddress.getByName(new URL(serviceInstance.getUri().toString()).getHost());

                StringBuilder str;
                if (address.isSiteLocalAddress()) {
                    str = new StringBuilder();
                    str.append(serviceInstance.isSecure() ? "https://" : "http://");
                    str.append("localhost:");
                    str.append(serviceInstance.getPort());
                    str.append("/");
                    listOfUri.add(str.toString());
                } else {
                    listOfUri.add(serviceInstance.getUri().toString());
                }
            }
        }
        System.out.println("listOfUri--->"+listOfUri);
        System.out.println("listOfUri--->"+listOfUri);
        return this.discoveryClient.getInstances("save-functionality");
    }

    @RequestMapping("/searchFunctionality")
    @ApiOperation(value = "SwaggerAttempt Search Functionality")
    public EmployeeDetails searchFunctionality() {
//        return "Search Functionality 2";
        return new EmployeeDetails();
    }
}
