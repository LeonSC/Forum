package service;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.SynchroDao;
import model.Page;
import model.Synchro;

@Service
public class SynchroService {

	@Autowired
	private SynchroDao synchroDao;
	
	/**
	 * 保存
	 * @param ip
	 * @param alias
	 * @return
	 */
	public Synchro save(String ip,String alias)
	{
		if(ip==null||alias==null)
		{
			return null;
		}
		
		//验证IP合法性
		Pattern pattern=Pattern.compile("^(0|[1-9]?|1\\d\\d?|2[0-4]\\d|25[0-5])\\.(0|[1-9]?|1\\d\\d?|2[0-4]\\d|25[0-5])\\.(0|[1-9]?|1\\d\\d?|2[0-4]\\d|25[0-5])\\.(0|[1-9]?|1\\d\\d?|2[0-4]\\d|25[0-5]):(\\d{1,9})$");
		
		//验证合法性
		if(!pattern.matcher(ip).matches())
		{
			return null;
		}
		
		Synchro syn=new Synchro();
		
		syn.setIpAddress(ip);
		syn.setAlias(alias);
		
		return this.synchroDao.save(syn);
	}
	
	/**
	 * 查询前20条集群信息
	 * @param nowPage
	 * @return
	 */
	public Page<Synchro> findLimit(Integer nowPage)
	{
		if(nowPage==null)
		{
			nowPage=1;
		}
		
		return this.synchroDao.find(nowPage, 20);
	}
}
