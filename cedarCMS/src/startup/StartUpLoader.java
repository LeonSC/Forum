package startup;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

public class StartUpLoader extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6633712045797179941L;
	
	public void init(ServletConfig conf)
	{
		System.out.println("===============================启动项目参数=================================");
		ServletContext application=conf.getServletContext();
		application.setAttribute("config", Config.getConfig());
		System.out.println("==========================项目参数启动======================================");
		
		System.out.println("===============================启动mongodb=================================");
		MongoDBConnector.getMongoDBConnector();
		System.out.println("===============================启动mongodb结束=================================");
		
		System.out.println("===============================装载超级用户=================================");
		SuperAdminConfig.getMap();
		System.out.println("==========================装载超级用户结束======================================");
	}

}
