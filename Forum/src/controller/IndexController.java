package controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import service.AdminService;
import service.UserService;

import model.User;
import startup.Config;

@Controller
public class IndexController {
	
	@Autowired
	private AdminService adminService;
	@Autowired
	private UserService userService;
	
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request)
	{
		return "index";
	}
	
	@RequestMapping("/building")
	public String building()
	{
		return "building";
	}
	
	/////////////////////////// 普通会员登录////////////////////////////////////////
	@RequestMapping("/memlogin")
	public String memLogin(HttpServletRequest request,
			@RequestParam(value = "goback", required = false) String goback) {
		// 优先级, goback优先
		if (goback != null) {
			request.getSession().setAttribute("recentView", goback);
			return "memlogin";
		}

		return "memlogin";
	}

	@RequestMapping("/memloginsubmit")
	public String memLoginSubmit(HttpServletRequest request,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "pw", required = false) String pw) {
		User u = this.userService.checkUser(email, pw);

		if (u == null) {
			request.setAttribute("error_wrongpw", "wrongpw");
			return "memlogin";
		}

		request.getSession().setAttribute("mem", u);

		// 如果为非首页需求登录
		Object path = request.getSession().getAttribute("recentView");
		request.getSession().setAttribute("recentView", "");// 用完后清理
		if (path != null) {
			return "redirect:" + Config.getConfig().getRootPath() + path.toString();
		}

		return "redirect:/index";
	}

	/**
	 * 登出
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/memlogoutsubmit")
	public String memLogoutSubmit(HttpServletRequest request,
			@RequestParam(value = "goback", required = false) String goback) {
		request.getSession().setAttribute("mem", null);

		// 优先级, goback优先
		if (goback != null) {
			return "redirect:" + Config.getConfig().getRootPath() + goback;
		}

		return "redirect:/index";
	}

	@RequestMapping("/memregister")
	public String memRegister(HttpServletRequest request) {
		return "memregister";
	}

	@RequestMapping("/memregistersubmit")
	public String memRegisterSubmit(@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "pw", required = false) String pw) {
		this.userService.registerUser(email, pw);

		return "redirect:/memlogin";
	}

	////////////////// 处理管理员///////////////////////////
	@RequestMapping("/adminconsole")
	public String adminLogin() {
		return "admin/login";
	}

	@RequestMapping("/adminlogin-submit")
	public String adminLoginSubmit(HttpServletRequest request,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "pw", required = false) String pw) {
		User adm = this.adminService.checkAdmin(email, pw);
		if (adm == null) {
			return "redirect:/adminconsole";
		}

		request.getSession().setAttribute("admin", adm);

		return "redirect:/admin/index";
	}

	@RequestMapping("/adminlogout")
	public String adminLogout(HttpServletRequest request) {
		request.getSession().setAttribute("admin", null);
		return "redirect:/admin";
	}
}
