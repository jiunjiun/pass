package jiunling.config;

public class config_sample {

	private static final int Second = 1000;
	public static final int SleepTime = 30 * Second;
	
	/***	wifi ¥\²v ¶V¤p¶V®t	***/
	public static final int Wifi_Level = 0;
	
	
	/***	Google Colud Messaging for Android (GCM)	***/
	/**
     * Google API project id registered to use GCM.
     */
    public static final String SENDER_ID = "your_sender_id";
    
    /**
     * Intent used to display a message in the screen.
     */
    public static final String DISPLAY_MESSAGE_ACTION = "com.google.android.gcm.demo.app.DISPLAY_MESSAGE";

    /**
     * Intent's extra that contains the message to be displayed.
     */
    public static final String EXTRA_MESSAGE = "your_message_key";
    
    public static String UserRegistrarId = null;
    
    /*******************************************************/
    
    public static boolean havaRoot = false;

}
