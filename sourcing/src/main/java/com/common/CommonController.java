package com.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CommonController {
	
	@RequestMapping("/test.do")
	public void test(HttpServletRequest request, HttpServletResponse response) {
		
		String baseUrl = "";
		
	}
	
}
