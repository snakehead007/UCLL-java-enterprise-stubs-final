package be.ucll.java.ent.soap.server;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class StudentsWebServiceConfig extends WsConfigurerAdapter {

    @Bean(name = "studentsService") // WSDL name like f.i. http://localhost:8180/stubs/soap/studentsService.wsdl
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema studentSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("StudentsPort");
        wsdl11Definition.setLocationUri("/soap/v1/students"); // URL to call SOAP upon
        wsdl11Definition.setTargetNamespace("http://ucll.be/java/ent/students");
        wsdl11Definition.setSchema(studentSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema StudentSchema() {
        return new SimpleXsdSchema(new ClassPathResource("students.xsd"));
    }

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true); // Generate WSDL on the fly
        return new ServletRegistrationBean(servlet, "/soap/*");
    }

}

