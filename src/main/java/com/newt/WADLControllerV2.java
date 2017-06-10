/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newt;
import com.newt.model.MySchemaOutputResolver;
import com.newt.model.VarudhoIllayo;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
 
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Validator;
import javax.xml.namespace.QName;
//import jdk.internal.org.xml.sax.Locator;
 
import org.jvnet.ws.wadl.Application;
import org.jvnet.ws.wadl.Doc;
import org.jvnet.ws.wadl.Grammars;
import org.jvnet.ws.wadl.Include;
import org.jvnet.ws.wadl.Param;
import org.jvnet.ws.wadl.ParamStyle;
import org.jvnet.ws.wadl.Representation;
import org.jvnet.ws.wadl.Request;
import org.jvnet.ws.wadl.Resource;
import org.jvnet.ws.wadl.Resources;
import org.jvnet.ws.wadl.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
  
@Controller
@RequestMapping("/application.wadl")
public class WADLControllerV2 {
    String xs_namespace="http://www.w3.org/2001/XMLSchema" ;
    @Autowired
    private RequestMappingHandlerMapping handlerMapping;
    @Autowired
    private WebApplicationContext webApplicationContext;
 
    @RequestMapping(method=RequestMethod.GET, produces={"application/xml"} ) 
//    @RequestMapping(method=RequestMethod.GET, produces={"application/json"} )
    public @ResponseBody Application generateWadl(HttpServletRequest request) {
//        System.out.println("enters this part");
//        System.out.println("request--->"+request);
        Application result = new Application();
        Grammars value=new Grammars();
//        Include in=new Include();
//        in.setSourceLocation(newLocator);
//value.getInclude().add();
        result.setGrammars(value);
        Doc doc = new Doc();
        doc.setTitle("Spring REST Service WADL");
        result.getDoc().add(doc);
        Resources wadResources = new Resources();
        String url=getBaseUrl(request);
        System.out.println("url---->"+url);
        wadResources.setBase(url);
          
        Map<RequestMappingInfo, HandlerMethod> handletMethods = handlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handletMethods.entrySet()) {
             
              
            HandlerMethod handlerMethod = entry.getValue();
//            System.out.println("handlerMethod--->"+handlerMethod.toString());
             
            Object object = handlerMethod.getBean();
            Object bean = webApplicationContext.getBean(object.toString());
             
            boolean isRestContoller = bean.getClass().isAnnotationPresent(RestController.class);
//            System.out.println("isRestContoller--->"+isRestContoller+"class name"+bean.getClass().getName());
            if(!isRestContoller) {
                continue;
            }
            RequestMappingInfo mappingInfo = entry.getKey();
//              System.out.println("mappingInfo-->"+mappingInfo.toString());
            Set<String> pattern =  mappingInfo.getPatternsCondition().getPatterns();
//            System.out.println("mappingInfo.getMethodsCondition()--->"+mappingInfo.getMethodsCondition());
//            System.out.println("mappingInfo.getMethodsCondition().getMethods()--->"+mappingInfo.getMethodsCondition().getMethods());
            Set<RequestMethod> httpMethods =  mappingInfo.getMethodsCondition().getMethods();
            
            if(httpMethods.isEmpty()){
                httpMethods=new HashSet<RequestMethod>();
                httpMethods.add(RequestMethod.GET);
            }
            ProducesRequestCondition producesRequestCondition = mappingInfo.getProducesCondition();
            Set<MediaType> mediaTypes = producesRequestCondition.getProducibleMediaTypes();
            Resource wadlResource = null; 
            for (RequestMethod httpMethod : httpMethods) {
//                System.out.println("httpMethod-->"+httpMethod.toString());
                org.jvnet.ws.wadl.Method wadlMethod = new org.jvnet.ws.wadl.Method();
      
                for (String uri : pattern) {
                    wadlResource = createOrFind( uri,  wadResources); 
                    wadlResource.setPath(uri);      
                }
                  
                wadlMethod.setName(httpMethod.name());
                Method javaMethod = handlerMethod.getMethod();
                wadlMethod.setId(javaMethod.getName());
                Doc wadlDocMethod = new Doc();
                wadlDocMethod.setTitle(javaMethod.getDeclaringClass().getSimpleName()+"."+javaMethod.getName());
                wadlMethod.getDoc().add(wadlDocMethod);
                  
                // Request
                Request wadlRequest = new Request();
                  
                Annotation[][] annotations = javaMethod.getParameterAnnotations();
                Class<?>[] paramTypes = javaMethod.getParameterTypes();
                int i = 0;
                System.out.println("javaMethod--->"+javaMethod.getName());
//                System.out.println("annotations-->"+annotations);
                for (Annotation[] annotation : annotations) {
                    Class<?> paramType =paramTypes[i];
                    i++;
                    for (Annotation annotation2 : annotation) {
//                        System.out.println("Enters annotation");
                        if (annotation2 instanceof RequestParam ) {
                            RequestParam param2 = (RequestParam)annotation2;
                            Param waldParam = new Param();
                            QName nm = convertJavaToXMLType(paramType);
                            waldParam.setName(param2.value());
                            waldParam.setStyle(ParamStyle.QUERY);
                            waldParam.setRequired(param2.required());
                            String defaultValue = cleanDefault(param2.defaultValue());
                            if ( !defaultValue.equals("") ) {
                                waldParam.setDefault(defaultValue);
                            }
                            waldParam.setType(nm);
                            wadlRequest.getParam().add(waldParam);
                        } else if ( annotation2 instanceof PathVariable ) {
                            PathVariable param2 = (PathVariable)annotation2;
                            QName nm = convertJavaToXMLType(paramType);
                            Param waldParam = new Param();
                            waldParam.setName(param2.value());
                            waldParam.setStyle(ParamStyle.TEMPLATE);
                            waldParam.setRequired(true);
                            wadlRequest.getParam().add(waldParam);
                            waldParam.setType(nm);
                        }
                    }
                }
                if ( ! wadlRequest.getParam().isEmpty() ) {
                    wadlMethod.setRequest(wadlRequest);
                }
                  
                // Response
                if ( !mediaTypes.isEmpty() ) {
//                    System.out.println("enters media type");
                    Response wadlResponse = new Response();
                    Class methodReturn = handlerMethod.getReturnType().getClass();
//                    Include include=new Include();
//                    include.setSourceLocation(methodReturn.get);
//Locator newLocator=new Locator();
                    ResponseStatus status = handlerMethod.getMethodAnnotation(ResponseStatus.class);
                    if(status==null) {
                        wadlResponse.getStatus().add((long)(HttpStatus.OK.value()));
                    }else {
                        HttpStatus httpcode = status.value();
                        wadlResponse.getStatus().add((long)httpcode.value());
                    }
 
                    for (MediaType mediaType : mediaTypes) {
                        Representation wadlRepresentation = new Representation();
                        wadlRepresentation.setMediaType(mediaType.toString());
                        value.getInclude().add(generateXSD(methodReturn));
//                        QName employeeDetails = convertJavaToXMLType(methodReturn);
                        QName employeeDetails=new QName("employee.EmployeeDetails");
//                        QName employeeDetails=methodReturn.getName();
                        wadlRepresentation.setElement(employeeDetails);
//                        wadlRepresentation.
                        wadlResponse.getRepresentation().add(wadlRepresentation);
                    }
                    wadlMethod.getResponse().add(wadlResponse);
                }
                  
                wadlResource.getMethodOrResource().add(wadlMethod);
                  
            }
              
        }
        System.out.println("wadResources--->"+wadResources.toString());
        result.getResources().add(wadResources);
          
