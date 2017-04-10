package com.theseeker;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class TheSeekerApplication {

	private static final Logger log = LoggerFactory.getLogger(TheSeekerApplication.class);


	public static void main(String[] args) throws UnknownHostException {
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

		NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(
				NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
				"b845dcb3-1e77-4eb1-bf43-3cd7d71f9067",
				"Pd7kWrDmARew"
		);



		String text =
				"In 2009, Elliot Turner launched AlchemyAPI to process the written word, with all of its quirks and nuances,"
						+ " and got immediate traction.";

		EntitiesOptions entities = new EntitiesOptions.Builder()
				.limit(200)
				.sentiment(true)
				.build();

		Features features = new Features.Builder()
				.entities(entities)
				.build();

		AnalyzeOptions parameters = new AnalyzeOptions.Builder()
				.text(text)
				.features(features)
				.returnAnalyzedText(true)
				.build();

		AnalysisResults results = service.analyze(parameters).execute();


		for (EntitiesResult result : results.getEntities()) {
			System.out.println(result.getText());
		}
	}
}
