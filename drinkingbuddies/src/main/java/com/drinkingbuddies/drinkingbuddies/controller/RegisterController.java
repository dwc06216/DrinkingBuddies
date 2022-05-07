package com.drinkingbuddies.drinkingbuddies.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.drinkingbuddies.drinkingbuddies.controller.Util.dbUtility;

@Controller
public class RegisterController {
	private dbUtility util = new dbUtility();
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String registerPage() {
		return "register";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String homePage(ModelMap model, @RequestParam String email, 
			@RequestParam String password, @RequestParam String userID,
			@RequestParam String legalName, @RequestParam String birthDate,
			@RequestParam String gender, @RequestParam String weight, @RequestParam String phone,
			@RequestParam String emergency, HttpServletResponse response) {
		// First error checking for the input fields
		Map<String, String> errorMap = new HashMap<>();
		int myWeight = 0;
		if (!isInteger(weight)) {
			errorMap.put("errorWeight", "Please enter a integer here");
		}else {
			myWeight = Integer.parseInt(weight);
		}		
		
		// Add to database and cookie if no problem is detected
		if (errorMap.isEmpty()) {
			util.newUser(email, userID, password, birthDate, phone, emergency, myWeight, "I am a person!");
			Cookie usrName = new Cookie("userName", "Welcome="+userID+"!");
			Cookie usrEmail = new Cookie("userEmail", email);
			usrName.setMaxAge(3600);
			usrEmail.setMaxAge(3600);
			response.addCookie(usrName);
			response.addCookie(usrEmail);
			return "redirect:/home";
		}else {
			for (Entry<String, String> e : errorMap.entrySet()) {
				model.put(e.getKey(), e.getValue());
			}
			return "register";
		}
		
	}
	
	
	//Helper functions
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    // only got here if we didn't return false
	    return true;
	}
}
