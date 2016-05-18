package com.samuelsilva.something.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.samuelsilva.something.enums.UserStatusEnum;
import com.samuelsilva.something.model.User;
import com.samuelsilva.something.repository.Users;

/**
 * @author samuel.silva
 */

@Controller
@RequestMapping("/userregistration")
public class UserCrudController {
	
	@Autowired
	private Users users;
	
	@RequestMapping("/usercrud")
	public ModelAndView init() {
		ModelAndView mv = new ModelAndView("UserCrud");
		mv.addObject(new User());
		
		return mv;
	}
	
	@RequestMapping
	public ModelAndView searchUser() {
		List<User> allUsers = users.findAll();
		
		ModelAndView mv = new ModelAndView("UserSearch");
		mv.addObject("userList", allUsers);
		
		return mv; 
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String save(@Validated User user, Errors errors, RedirectAttributes redirectAttributes) {
		user.setRegistrationDate(new Date());
		
		if(errors.hasErrors()) {
			return "UserCrud";
		}
		
		users.save(user);
		redirectAttributes.addFlashAttribute("message", "User successfully saved!");
		
		return "redirect:/userregistration/usercrud";
	}
	
	@ModelAttribute("userStatusList")
	public List<UserStatusEnum> getUserStatusEnumList() {
		return Arrays.asList(UserStatusEnum.values());
	}
}
