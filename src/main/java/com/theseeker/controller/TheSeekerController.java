package com.theseeker.controller;

import com.theseeker.indexer.BM25;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by claudinei on 24/05/17.
 */
@Controller
@RequestMapping("/rest")
public class TheSeekerController {
    @Autowired
    BM25 bm;

    @RequestMapping(value = "/consult", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public String simplex(@RequestBody String consulta) {
        System.out.println("JSON: " + consulta);
        try {
            long antes = System.currentTimeMillis();
            bm.consult(consulta);
            long depois = System.currentTimeMillis();
            long diff = depois - antes;
            return ""+diff;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ERRO";
    }

}