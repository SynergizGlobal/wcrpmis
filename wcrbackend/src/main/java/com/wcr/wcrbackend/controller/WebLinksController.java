package com.wcr.wcrbackend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.DTO.WebLinks;
import com.wcr.wcrbackend.service.IWebLinkService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/weblinks")
public class WebLinksController {
	@Autowired
	private IWebLinkService weblinkService;
	
	Logger logger = Logger.getLogger(WebLinksController.class);
	
	@Value("${common.error.message}")
	public String commonError;
	/*@GetMapping("/api/getWebLinks")
	List<WebLinks> getWebLinks() {
		return weblinkService.getWebLinks();
	}*/
	
	@PostMapping(value="/ajax/form/web-links")
	public Map<String, List<WebLinks>> webLinks(@RequestBody WebLinks obj,HttpSession session){
		//ModelAndView model = new ModelAndView(PageConstants2.webLinks);
		Map<String, List<WebLinks>> model = new HashMap<>();
		try {
			List<WebLinks> webLinksList = weblinkService.getWebLinks(obj);
			model.put("webLinksList", webLinksList);
			
		}catch (Exception e) {
			e.printStackTrace();
			//model.addObject("error", commonError);
			logger.error("webLinks : " + e.getMessage());
		}
		return model;
	}
	
	@PostMapping(value = "/update-web-links")
	public ResponseEntity<?> updateWebLinks(@ModelAttribute WebLinks obj){
		//ModelAndView model = new ModelAndView();
		String attributeKey = "";
		String attributeMsg = "";
		try{
			//model.setViewName("redirect:/web-links");
			boolean flag =  weblinkService.updateWebLinks(obj);
			if(flag) {
				attributeKey = "success";
				attributeMsg = "Web links Updated Succesfully.";
				//attributes.addFlashAttribute("success", "Web links Updated Succesfully.");
			}else {
				attributeKey = "error";
				attributeMsg = "Updating Web links is failed. Try again.";
				//attributes.addFlashAttribute("error","Updating Web links is failed. Try again.");
			}
		}catch (Exception e) {
			e.printStackTrace();
			attributeKey = "error";
			attributeMsg = commonError;
			//attributes.addFlashAttribute("error", commonError);
			logger.error("updateWebLinks : " + e.getMessage());
		}
		return ResponseEntity.ok(Map.of(attributeKey,attributeMsg));
	}
}
