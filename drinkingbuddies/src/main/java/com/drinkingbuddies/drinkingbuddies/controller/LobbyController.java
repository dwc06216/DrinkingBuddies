package com.drinkingbuddies.drinkingbuddies.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.drinkingbuddies.drinkingbuddies.controller.Util.User;
import com.drinkingbuddies.drinkingbuddies.controller.Util.dbUtility;

@Controller
public class LobbyController {
	private LinkedList<Player> allPlayers = new LinkedList<>();
	private dbUtility util = new dbUtility();
	
    @RequestMapping(value = "/lobby", method = RequestMethod.GET)
    public String lobbyPage(HttpServletRequest request){
    	// Get participants bitch!!!!!!!!!!!!!
    	String lobbyName = readCookie("lobbyName", request).get();
    	LinkedList<String> allPlayerEmails = util.getParticipants(lobbyName);
    	
    	// Need to check if the user is actually me
    	String myName = readCookie("userName",request).get();
    	
    	
    	// Need to construct player objects and get them into the list
    	User me = null;
    	int myShots = 0;
    	int seat = 2;
    	for (String e : allPlayerEmails) {
    		User u = util.getUser(e);
    		String username = u.getUsername();
    		int amount = util.getAmountDrinked(u.getEmail(), lobbyName);
    		if (username.equals(myName)) {
    			myShots = amount;
    		}else {
    			Player otherPlayer = new Player(username, amount, seat);
        		allPlayers.add(otherPlayer);
        		seat++;
    		}
    	}
    	Player mePlayer = new Player(myName, myShots, 1);
    	allPlayers.add(0, mePlayer);
    	for (Player p : allPlayers) {
    		System.out.println(p.getName());
    		System.out.println(p.getShots());
    	}
        return "lobby";
    }
    
    @RequestMapping(value = "/lobby", method = RequestMethod.POST)
    public void addDrink(@RequestParam String userID) {
    	for (Player p : allPlayers) {
    		if (p.getName().equals(userID)) {
    			p.addShot();
    		}
    	}
    }
	
	public class Player{
		private String name;
		private int shots;
		private int seatNum;
		
		Player(String name, int shots, int seatNum){
			this.name = name;
			this.shots = shots;
			this.seatNum = seatNum;
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getShots() {
			return shots;
		}
		public void setShots(int shots) {
			this.shots = shots;
		}
		public void addShot() {
			this.shots += 1;
		}
		public void addShots(int shots) {
			this.shots += shots;
		}
	}
	
	// Read them cookies
	public Optional<String> readCookie(String key, HttpServletRequest request) {
	    return Arrays.stream(request.getCookies())
	      .filter(c -> key.equals(c.getName()))
	      .map(Cookie::getValue)
	      .findAny();
	}
}
