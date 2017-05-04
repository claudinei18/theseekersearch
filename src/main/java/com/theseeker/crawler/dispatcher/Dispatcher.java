package com.theseeker.crawler.dispatcher;

import com.theseeker.crawler.entities.FetchedPages;
import com.theseeker.crawler.entities.queuedURL;
import com.theseeker.crawler.fetcher.Fetcher;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.PostRemove;
import java.io.BufferedReader;

import com.theseeker.crawler.entities.queuedURLDAO.queuedURLDAO;

import com.theseeker.crawler.entities.seenURL;
import com.theseeker.crawler.entities.seenURLDAO.seenURLDAO;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.*;

/**
 * Created by claudinei on 28/03/17
 */
@Component
public class Dispatcher {

    @Autowired
    ApplicationContext ctx;

    @Autowired
    queuedURLDAO queuedURLDAO;

    @Autowired
    seenURLDAO seenURLDAO;

    @Autowired
    Fetcher fetcher;

    ExecutorService executorService;


    public Dispatcher() {

    }

    /*@Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor(); // Or use another one of your liking
    }*/

    /*@Bean
    public CommandLineRunner schedulingRunner(TaskExecutor executor) {
        return new CommandLineRunner() {
            public void run(String... args) throws Exception {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            while (!(queuedURLDAO.queuedURLIsEmpty())) {
                                queuedURL qurl = queuedURLDAO.retrieveAndDelete();
                                if (qurl != null) {
                                    System.out.println("DISPATCHER PROCESSANDO: " + qurl.getDominio());
//                                    Fetcher fetcher = ctx.getBean(Fetcher.class);
                                    try {
                                        fetcher.start(qurl.getDominio());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    seenURL sl = new seenURL(qurl.getDominio(), qurl.getIp());
                                    seenURLDAO.insertURL(sl);
                                }

                            }
                        }
                    }
                });
            }
        };
    }*/

    /*@PostConstruct
    public void teste2() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        final Future handler = executor.submit(new Callable() {
            @Override
            public Object call() throws Exception {
                for (int i = 0; i < 2; i++) {
                    System.out.println("teste");
                    Thread.sleep(500);
                }
                return null;
            }
        });
        executor.schedule(new Runnable() {
            public void run() {
                handler.cancel(true);
            }
        }, 1500, TimeUnit.MILLISECONDS);
    }*/

    /*@PostConstruct
    public void teste() {
        schedulingRunner(taskExecutor());
    }*/

    /*@PostConstruct
    public void readFromQueuedURLs() throws IOException {


        BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .namingPattern("dispatcherThread-%d").build();

        executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while(true){
                    while(!(queuedURLDAO.queuedURLIsEmpty())){
                        queuedURL qurl = queuedURLDAO.retrieveAndDelete();
                        if(qurl != null){
                            System.out.println("DISPATCHER PROCESSANDO: " + qurl.getDominio());
                            Fetcher fetcher = ctx.getBean(Fetcher.class);
                            try {
                                fetcher.start(qurl.getDominio());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            seenURL sl = new seenURL(qurl.getDominio(), qurl.getIp());
                            seenURLDAO.insertURL(sl);
                        }

                    }
                }
            }
        });

    }*/

    /*@PostConstruct
    public void readFromQueuedURLs() throws IOException {
        while (true) {
            while (!(queuedURLDAO.queuedURLIsEmpty())) {
                queuedURL qurl = queuedURLDAO.retrieveAndDelete();
                if (qurl != null) {
                    System.out.println("DISPATCHER PROCESSANDO: " + qurl.getDominio());
                    Fetcher fetcher = ctx.getBean(Fetcher.class);
                    try {
                        fetcher.start(qurl.getDominio());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    seenURL sl = new seenURL(qurl.getDominio(), qurl.getIp());
                    seenURLDAO.insertURL(sl);
                }

            }
        }
    }*/

    @PostConstruct
    public void startDispatcher() {
        new Thread(t1).start();
    }

    private Runnable t1 = new Runnable() {
        public void run() {
            while (true) {
                while (!(queuedURLDAO.queuedURLIsEmpty())) {
                    queuedURL qurl = queuedURLDAO.retrieveAndDelete();
                    if (qurl != null) {
//                            System.out.println("DISPATCHER PROCESSANDO: " + qurl.getDominio());
                        try {
                            fetcher.start(qurl);
                            seenURL sl = new seenURL(qurl.getDominio(), qurl.getIp());
                            seenURLDAO.insertURL(sl);
                        } catch (IOException e) {
                            e.printStackTrace();
                            //INSERIR EM REJECTED
                        }
                    }

                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    };

/*    @PreDestroy
    public void beandestroy() {
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }*/
}
