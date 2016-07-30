package startup;

import java.util.Arrays;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;

public final class MongoDBConnector {
	
	private static MongoDBConnector mbdc;
	public static Datastore datastore=null;
	
	private MongoDBConnector(){}
	
	public static MongoDBConnector getMongoDBConnector()
	{
		if(MongoDBConnector.mbdc==null)
		{
			MongoDBConnector.mbdc=new MongoDBConnector();
			
			//mongodb
			MongoDBConnector.mbdc.getDB();
			
			return MongoDBConnector.mbdc;
		}
		
		return MongoDBConnector.mbdc;
	}

	private void getDB()
	{
		try {
			MongoClientOptions options = MongoClientOptions.builder()
					.connectionsPerHost(3000)
					.threadsAllowedToBlockForConnectionMultiplier(10)
					.readPreference(ReadPreference.nearest())
					.build();
			
			MongoCredential credential = MongoCredential.createCredential(Config.dbUser, Config.dbBase, Config.dbPassword.toCharArray());
			
			MongoClient mongoClient = new MongoClient(Arrays.asList(new ServerAddress(Config.dbIP, Config.dbPort)), Arrays.asList(credential),options);
			
			final Morphia morphia = new Morphia();
			
			morphia.mapPackage("model");
			
			MongoDBConnector.datastore = morphia.createDatastore(mongoClient, Config.dbBase);
			MongoDBConnector.datastore.ensureIndexes();
			
			//MongoDBConnector.db = mongoClient.getDatabase("cedar_forum");
			return;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
