package com.wcr.wcrbackend.reference.controller;

import java.util.List;



import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wcr.wcrbackend.constants.PageConstants;
import com.wcr.wcrbackend.reference.Iservice.BankNameService;
import com.wcr.wcrbackend.reference.model.Bank;

import jakarta.servlet.http.HttpSession;


@Controller
public class BankNameController {

	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(BankNameController.class);
	
	@Autowired
	BankNameService service;
	
	@RequestMapping(value="/bank-name",method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView BankName(HttpSession session,@ModelAttribute Bank obj){
		ModelAndView model = new ModelAndView(PageConstants.bankName);
		try {
			List<Bank> BankNameList = service.getBankNamesList();
			model.addObject("BankNameList", BankNameList);
			Bank bankNameDetails = service.getBankNameDetails(obj);
			model.addObject("bankNameDetails", bankNameDetails);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("BankName : " + e.getMessage());
		}
		return model;
	}
	
	
	@RequestMapping(value = "/add-bank-name", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView addBankName(@ModelAttribute Bank obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/bank-name");
			boolean flag =  service.addBankName(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Bank Name Added Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Adding Bank Name is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Adding Bank Name is failed. Try again.");
			logger.error("addBankName : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/update-bank-name", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView updateBankName(@ModelAttribute Bank obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/bank-name");
			boolean flag =  service.updateBankName(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Bank Name Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Updating Bank Name is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Updating Bank Name is failed. Try again.");
			logger.error("updateBankName : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/delete-bank-name", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView deleteBankName(@ModelAttribute Bank obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/bank-name");
			boolean flag =  service.deleteBankName(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Bank Name Deleted Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			logger.error("deleteBankName : " + e.getMessage());
		}
		return model;
	}
}




