package com.xiaohemusic.MusicCrawler;

import java.util.Map;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public abstract class ExtendedPageProcessor implements PageProcessor {
	private Site site = Site.me().setRetryTimes(3).setSleepTime(100);
	public volatile Page result = null;
	
	final public String FailedFetchResult = "unknown";
	
	
    @Override
    public Site getSite() {
        return site;
    }

    /**
     * 
     * @param url to the page to crawl
     * @return
     */
    public Map<String, Object> crawl(String url) {
        	Spider.create(this).setDownloader(new HttpClientDownloader()).addUrl(url).thread(5).run();
        	// 
        	while(this.result == null) {
        		try {
        			Thread.sleep(500);
        		} catch(InterruptedException e) {
        			e.printStackTrace();
        		}
        	}
        	return this.result.getResultItems().getAll();
        }
}
