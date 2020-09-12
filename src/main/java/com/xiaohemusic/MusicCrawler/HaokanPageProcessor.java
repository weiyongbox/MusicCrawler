package com.xiaohemusic.MusicCrawler;

import us.codecraft.webmagic.Page;

public class HaokanPageProcessor extends ExtendedPageProcessor {

	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub
//		System.out.print(page.getHtml().toString());
		String title = page.getHtml().css(".page-left .videoinfo .videoinfo-title").get();
		// WebMagic好像没有提供获取text content的方法。
		// 去除<h2  class="videoinfo-title">和</h2>
		if(title == null) {
			title = this.FailedFetchResult;
		} else {
			title = title.replaceAll("<[/]*h2[\\s]*[\\w\"-=]*>", "");
		}
		page.putField("title", title);
		String author = page.getHtml().css(".author .author-detail .tag-1 a").get();
		// WebMagic好像没有提供获取text content的方法。
		// 去除<a>和</a>
		if(author == null) {
			author = this.FailedFetchResult;
		} else {
			author = author.replaceAll("<[/]*a[\\s]*[\\w=\\\"/-\\s]*>", "");
		}
    	page.putField("author", author);
    	page.putField("mp4", page.getHtml().css(".videos .hkplayer video", "src").regex("(https://[.\\w/-]+\\.mp4)").get());
    	synchronized(this) {
    		this.result = page;
    	}
	}

}
