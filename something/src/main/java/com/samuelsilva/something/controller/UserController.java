package com.samuelsilva.something.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.samuelsilva.something.enums.UserStatusEnum;
import com.samuelsilva.something.model.User;
import com.samuelsilva.something.repository.filter.UserFilter;
import com.samuelsilva.something.service.UserService;

/**
 * @author samuel.silva
 */

@Controller
@RequestMapping("/userregistration")
public class UserController {
	
	// Redirect attributes
	private static final String USER_CRUD = "UserCrud";
	private static final String USER_SEARCH = "UserSearch";
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/usercrud")
	public ModelAndView init() {
		ModelAndView mv = new ModelAndView(USER_CRUD);
		mv.addObject(new User());
		
		return mv;
	}
	
	@RequestMapping
	public ModelAndView searchUser(@ModelAttribute("filter") UserFilter filter) {
		List<User> responseList = userService.search(filter);
		
		ModelAndView mv = new ModelAndView(USER_SEARCH);
		mv.addObject("userList", responseList);
		
		return mv; 
	}
	
	@RequestMapping("{id}")
	public ModelAndView update(@PathVariable("id") User user) {
		ModelAndView mv = new ModelAndView(USER_CRUD);
		mv.addObject(user);
		
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String save(@Validated User user, Errors errors, RedirectAttributes redirectAttributes) {
		if(errors.hasErrors()) {
			return USER_CRUD;
		}
		
		userService.save(user);
		redirectAttributes.addFlashAttribute("message", "User successfully saved!");
		
		return "redirect:/userregistration/usercrud";
	}
	
	@RequestMapping(value="{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		userService.delete(id);
		
		redirectAttributes.addFlashAttribute("message", "User successfully deleted!");
		return "redirect:/userregistration";
	}
	
	@RequestMapping(value = "/{id}/changeStatus", method = RequestMethod.PUT)
	public @ResponseBody String changeStatus(@PathVariable Long id) {
		return userService.changeStatus(id);
	}
	
	@ModelAttribute("userStatusList")
	public List<UserStatusEnum> getUserStatusEnumList() {
		return Arrays.asList(UserStatusEnum.values());
	}
}
