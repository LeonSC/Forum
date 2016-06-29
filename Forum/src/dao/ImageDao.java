package dao;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import model.Image;
import startup.MongoDBConnector;

@Repository
public class ImageDao {

	public Image save(Image image)
	{
		if(image==null)
		{
			image=new Image();
			image.setBM_DEL(-1);
			return image;
		}
		
		if(image.getName()==null)
		{
			image=new Image();
			image.setBM_DEL(-2);
			return image;
		}
		
		if(image.getUsedin()==null)
		{
			image=new Image();
			image.setBM_DEL(-3);
			return image;
		}
		
		if(image.getOuterkey()==null)
		{
			image=new Image();
			image.setBM_DEL(-4);
			return image;
		}
		
		MongoDBConnector.datastore.save(image);
		ObjectId id = image.getId();
		return MongoDBConnector.datastore.get(Image.class,id);
	}
	
}
