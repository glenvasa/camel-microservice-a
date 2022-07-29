package com.glenvasa.microservices.camelmicroservicea.routes.a;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyFirstTimerRouter extends RouteBuilder {

    // create routes
    @Override
    public void configure() throws Exception {
        // listen to a queue - one endpoint; Here, however we will use a timer
        // transformation to inputs
        // save to database - second endpoint; Here, however we will save to a log


        // Exchange[ExchangePattern: InOnly, BodyType: null, Body: [Body is null]]
        // timer generates a null message every second; we are picking it up and sending it to a log
        // when we add transform method, now BodyType: String, Body: My Constant Message
        // timer endpoint
        from("timer:first-timer")
//                .transform().constant("My Constant Message")
                .transform().constant("Time is now: " + LocalDateTime.now())
                .to("log:first-timer");
    }

}

@Component
class GetCurrentTimeBean {
    
}