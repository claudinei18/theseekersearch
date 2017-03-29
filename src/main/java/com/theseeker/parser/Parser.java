package com.theseeker.parser;

import com.theseeker.entities.FetchedPages;
import org.springframework.stereotype.Component;

/**
 * Created by claudinei on 29/03/17.
 */
@Component
public class Parser {

    public void parseFetchedPage(FetchedPages fp){
        System.out.println("Realizando Parser");
        System.out.println(fp.getDominio());
    }
}
