package com.itkhamar.config;

import org.apache.camel.spi.CamelEvent;
import org.apache.camel.support.EventNotifierSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RouteStatusEventNotifier extends EventNotifierSupport {
    private static final Logger LOGGER = LoggerFactory.getLogger(RouteStatusEventNotifier.class);

    @Override
    public void notify(CamelEvent event) throws Exception {
        if (event.getType().equals(CamelEvent.Type.RouteStarted)) {
            String routeId = event.toString();
            LOGGER.info("Route : {}", routeId);
        }
    }

    @Override
    public boolean isEnabled(CamelEvent event) {
        return event.getType().equals(CamelEvent.Type.RouteStarted);
    }

    @Override
    protected void doStart() throws Exception {
        // No initialization required
    }

    @Override
    protected void doStop() throws Exception {
        // No cleanup required
    }
}
