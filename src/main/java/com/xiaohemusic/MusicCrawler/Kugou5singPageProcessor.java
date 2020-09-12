/**
 * 
 */
package com.xiaohemusic.MusicCrawler;

import us.codecraft.webmagic.Page;

/**
 * @author weiyong
 *
 */
public class Kugou5singPageProcessor extends ExtendedPageProcessor {
	@Override
	public void process(Page page) {
//        page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/\\w+/\\w+)").all());
//    	page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
//        page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());
//        if (page.getResultItems().get("name")==null){
//            //skip this page
//            page.setSkip(true);
//        }
//        page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));
		String title = page.getHtml().css(".ws_mv_player .content .title .con span").get();
		if (title == null) {
			title = this.FailedFetchResult;
		} else {
			// WebMagic好像没有提供获取text content的方法。
			// 去除<span>和</span>
			title = title.replaceAll("<[/]*span>", "");
		}
		page.putField("title", title);
		page.putField("author", page.getHtml().css(".ws_mv_player .content-r .con .name a", "title").get());
		page.putField("mp4", page.getHtml().css("script").regex("[\\w'.:/-]+.mp4").all());
		synchronized (this) {
			this.result = page;
		}

//        System.out.println(page.getUrl().toString());
//        System.out.println(page.getHtml().getDocument().toString());
	}
}
