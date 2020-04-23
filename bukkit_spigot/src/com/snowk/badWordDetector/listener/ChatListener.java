package com.snowk.badWordDetector.listener;

import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.snowk.badWordDetector.BadWordDetector;
import com.snowk.badWordDetector.algorithm.SensitivewordFilter;
import com.snowk.badWordDetector.config.ConfigHandler;

public class ChatListener implements Listener {
	
	String REGEX_1 = "[a-zA-Z\\u4e00-\\u9fa5]"; // DFA Cycle 1 RegEx
	String REGEX_2 = "[\\u4e00-\\u9fa5]"; // DFA Cycle 2 RegEx
	String REGEX_3 = "[�ٸ���]";  // ���Դ� 1 �� [�ų�����/��������]
	String REGEX_4 = "[�ȱұƱʵ������ү�̶�������ù�����������ȱ��˹���Ѽ����a-zA-Z&\\&0-9]"; // ���Դ� 2�� [�ų�����/��������]
	String CANCEL_DEBUG = "cancel"; // cancel debug function
	
	/** playerMatcher (ArrayList)
	 *  k: Player Name (String)
	 *  function: save PlayerName when they use /bwk test command,
	 *  		  then for matching them with the next chat.
	 */
	public static ArrayList<String> playerMatcher = new ArrayList<String>();
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		
		Player player = e.getPlayer();
		String inputMsg = e.getMessage(); // ԭʼ�ַ������������дʣ��򲻿ɱ䶯

		/**
		 * ��ʼ��
		 * @Description: Initialization and Statistics
		 */
		
		boolean isSensitive = false; 
		SensitivewordFilter filter = new SensitivewordFilter();
		String logger_1 = "��7[��c!��7] ��d��lBadWordKiller ��ʼ��� ";
		String logger_2 = "��7[��a!��7] ��3���дʿ�������" + filter.sensitiveWordMap.size();
		
		/**
		 * ������� ��������
		 * @Description: �����������֡����ţ����ң�    ��fu��ck��123123#@���� ��������inputMsg��ֻ���滻�� Ŀ�ģ��ҳ� fuck
		 */
		
		String regEx0 = REGEX_1;
		Pattern p = Pattern.compile(regEx0); 
		Matcher m  = p.matcher(inputMsg);
		StringBuffer finalStr = new StringBuffer();
		while(m.find()){
			finalStr.append(m.group());
		}
		String buffString_1 = finalStr.toString(); 	// ���ڴ�����ַ��� �����ڼ�⵽���дʺ�ʹ�á�
		
		/**
		 * ���Դʹ��� 
		 * @Description: �ж����Դ��ڲ�ͬ���ϵ����ã�������һ�����Դ���������� �����ں��Ը��ź���ڵĵ����ַ��� �ų�����     �����ų����ٳ�
		 */
		
		if (buffString_1.length()==1) {
			String regExLvl_0 = REGEX_3;
	        Pattern p0 = Pattern.compile(regExLvl_0);
	        Matcher m0 = p0.matcher(inputMsg);
	        buffString_1 = m0.replaceAll(ConfigHandler.maskSymbol).trim();
		}
		String logger_3 = "��7[��e!��7] ��6���ڴ����ַ�������һ������󣩣�" + buffString_1;

		/**
		 * DFA Cycle-1
		 * @Description: ȫ����ּ��   ��һ�� ����Ҫ���Ӣ�ġ�
		 */
		
		Set<String> set = filter.getSensitiveWord(buffString_1.toLowerCase(), 1);
        if (ConfigHandler.replaceEnable) {
    		if (!set.isEmpty()) { // ���о䴦���滻
    			isSensitive = true;
    		}
        } else {
    		if (!set.isEmpty()) {  // ���о䴦��ɾ��
    			player.sendMessage(ConfigHandler.msg_Reject);
    			e.setCancelled(true);
    			return;
    		}
        }
		String logger_4 = "��7[��c!��7] ��a��l��һ�����дʣ� ��3" + set;
        String logger_5 = "��7[��e!��7] ��6���ڴ����ַ�����DFA-1�����ĵ�ɸ��" + buffString_1;
		
        /**
		 * postProcess DFA Cycle-1 
		 * @Description: �������У��ָ�ԭ�䣻�������У�ǿ���滻�������дʣ�����ȡ�
		 */
        
        if (!isSensitive) { 
        	buffString_1 = inputMsg;
        } else {
	        String regExLvl_3 = REGEX_4;
	        Pattern p3 = Pattern.compile(regExLvl_3);
	        Matcher m3 = p3.matcher(buffString_1);
	        buffString_1 = m3.replaceAll("").trim();
        }
        
		/**
		 * ������� �ڶ������
		 * @Description: ��������Ӣ�ġ����֡����ţ����ң�    �ݡ�asd@ #��1-2sadefvse3�� ��������inputMsg��ֻ���滻�� Ŀ�ģ��ҳ�  ������
		 */
		
		String regEx1 = REGEX_2;
		Pattern p1 = Pattern.compile(regEx1); 
		Matcher m1  = p1.matcher(inputMsg);
		StringBuffer finalStr2 = new StringBuffer();
		while(m1.find()){
			finalStr2.append(m1.group());
		}
		buffString_1 = finalStr2.toString();  // ���ڴ�����ַ��� �����ڼ�⵽���дʺ�ʹ�á�
		String logger_6 = "��7[��e!��7] ��6���ڴ����ַ������ڶ�������󣩣�" + buffString_1;
		
