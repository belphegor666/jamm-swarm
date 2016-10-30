package org.tmr.jamm;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.model.rest.RestBindingMode;

import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;

/**
 * Created by tmr
 */
@ApplicationScoped
@Startup
@ContextName("SwarmCamel")
public class RestDslRoute extends RouteBuilder {
    @Override
    public void configure() {
        // setup Undertow as the REST endpoint
        restConfiguration().component("undertow").host("localhost").port(8080).bindingMode(RestBindingMode.auto);

        // define a simple REST service
        rest("/rest/hello")
            .get()
            .consumes(MediaType.APPLICATION_JSON)
            .produces(MediaType.APPLICATION_JSON)
            .route()
                .setBody(simple("bar"))
                .to("mock:jamm-out");
    }
}
