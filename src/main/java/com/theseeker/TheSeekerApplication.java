package com.theseeker;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.*;
import com.theseeker.crawler.entities.seenURL;
import com.theseeker.util.robots.NoRobotClient;
import com.theseeker.util.robots.NoRobotException;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

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

		NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(
				NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
				"b845dcb3-1e77-4eb1-bf43-3cd7d71f9067",
				"Pd7kWrDmARew"
		);

		/*EntitiesOptions entities = new EntitiesOptions.Builder().limit(10000).build();
		Features features = new Features.Builder().entities(entities).build();
		AnalyzeOptions parameters = new AnalyzeOptions.Builder().url("www.cnn.com").features(features).build();
		AnalysisResults results = service.analyze(parameters).execute();
		List<EntitiesResult> a = results.getEntities();



		for (EntitiesResult b: a) {
			System.out.println(b.getText());
			System.out.println(b.getCount());
			System.out.println(b.getRelevance());
			System.out.println(b.getType());
			System.out.println(b.getDisambiguation().getDbpediaResource());
			System.out.println(b.getDisambiguation().getSubtype());
		}*/

		/*try {
			Document doc = Jsoup.connect("http://dbpedia.org/resource/Barack_Obama")
                    .userAgent("TheSeeker1.0")
                    .header("Accept-Language", "en")
                    .timeout(3000)
                    .get();
			doc = Jsoup.parse(doc.html());
			String x = doc.body().text();
			System.out.println(x);
		} catch (IOException e) {
			e.printStackTrace();
		}*/

		/*try{
			Document doc = Jsoup.connect("http://dbpedia.org/resource/Kevin_Hart_(actor)")
					.userAgent("TheSeeker1.0")
					.header("Accept-Language", "en")
					.timeout(3000)
					.get();

			Element p = doc.select("p").first();
			String text = p.text(); //some bold text
			System.out.println(text);
		}catch (Exception e) {
            *//*System.out.println("ERRO: Não conseguiu coletar com o JSOUP. " + dominio);
            e.printStackTrace();*//*
		}*/

	}
}