		/**
		 * DFA Cycle-2
		 * @Description: ȫ����ּ��   �ڶ��� ����Ҫ���  ���ġ��Լ���Ӣ�Ļ������дʡ�
		 */
		
		SensitivewordFilter filter2 = new SensitivewordFilter();
		Set<String> set2 = filter2.getSensitiveWord(buffString_1.toLowerCase(), 1);
		
        if (ConfigHandler.replaceEnable) {
    		if (!set2.isEmpty()) { // ���о䴦���滻
    	        isSensitive = true;
    			// ���滻ȫ���ģ����滻��Ӣ��ϣ�������Сʧ��
    	        for(String x: set2) {
    	        	buffString_1 = ignoreCaseReplace(buffString_1,x, ConfigHandler.maskSymbol);
    	        }
    	        for(String y: set) {
    	        	buffString_1 = ignoreCaseReplace(buffString_1,y, ConfigHandler.maskSymbol);
    			}
    		} 
        } else {
    		if (!set2.isEmpty()) {  // ���о䴦��ɾ��
    			player.sendMessage(ConfigHandler.msg_Reject);
    			e.setCancelled(true);
    			return;
    		}
    		
        }  
        String logger_7 = "��7[��c!��7] ��a��l�ڶ������дʣ� ��3" + set2;
        String logger_8 = "��7[��e!��7] ��6���ڴ����ַ�����DFA-2����Ӣ��ɸ��" + buffString_1;
		
        /**
		 * postProcess DFA Cycle-2
		 * @Description:  ȫ�䵥�ּ�飺 ���ҵ���ƥ�䣨�ֲ�DFA�㷨�����ԣ�
		 */
        
    	if (!isSensitive) { 				// �����о�  ��ԭʼ���������
    		isSensitive = doCharCheck(isSensitive,e,inputMsg);
        } else { 							// ���о�  ��ǿ��ɾ�����ŷ����������д�֮��ľ��������
    	    String regExLvl_3 = REGEX_4;
    	    Pattern p3 = Pattern.compile(regExLvl_3);
    	    Matcher m3 = p3.matcher(buffString_1);
    	    buffString_1 = m3.replaceAll("").trim();
        	isSensitive = doCharCheck(isSensitive,e,buffString_1);
   		}
    	String logger_9 = "��7[��e!��7] ��6���ڴ����ַ�����DFA-�������ֵ�ɸ��" + buffString_1;
    	
    	if (buffString_1.length()==0) { //��ǿ���滻����ⷢ������Ϣ
    		buffString_1 = ConfigHandler.maskSymbol;
    	}
    	
    	String logger_10 = "��7[��4!��7] ��c��l�������о��жϽ������a��l" + isSensitive;
    	String logger_11 = "��7[��4!��7] ��c��l�������о��жϽ������c��l" + isSensitive;
    	
        /**
		 * Log Print for Debugging
		 * @Description:  Log Print for Debugging
		 */
    	
    	if (playerMatcher.contains(player.getName())) {
    		if (e.getMessage().equalsIgnoreCase(CANCEL_DEBUG)) {
            	playerMatcher.remove(player.getName());
            	e.setCancelled(true);
            	player.sendMessage("��7[��c!��7] ��d��lBadWordKiller debug�����ѹرգ�");
            	return;
            } 
    		player.sendMessage(logger_1);
    		player.sendMessage(logger_2);
    		player.sendMessage(logger_3);
    		player.sendMessage(logger_4);
    		player.sendMessage(logger_5);
    		player.sendMessage(logger_6);
    		player.sendMessage(logger_7);
    		player.sendMessage(logger_8);
    		player.sendMessage(logger_9);
    		if (isSensitive) {
        		player.sendMessage(logger_10);
    		} else {
        		player.sendMessage(logger_11);
    		}
    		player.sendMessage("��7[��c!��7] ��cBadWordKiller ��adebug������ϣ� ��e���� ��ccancel ��e�˳����");
    		player.sendMessage("");
    		e.setCancelled(true);
    	}
	}
	
	/**
	 * ���ҵ���ƥ�䣨�ֲ�DFA�㷨�����ԣ�
	 * @Description: Check the single Chinese letter which is highly aggressive (fix DFA)
	 */
	private boolean doCharCheck(boolean isSensitive, AsyncPlayerChatEvent e, String pMessage) {
		if (ConfigHandler.replaceEnable) {
   			for(String charBan: BadWordDetector.banCharList) {
   				if (pMessage.contains(charBan)) {
   					isSensitive = true;
   					pMessage = ignoreCaseReplace(pMessage,charBan, ConfigHandler.maskSymbol);	
   				}
   				
   			}
   			e.setMessage(pMessage);
   		} else {
   	        for(String charBan: BadWordDetector.banCharList) {
   	        	if (pMessage.contains(charBan)) {
   	        		isSensitive = true;
   	        		e.getPlayer().sendMessage(ConfigHandler.msg_Reject);
   	        		e.setCancelled(true);
   	        		break;
   	        	}
   	        }
   		}
   		return isSensitive;
	}
	
	/**
	 * ���Դ�Сд�����ı��滻
	 * @Description: Replace string without case sensitive
	 */
	public static String ignoreCaseReplace(String source, String oldstring,String newstring){
		Pattern p = Pattern.compile(oldstring, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(source);
		String ret = m.replaceAll(newstring);
		return ret;
	}
}
