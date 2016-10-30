package org.tmr.jamm;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;

import javax.enterprise.context.ApplicationScoped;
import javax.ejb.Startup;

/**
 * Created by tmr
 */
@ApplicationScoped
@Startup
@ContextName("SwarmCamel")
public class SampleRoute extends RouteBuilder {
    @Override
    public void configure() {
        from("undertow:http://localhost/hello")
            .setBody(simple("foo"))
            .to("mock:jamm-out");
    }
}
