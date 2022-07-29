package com.glenvasa.microservices.camelmicroservicea.routes.a;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class MyFirstTimerRouter extends RouteBuilder {

    @Autowired
    private GetCurrentTimeBean getCurrentTimeBean;

    @Autowired
    private SimpleLoggingProcessingComponent loggingComponent;


    // create routes
    @Override
    public void configure() throws Exception {
        // listen to a queue - one endpoint; Here, however we will use a timer
        // transformation to inputs
        // save to database - second endpoint; Here, however we will save to a log


        // Exchange[ExchangePattern: InOnly, BodyType: null, Body: [Body is null]]
        // timer generates a null message every second; we are picking it up and sending it to a log
        // when we add transform method, now BodyType: String, Body: My Constant Message
        // 2 things we can do in our route: processing and transformation

        // timer endpoint
        from("timer:first-timer")
                .log("${body}") // null
                .transform().constant("My Constant Message")
                .log("${body}") // My Constant Message
//                .transform().constant("My Constant Message")
//                .transform().constant("Time is now: " + LocalDateTime.now()) // each message has same time b/c it is constant
                .bean(getCurrentTimeBean, "getCurrentTime") // spring bean used for transformation. Bean is invoked each time a message created, so we get new updated time for each new message.
                .log("${body}")// Time is now ....
                .bean(loggingComponent)
                .log("${body}") // body stays the same b/c loggingComponent bean returns nothing; processing not transformation
                .to("log:first-timer");
    }

}

@Component
class GetCurrentTimeBean {
    // transformation, returning something(String)
    public String getCurrentTime() {
        return "Time is now: " + LocalDateTime.now();
    }
}


    @Component
    class SimpleLoggingProcessingComponent {

        private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessingComponent.class);

        // processing; returning void
        public void process(String message){
             logger.info("SimpleLoggingProcessingComponent {}", message);
        }

}