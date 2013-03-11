package jiunling.wifi;

/*參考：http://www.cnblogs.com/fly_binbin/archive/2010/12/21/1913230.html*/

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.util.Log;

public class WifiHelper {
	private final boolean D = false;
    private final String TAG = this.getClass().getSimpleName();

    private WifiManager wifiManager;// 聲明管理對象

    private WifiInfo wifiInfo;// Wifi信息

    private List<ScanResult> scanResultList; // 掃描出來的網絡連接列表
    private List<WifiConfiguration> wifiConfigList;// 網絡配置列表

    private WifiLock wifiLock;// Wifi鎖
    
    //wifiConnect status
    public static final byte WifiCipher_NOPASSWD 	= 1;
	public static final byte WifiCipher_WEP 		= 2;
	public static final byte WifiCipher_WPA 		= 3;

    public WifiHelper(Context context) {
        this.wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);// 獲取Wifi服務
        // 得到Wifi信息
        this.wifiInfo = wifiManager.getConnectionInfo();// 得到連接信息

    }

    /**
     * 獲得wifi狀態
     * 
     * @return true if Wi-Fi is enabled
     */
    public boolean getWifiStatus() {
        return wifiManager.isWifiEnabled();
    }

    /** 打開 wifi */
    public boolean openWifi() {
        if (!wifiManager.isWifiEnabled()) {
            try {
                return wifiManager.setWifiEnabled(true);
            } catch (SecurityException e) {
                return false;
            }
        } else {
            return false;
        }

    }
    
    

    /** 關閉 wifi */
    public boolean closeWifi() {
        if (!wifiManager.isWifiEnabled()) {
            return true;
        } else {
            try {
                return wifiManager.setWifiEnabled(false);
            } catch (SecurityException e) {
                return false;
            }
        }
    }

    /**
     * 鎖定wifi。 Locks the Wi-Fi radio on until release is called. If this
     * WifiLock is reference-counted, each call to acquire will increment the
     * reference count, and the radio will remain locked as long as the
     * reference count is above zero. If this WifiLock is not reference-counted,
     * the first call to acquire will lock the radio, but subsequent calls will
     * be ignored. Only one call to release will be required, regardless of the
     * number of times that acquire is called.
     */
    public void lockWifi() {
        // 其實鎖定WiFI就是判斷wifi是否建立成功，在這裡使用的是held，握手的意思acquire 得到！
        // 參考：http://www.asiteof.me/2011/02/wakelock-wifilock/
        if (wifiLock == null) {
            wifiLock = wifiManager.createWifiLock("flyfly"); // 創建一個鎖的標誌
        }
        wifiLock.acquire();

    }

    /** 解鎖wifi */
    public void unLockWifi() {
        if (!wifiLock.isHeld()) {
            wifiLock.release(); // 釋放資源
        }
    }

    /** 掃描網絡 */
    public void startScan() {
        if (wifiManager.startScan()) {
            scanResultList = wifiManager.getScanResults(); // 掃描返回結果列表
            wifiConfigList = wifiManager.getConfiguredNetworks(); // 掃描配置列表
        } else {
            if(D) Log.e(TAG, "wifiManager.startScan().. The scan was initiated.");
        }

    }

    /** 返回結果列表 */
    public List<ScanResult> getWifiList() {
        return scanResultList;
    }

    /** 返回配置列表 */
    public List<WifiConfiguration> getWifiConfigList() {
    	wifiConfigList = wifiManager.getConfiguredNetworks(); // 掃描配置列表
        return wifiConfigList;
    }

    /**
     *  獲得頻段標識符號，範圍為1-14。
     * 信道标识符、频率（单位：MHz）：（1、2412），（2、2417），（3、2422），（4、2427），（5、2432），（6、2437），（7
     * 、2442），（8、2447） （9、2452），（10、2457）
     * ，（11、2462），（12、2467），（13、2472），（14、2484）
     */
    public int getWifiFrequency(ScanResult mScanResult){
        int frequency=mScanResult.frequency;
        return (frequency-2412)/5+1;
    }
    
    
    /**
     * 獲取掃描列表
     * 
     * @return 返回一個StringBuilder值，格式為StringBuilder
     */
    public StringBuilder lookUpscan() {
        StringBuilder scanBuilder = new StringBuilder();

        for (int i = 0; i < scanResultList.size(); i++) {
            scanBuilder.append("編號：" + (i + 1));
            scanBuilder.append(scanResultList.get(i).toString()); // 所有信息
            scanBuilder.append("\n");
        }

        return scanBuilder;
    }

    /** 獲取指定信號的強度 */
    public Integer getScanResultListLevel(int NetId) {
        return scanResultList.get(NetId).level;
    }

    /** 獲取本機Mac地址 */
    public String getMac() {
        return (wifiInfo == null) ? null : wifiInfo.getMacAddress();
    }

    /** 獲取當前連接的BSSID */
    public String getBSSID() {
        return (wifiInfo == null) ? null : wifiInfo.getBSSID();
    }

    /** 獲取當前連接的SSID */
    public String getSSID() {
        return (wifiInfo == null) ? null : wifiInfo.getSSID();
    }

    /** 返回當前連接的網絡的ID */
    public Integer getCurrentNetId() {
        return (wifiInfo == null) ? null : wifiInfo.getNetworkId();
    }

    /** 返回所有信息 */
    public WifiInfo getWifiInfo() {
        return this.wifiInfo;
    }

    /** 獲取IP地址 */
    public Integer getIP() {
        return (wifiInfo == null) ? null : wifiInfo.getIpAddress();
    }

    /** 獲得當前連接的Ip address */
    public String getLocalIpAddress() {
        // 參考：http://lovezhou.iteye.com/blog/945880
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            if(D) Log.e(TAG, ex.toString());
        }
        return null;
    }

    // -----不知道怎麼用......
    /** 添加一個連接 */
    public Boolean addNetWordLink(WifiConfiguration config) {
        int NetId = wifiManager.addNetwork(config);
        if(D) Log.e(TAG, " add NetId "+NetId);
        return wifiManager.enableNetwork(NetId, true);
    }
       
    /** 更新一個連接 */
    public Boolean updateNetWordLink(WifiConfiguration config) {
        int NetId = wifiManager.updateNetwork(config);
        return wifiManager.enableNetwork(NetId, true);
    }
    
    /** 更新一個連接以NetId */
    public Boolean updateNetWordLinkNetId(int NetId) {
        return wifiManager.enableNetwork(NetId, true);
    }

    /** 禁用一個鏈接 */
    public Boolean disableNetWordLick(int NetId) {
        wifiManager.disableNetwork(NetId);
        return wifiManager.disconnect();
    }

    /** 移除一個鏈接 */
    public Boolean removeNetworkLink(int NetId) {
        return wifiManager.removeNetwork(NetId);
    }

    /** 不顯示SSID */
    public void hiddenSSID(int NetId) {
        wifiConfigList.get(NetId).hiddenSSID = true;
    }

    /** 顯示SSID */
    public void displaySSID(int NetId) {
        wifiConfigList.get(NetId).hiddenSSID = false;
    }
    // -----不知道怎麼用......
    
    /** 取得目前網路的 WifiConfiguration	**/
    public WifiConfiguration getWifiConfiguration(){
		WifiConfiguration nowWifiConfiguration = null;
		for (WifiConfiguration mWifiConfiguration : getWifiConfigList()) {
			if(mWifiConfiguration.SSID.equals("\""+getSSID()+"\"")){
    			nowWifiConfiguration = mWifiConfiguration;
    		}
		}
		return nowWifiConfiguration;
	}
    
    /**		尋找指定的Wifi	**/
	public boolean SearchWifi(String SSID){
		
		/**		開始搜尋		**/
		startScan();
		
		/**		判斷目前有沒有指定的ap		**/
		boolean CheckSpecifiedWifi = false;
		for (ScanResult mScanResult : getWifiList()) {
    		if(mScanResult.SSID.equals(SSID)){
    			CheckSpecifiedWifi = true;
    			break;
    		}
        }
		
		return CheckSpecifiedWifi;
	}
	
	/**		判斷在wifi list 有沒有指定ap		**/
	public int SearchWifiList(String SSID){
		int NetId = -1;
		for (WifiConfiguration mWifiConfiguration : getWifiConfigList()) {
    		if(mWifiConfiguration.SSID.equals("\""+SSID+"\"")){
    			NetId = mWifiConfiguration.networkId;
				break;
			}
		}
		return NetId;
	}
    
    
    /**		依不同類型的AP建立WifiConfiguration 	**/
    public WifiConfiguration ConnectWifi(String SSID , String PASSWD , byte WifiCipherStatus){
    	WifiConfiguration wc = new WifiConfiguration();
    	
    	wc.allowedAuthAlgorithms.clear();  
    	wc.allowedGroupCiphers.clear();  
    	wc.allowedKeyManagement.clear();  
    	wc.allowedPairwiseCiphers.clear();  
        wc.allowedProtocols.clear();  
        
        wc.SSID = "\""+ SSID +"\"";

		switch(WifiCipherStatus){
		case WifiCipher_NOPASSWD:
			/*** wifi is not passwd ***/
			wc.wepKeys[0] = "";
			wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			wc.wepTxKeyIndex = 0;
			break;
			
		case WifiCipher_WEP:
			/*** wifi is WEP ***/
			wc.preSharedKey = "\""+PASSWD+"\""; 
			wc.hiddenSSID = true;  
			wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
			wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			wc.wepTxKeyIndex = 0;
			break;
			
		
		case WifiCipher_WPA:
			/*** wifi is WPA ***/
			wc.preSharedKey = "\""+ PASSWD +"\"";
			wc.hiddenSSID 	= true;
			wc.status 		= WifiConfiguration.Status.ENABLED; 
			wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
//			wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
			break;
	
		}
		
		return wc;			
    }
}
