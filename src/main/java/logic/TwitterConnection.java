package logic;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterConnection {
	
	private static TwitterConnection singleton;
	
	private Twitter twitter;

	private TwitterConnection() {
		singleton = null;
		
		twitter = TwitterFactory.getSingleton();
	}

	public static TwitterConnection getInstance() {
		if (singleton == null) {
			singleton = new TwitterConnection();
		}
		
		return singleton;
	}
	
	public void updateStatus(String message) throws TwitterException {
		twitter.updateStatus(message);
	}
}