        return result;
    }
    private QName convertJavaToXMLType(Class<?> type) {
        QName nm = new QName("");
        String classname=type.toString();
        System.out.println("classname--->"+classname);
        if (classname.indexOf("String")>=0) {
             nm = new QName(xs_namespace,"string","xs");
             
        }else if(classname.indexOf("Integer")>=0) {
             nm = new QName(xs_namespace,"int","xs");
        }else if(classname.indexOf("long")>=0) {
             nm = new QName(xs_namespace,"long","xs");
        }
        return nm;
    }
    private Resource createOrFind(String uri, Resources wadResources) {
          List<Resource> current = wadResources.getResource();
          for(Resource resource:current) {
              if(resource.getPath().equalsIgnoreCase(uri)){
                  return resource;
              }
          }
          Resource wadlResource = new  Resource();
          current.add(wadlResource);
          return wadlResource;
    }
    private String getBaseUrl (HttpServletRequest request)  {
        String requestUri = request.getRequestURI();
        return request.getScheme()+"://"+ request.getServerName()+":"+ request.getServerPort() + requestUri;
    }
      
    private String cleanDefault(String value) {
        value = value.replaceAll("\t", "");
        value = value.replaceAll("\n", "");
        return value;
    }
    
    public Include generateXSD(Class returnType) {
        Include in=new Include();
        try {
//            JAXBContext jaxbContext = JAXBContext.newInstance(returnType);
//            SchemaOutputResolver sor = new MySchemaOutputResolver();
//            jaxbContext.generateSchema(sor);
        VarudhoIllayo varudhoIllayo=new VarudhoIllayo();
        varudhoIllayo.illaya(returnType);
        in.setHref("http://www.example.org/employee");
//        in.setSourceLocation();
//        value.getInclude().add();
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(WADLControllerV2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            ex.printStackTrace();
            Logger.getLogger(WADLControllerV2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return in;
    }
}
