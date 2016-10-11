package controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import model.User;
import service.PersonalService;

@Controller
@RequestMapping("/personal")
public class PersonalController {

	@Autowired
	private PersonalService personalService;
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request)
	{
		User user=(User)request.getSession().getAttribute("mem");
		
		request.setAttribute("topicpage", this.personalService.findUserTopic(user.getBM_ID(), 1));
		
		return "personal/index";
	}
	
	@RequestMapping("/edit-submit")
	public String submitEditPersonal(HttpServletRequest request,
			@RequestParam(value="headericon", required=false)String headericon,
			@RequestParam(value="gender", required=false)Integer gender,
			@RequestParam(value="nickname", required=false)String nickname)
	{
		User user=(User)request.getSession().getAttribute("mem");
		
		user=this.personalService.editUserByBMID(user.getBM_ID(), headericon, nickname, gender);
		
		request.getSession().setAttribute("mem", user);
		
		return "redirect:/personal/index";
	}
	
	/**
	 * 用户发布的主题
	 * @param request
	 * @return
	 */
	@RequestMapping("/mytopic")
	public String myTopic(HttpServletRequest request)
	{
		User user=(User)request.getSession().getAttribute("mem");
		
		request.setAttribute("topicpage", this.personalService.findUserTopic(user.getBM_ID(), 1));
		
		return "personal/mytopic";
	}
	
	/**
	 * 用户的回答
	 * @param request
	 * @return
	 */
	@RequestMapping("/mycontent")
	public String myContent(HttpServletRequest request)
	{
		User user=(User)request.getSession().getAttribute("mem");
		
		request.setAttribute("answerpage", this.personalService.findUserAnswer(user.getBM_ID(), 1));
		
		return "personal/myanswer";
	}
	
	
}
