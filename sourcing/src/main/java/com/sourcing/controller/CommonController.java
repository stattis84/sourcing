package com.sourcing.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sourcing.CrawlingResult;

@Controller
public class CommonController {

    @GetMapping("/test.do")
    public String testCrawling(Model model) {
        List<CrawlingResult> crawlingResults = new ArrayList<>();

        String url = "https://browse.gmarket.co.kr/search?keyword=%EC%8B%9C%EA%B7%B8%EB%8B%88%EC%8A%A4";
        
        System.out.println("url : " + url);
        
        try {
            Document doc = Jsoup.connect(url).get();

            Elements products = doc.select("div.box__component-itemcard");

            for (Element product : products) {
            	
            	if(product.select("span.text__count").text().equals("")) {
            		continue;
            	}
            	
                int purchaseCount = Integer.parseInt(product.select("span.text__count").text());

                if (purchaseCount >= 1) {
                    String title = product.select("div.box__item-title > strong.title__text").text();
                    String link = "https://browse.gmarket.co.kr" + product.select("a.link__item").attr("href");
                    String imageUrl = product.select("img.img__item").attr("data-src");

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
}