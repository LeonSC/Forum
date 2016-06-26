package controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import model.User;
import service.UserService;

@Controller
@RequestMapping("/personal")
public class PersonalController {

	@Autowired
	private UserService userService;
	
	@RequestMapping("/index")
	public String index()
	{
		return "personal/index";
	}
	
	@RequestMapping("/edit-submit")
	public String submitEditPersonal(HttpServletRequest request,
			@RequestParam(value="headericon", required=false)String headericon,
			@RequestParam(value="gender", required=false)Integer gender,
			@RequestParam(value="nickname", required=false)String nickname)
	{
		User user=(User)request.getSession().getAttribute("mem");
		
		user=this.userService.editUserByBMID(user.getBM_ID(), headericon, nickname, gender);
		
		request.getSession().setAttribute("mem", user);
		
		return "redirect:/personal/index";
	}
}
