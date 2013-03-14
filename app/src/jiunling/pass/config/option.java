package jiunling.pass.config;

public class option {
	/***	root 	***/
    public static boolean havaRoot 						= false;
    
	/***	wifi ¥\²v ¶V¤p¶V®t	***/
	public static final int Wifi_Level 					= -70;
	
	/**		Wifi setting	**/
	public static boolean WifiScan						= true;
	public static boolean NotificationUser				= false;
	
	private static final int Second 					= 1000;
	public static int UpdateTime 						= 30;
	public static int SleepTime							= UpdateTime * Second;
	
	
	/**				hide			**/
	/**		Wifi public(hide get)	**/
	public static int pubUpdateTime 					= 60;
	public static int pubSleepTime						= pubUpdateTime * Second;
	
}
