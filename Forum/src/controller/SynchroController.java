package controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.SynchroService;

@Controller
@RequestMapping("/synchro")
public class SynchroController {

	@Autowired
	private SynchroService synchroService;
	
	@ResponseBody
	@RequestMapping("/check")
	public String checkConnection(HttpServletResponse response)
	{
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET");
		response.setHeader("Access-Control-Max-Age", "100");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		
		return "ok";
	}
	
	@ResponseBody
	@RequestMapping("/reload/{key}")
	public String reloadForumTitleCache(@PathVariable String key)
	{
		this.synchroService.relordForumTitleTriger(key);
		
		return "ok";
	}
}
