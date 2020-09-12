package com.xiaohemusic.MusicCrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import us.codecraft.webmagic.Page;

public class Kugou5singMp3PageProcessor extends ExtendedPageProcessor {

	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub
		System.out.print(page.getHtml().toString());
	}

	/**
	 * 该方法把 PhantomJS 作为单独的进程运行；结果内容通过 “标准输入 、标准输出” 作为string传送到本Java进程。
	 * 
	 * @param url
	 * @return
	 */
	public Map<String, Object> fetchMp3Directly(String url) {
		String HTML = "";
		String jsPath = "C:\\weiyong\\tools\\phantomjs-2.1.1-windows\\getPage.js";
		String exePath = "phantomjs.exe";
		Runtime rt = Runtime.getRuntime();
		Process p;
		InputStream is = null;
		BufferedReader br = null;
		StringBuffer sbf = null;

		try {
			p = rt.exec(exePath + " " + jsPath + " " + url);

			is = p.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			sbf = new StringBuffer();
			String tmp = "";
			while ((tmp = br.readLine()) != null) {
				sbf.append(tmp);
				sbf.append("\r\n");
			}
			HTML = sbf.toString();

//			System.out.print(HTML);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			sbf = null;
			is = null;
			br = null;
		}

		return this.extractMp3(HTML);
	}

	private Map<String, Object> extractMp3(String html) {
		Map<String, Object> result = new HashMap<String, Object>();

//		Page page = new Page();
//		page.setRawText(html);
//		result.put("author", page.getHtml().css(".user_info_box .user_name a").get());
//		result.put("mp3", page.getHtml().regex("(https://[\\w/.]+\\.mp3)").get());

		Pattern r = Pattern.compile("(https://[\\w/.]+\\.mp3)");
		Matcher m = r.matcher(html);
		if (m.find()) {
			result.put("author", "unknown");
			result.put("mp3", m.group(0));
		}

		return result;
	}

	/**
	 * 本方法使用标准 WebDriver 接口控制 PhantomJS的行为和内容。 WebDrvier是GhostDriver，内嵌在
	 * PhantomJS之中； Java binding使用的是 Selenium 提供的 实现 PhantomJSDriver。
	 * 
	 * @param url
	 * @return
	 */
	public Map<String, Object> fetchMp3BySelenium(String url) {
		Map<String, Object> result = new HashMap<String, Object>();
		String phantomjsCmd = "phantomjs";
		if(System.getProperty("os.name").toLowerCase().contains("windows")) {
			phantomjsCmd = "phantomjs.exe";
		}
		System.setProperty("phantomjs.binary.path", System.getProperty("user.dir") + "/" + phantomjsCmd);
		
		// 设置driver的各种参数
		DesiredCapabilities caps = new DesiredCapabilities();
//		 caps.setCapability("", value);
		WebDriver driver = new PhantomJSDriver(caps);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		try {
			driver.get(url);
			
			WebElement element = driver.findElement(By.cssSelector(".k_content .k_main .view .view_tit h1"));
			result.put("title", element.getText());
			element = driver.findElement(By.cssSelector(".user_info_box .user_name a"));
			result.put("author", element.getText());
			element = driver.findElement(By.tagName("audio"));
			result.put("mp3", element.getAttribute("src"));
		} finally {
			driver.quit();
		}

		return result;
	}

}
