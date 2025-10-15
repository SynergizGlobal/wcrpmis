package com.wcr.wcrbackend.common;

import java.net.URL;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class UrlGenerator {
	Logger logger = Logger.getLogger(UrlGenerator.class);
	public String getURLBase() {
		String base_url = "";
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
			        .getRequestAttributes()).getRequest();
		    URL requestURL = new URL(request.getRequestURL().toString());
		    String port = requestURL.getPort() == -1 ? "" : ":" + requestURL.getPort();
		    //return requestURL.getProtocol() + "://" + requestURL.getHost() + port;
		    
		    base_url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		    
		    //return CommonConstants.PORTAL + request.getContextPath();
		    //return CommonConstants.CONTEXT_PATH;
		}catch (Exception e) {
			logger.error("getURLBase : " + e.getMessage());
		}
		return base_url;
	}
	
	public String getIpAddress(){
		String ip_address = "";
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
			        .getRequestAttributes()).getRequest();
			
		    URL requestURL = new URL(request.getRequestURL().toString());
		    String port = requestURL.getPort() == -1 ? "" : ":" + requestURL.getPort();
		    //return requestURL.getProtocol() + "://" + requestURL.getHost() + port;
		    //ip_address = requestURL.getHost();
		    ip_address = request.getServerName().toString();
			/*if("10.203.10.158".equals(ip_address)) {
				ip_address = "pmis.mrvc.gov.in";
			}*/
		} catch (Exception e) {
			logger.error("getIpAddress : " + e.getMessage());
		}
		return ip_address;
	}
	
	public String getContextPath(){
		String context_path = "";
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
			        .getRequestAttributes()).getRequest();
		    context_path = request.getContextPath().toString();
		    if(!StringUtils.isEmpty(context_path)) {
		    	context_path = context_path.replaceAll("/", "");
		    }
		} catch (Exception e) {
			logger.error("getContextPath : " + e.getMessage());
		}
		return context_path;
	}
	
	public String getNGINXFilesBasePath(){
		String base_path = "";
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
			        .getRequestAttributes()).getRequest();
			String ip_address = request.getServerName().toString();
			/*if("10.203.10.158".equals(ip_address)) {
				ip_address = "pmis.mrvc.gov.in";
			}*/
		    
		    if("10.203.10.158".equals(ip_address) || "203.153.40.44".equals(ip_address) || "pmis.mrvc.gov.in".equals(ip_address)) {
		    	base_path = "C:/Development/nginx-1.9.9/html/"+getContextPath();
		    }else if("13.235.73.61".equals(ip_address)) {
		    	base_path = "usr/share/nginx/html/"+getContextPath();
		    }else if("127.0.0.1".equals(ip_address) || "localhost:90".equals(ip_address)) {
		    	base_path = "C:/Development/nginx-1.9.9/html/"+getContextPath();
		    }
		} catch (Exception e) {
			logger.error("getNGINXFilesBasePath : " + e.getMessage());
		}
		return base_path;
	}
	
	public String getUser_login_report_mail_id(){
		String user_login_report_mail_id = "";
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
			        .getRequestAttributes()).getRequest();
			String ip_address = request.getServerName().toString();
			/*if("10.203.10.158".equals(ip_address)) {
				ip_address = "pmis.mrvc.gov.in";
			}*/
		    
		    if("10.203.10.158".equals(ip_address) || "203.153.40.44".equals(ip_address) || "pmis.mrvc.gov.in".equals(ip_address)) {
		    	user_login_report_mail_id = "dycste2@mrvc.gov.in";
		    }else if("13.235.73.61".equals(ip_address)) {
		    	user_login_report_mail_id = "raviteja.reddy@synergizglobal.com";
		    }else if("127.0.0.1".equals(ip_address) || "localhost:90".equals(ip_address)) {
		    	user_login_report_mail_id = "gkalluri@ghsltechnologies.com";
		    }
		} catch (Exception e) {
			logger.error("getUser_login_report_mail_id : " + e.getMessage());
		}
		return user_login_report_mail_id;
	}
	
	public String getManagers_mail_ids_for_alerts(){
		String managers_mail_ids = "";
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
			        .getRequestAttributes()).getRequest();
			String ip_address = request.getServerName().toString();
			/*if("10.203.10.158".equals(ip_address)) {
				ip_address = "pmis.mrvc.gov.in";
			}*/
		    
		    if("10.203.10.158".equals(ip_address) || "203.153.40.44".equals(ip_address) || "pmis.mrvc.gov.in".equals(ip_address)) {
		    	managers_mail_ids = "rajiv.dhupkar@synergizglobal.com,raviteja.reddy@synergizglobal.com";
		    }else if("13.235.73.61".equals(ip_address)) {
		    	managers_mail_ids = "rajiv.dhupkar@synergizglobal.com,raviteja.reddy@synergizglobal.com";
		    }else if("127.0.0.1".equals(ip_address) || "localhost:90".equals(ip_address)) {
		    	managers_mail_ids = "kallurigurappa@gmail.com,gkalluri@ghsltechnologies.com";
		    }
		} catch (Exception e) {
			logger.error("getUser_login_report_mail_id : " + e.getMessage());
		}
		return managers_mail_ids;
	}
	
}
