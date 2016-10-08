package service;

import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.SynchroDao;
import model.Page;
import model.Synchro;
import model.TempSynCheckKey;
import startup.ForumCache;
import tools.Tools;

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
		
		//ip去掉所有空格
		ip=ip.replaceAll(" ", "");
		
		//验证IP合法性
		Pattern pattern=Pattern.compile("^(0|[1-9]?|1\\d\\d?|2[0-4]\\d|25[0-5])\\.(0|[1-9]?|1\\d\\d?|2[0-4]\\d|25[0-5])\\.(0|[1-9]?|1\\d\\d?|2[0-4]\\d|25[0-5])\\.(0|[1-9]?|1\\d\\d?|2[0-4]\\d|25[0-5]):(\\d{1,9})(/([A-Za-z]+))?$");
		
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
	
	/**
	 * 更新同步锁,只有在同步锁合法的前提下会触发更新
	 * 触发远程调用
	 */
	public int renewSynKeyAndRelord()
	{
		String key=this.synchroDao.saveOrUpdateTempSynCheckKey();
		
		List<Synchro> list=this.synchroDao.find();
		
		for(Synchro syn:list)
		{
			try {
				URL realUrl = new URL(new StringBuffer("http://").append(syn.getIpAddress()).append("/synchro/reload/").append(key).toString());
				URLConnection connection = realUrl.openConnection();
				connection.connect();
			} catch (Exception e) {
				continue;
			}
			
		}
		
		return 0;
	}
	
	/**
	 * 触发论坛结构重建
	 * @param timing
	 * @return
	 */
	public int relordForumTitleTriger(String key)
	{
		//随机key对应
		TempSynCheckKey tsck=this.synchroDao.findTempSynCheckKey();
		
		if(tsck==null||tsck.getKey()==null||!tsck.getKey().equals(key))
		{
			return -1;
		}
		
		//计算时间限制
		Long now=Tools.getServerTime();
		
		Long rest=now-tsck.getLimit();
		
		if(rest>300000)
		{
			return -2;
		}
		
		//重建缓存
		ForumCache.getCache().reBuildForumTitleCache();
		
		return 0;
	}
}
