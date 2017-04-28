package com.theseeker;

import com.theseeker.util.robots.NoRobotClient;
import com.theseeker.util.robots.NoRobotException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

@SpringBootApplication
public class TheSeekerApplication {

	private static final Logger log = LoggerFactory.getLogger(TheSeekerApplication.class);


	public static void main(String[] args) throws UnknownHostException, MalformedURLException, NoRobotException {
		SpringApplication app = new SpringApplication(TheSeekerApplication.class);
		SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);
		ConfigurableApplicationContext run = app.run(args);
		run.registerShutdownHook();
		Environment env = run.getEnvironment();
		log.info("Access URLs:\n----------------------------------------------------------\n\t" +
						"Local: \t\thttp://127.0.0.1:{}\n\t" +
						"External: \thttp://{}:{}\n----------------------------------------------------------",
				env.getProperty("server.port"),
				InetAddress.getLocalHost().getHostAddress(),
				env.getProperty("server.port"));


	}
}
