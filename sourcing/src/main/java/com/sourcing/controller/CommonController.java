package com.sourcing.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sourcing.CrawlingResult;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CommonController {
	
	@GetMapping("/testform.do")
	public String testform(Model model) {
		
		return "crawling_form";
		
	}
	
    @GetMapping("/test.do")
    public String testCrawling(Model model) {
        List<CrawlingResult> crawlingResults = new ArrayList<>();

        String url = "https://browse.gmarket.co.kr/search?keyword=%EC%8B%9C%EA%B7%B8%EB%8B%88%EC%8A%A4";
        
        System.out.println("url : " + url);
        
        try {
            Document doc = Jsoup.connect(url).get();

            Elements products = doc.select("div.box__component-itemcard");

            for (Element product : products) {
            	
            	if(product.select(".list-item__pay-count > span.text").text().replace("구매 ", "").equals("")) {
            		continue;
            	}
            	
                int purchaseCount = Integer.parseInt(product.select(".list-item__pay-count > span.text").text().replace("구매 ", ""));
                
                if (purchaseCount >= 1) {
                    String title = product.select("div.box__item-title > span.text__item-title > a > span.text__item").text();
                    String link = product.select("a.link__item").attr("href");
                    String imageUrl = product.select("div.box__image > a > img").attr("src");

                    CrawlingResult result = new CrawlingResult();
                    result.setTitle(title);
                    result.setLink(link);
                    result.setImageUrl(imageUrl);

                    crawlingResults.add(result);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        model.addAttribute("crawlingResults", crawlingResults);
        return "crawling_results";
    }
    
    
    @GetMapping("/seltest.do")
    public String seleniumCrawling(HttpServletRequest request, Model model) {
    	
    	System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe"); // ChromeDriver 경로 설정
        WebDriver driver = new ChromeDriver();
    	
        List<CrawlingResult> crawlingResults = new ArrayList<>();
        
        String keyword = request.getParameter("keyword");
        
        String url = "https://browse.gmarket.co.kr/search?keyword=" + keyword;
        
        System.out.println("url : " + url);
        
        try {
            driver.get(url);
            Thread.sleep(5000); // 일정 시간 대기하여 이미지가 로드되는 것을 기다림
            
            System.out.println("driver : " + driver.toString());
            
            /*
            var stTime = new Date().getTime(); //현재시간
            var scrollSize = 0;
            while (new Date().getTime() < stTime + 10000) { //30초 동안 무한스크롤 지속
                Thread.sleep(500); //리소스 초과 방지
                //executeScript: 해당 페이지에 JavaScript 명령을 보내는 거
                ((JavascriptExecutor)driver).executeScript("window.scrollTo(" + scrollSize + ", " + scrollSize + 300 + ")", driver.findElement(By.cssSelector("div.section__module-wrap")));
                
                scrollSize += 300;
                
            }
            */
            
            List<WebElement> products = driver.findElements(By.cssSelector("div.box__component-itemcard"));
            
            int scrollSize = 0;
            for (WebElement product : products) {
            	
            	((JavascriptExecutor)driver).executeScript("window.scrollTo(" + scrollSize + ", " + scrollSize + 10 + ")", product);
            	
            	scrollSize += 10;
            	
            	try {
            		
                	if(product.findElement(By.cssSelector(".list-item__pay-count > span.text")).getText().replace("구매 ", "").equals("")) {
                		continue;
                	}
                	
                    int purchaseCount = Integer.parseInt(product.findElement(By.cssSelector(".list-item__pay-count > span.text")).getText().replace("구매 ", ""));
                    
                    if (purchaseCount >= 1) {
                        String title = product.findElement(By.cssSelector("div.box__item-title > span.text__item-title > a > span.text__item")).getText();
                        String link = product.findElement(By.cssSelector("a.link__item")).getAttribute("href");
                        String imageUrl = product.findElement(By.cssSelector("div.box__image > a > img")).getAttribute("src");
                        String price = product.findElement(By.cssSelector("div.box__item-price > div.box__price-seller > strong.text__value")).getText();
                        String payCount = product.findElement(By.cssSelector(".list-item__pay-count > span.text")).getText().replace("구매 ", "");
                        
                        CrawlingResult result = new CrawlingResult();
                        result.setTitle(title);
                        result.setLink(link);
                        result.setImageUrl(imageUrl);
                        result.setPrice(price);
                        result.setPayCount(payCount);

                        crawlingResults.add(result);
                    }
                    
            	} catch(Exception e) {
            		
            		System.out.println("구매갯수 없음");
            		
            	}
            	
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        model.addAttribute("crawlingResults", crawlingResults);
        return "crawling_results";
    }
    
}