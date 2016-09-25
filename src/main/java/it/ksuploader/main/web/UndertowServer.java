package it.ksuploader.main.web;


import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.PathResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import it.ksuploader.main.KSUploaderServer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import java.nio.file.Paths;

import static io.undertow.Handlers.resource;
import static it.ksuploader.main.KSUploaderServer.config;

/**
 * Created by Sergio on 25/09/2016.
 */
public class UndertowServer {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	private Undertow server;
	
	public UndertowServer() {
		try {
			DeploymentInfo servletBuilder = Servlets.deployment()
					.setClassLoader(UndertowServer.class.getClassLoader())
					.setDeploymentName("HttpRequestHandler")
					.setContextPath("/")
					.addServlets(
							Servlets.servlet(
									"HttpRequestHandler",
									HttpRequestHandler.class)
									.addMapping("/")
									.setMultipartConfig(new MultipartConfigElement(System.getProperty("java.io.tmpdir"), config.getMaxFileSize(), config.getMaxFileSize() * 1024, 0))
					);
			
			DeploymentManager manager = Servlets.defaultContainer().addDeployment(servletBuilder);
			manager.deploy();
			
			PathHandler path = Handlers.path(
					resource(new PathResourceManager(Paths.get(KSUploaderServer.config.getFolder()), 0))
							.setDirectoryListingEnabled(false)
			).addPrefixPath("/upload", manager.start());
			
			
			this.server = Undertow.builder()
					.addHttpListener(KSUploaderServer.config.getWebPort(), "0.0.0.0")
					.setHandler(path)
					.build();
		} catch (ServletException e) {
			logger.log(Level.ERROR, "Cannot set webserver paths.");
		}
	}
	
	public void start() {
		try {
			this.server.start();
			logger.log(Level.INFO, "Webserver listening on port " + config.getWebPort() + ".");
		} catch (Exception e) {
			logger.log(Level.ERROR, "Cannot start webserver.");
		}
	}
	
}
