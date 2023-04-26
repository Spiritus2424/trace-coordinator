package org.eclipse.trace.coordinator.startuplistener;

import org.eclipse.trace.coordinator.trace.TraceController;
import org.eclipse.trace.coordinator.traceserver.TraceServerManager;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class StartupListener implements ServletContextListener {

    @Inject
    private TraceServerManager traceServerManager;

    @Inject
    private TraceController traceController;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Perform any initialization tasks here
        // This method will be called when the server is fully started and ready to
        // handle requests
        System.out.println("READER");
        System.out.println(this.traceServerManager.getTraceServers().size());
        System.out.println(this.traceController.toString());

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Perform any cleanup tasks here
        // This method will be called when the server is shutting down
    }
}