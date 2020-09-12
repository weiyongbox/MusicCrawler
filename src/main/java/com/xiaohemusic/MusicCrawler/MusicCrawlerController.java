package com.xiaohemusic.MusicCrawler;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/crawlers")
public class MusicCrawlerController {
    
    @RequestMapping(value = "/5sing",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> crawl5sing(@RequestParam(value = "url") String url) {
    	return new Kugou5singPageProcessor().crawl(url);
    }
    
    @RequestMapping(value = "/haokan",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> crawlHaokan(@RequestParam(value = "url") String url) {
    	return new HaokanPageProcessor().crawl(url);
    }
    
    @RequestMapping(value = "/5singmp3",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> crawl5singmp3(@RequestParam(value = "url") String url) {
//    	return new Kugou5singMp3PageProcessor().fetchMp3Directly(url);
    	return new Kugou5singMp3PageProcessor().fetchMp3BySelenium(url);
    }
    
    
}
