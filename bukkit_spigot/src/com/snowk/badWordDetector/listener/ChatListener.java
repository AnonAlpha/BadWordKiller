package com.snowk.badWordDetector.listener;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	String REGEX_4 = "[�ȱұƱʵ������ү�̶��������������ȱ��˹���Ѽ����a-zA-Z&\\&0-9]"; // ���Դ� 2�� [�ų�����/��������]
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {

		String inputMsg = e.getMessage(); // ԭʼ�ַ������������дʣ��򲻿ɱ䶯

		/**
		 * ��ʼ��
		 * @Description: Initialization and Statistics
		 */
		
		boolean isSensitive = false; 
		boolean printLog = true;
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
    			e.getPlayer().sendMessage(ConfigHandler.msg_Reject);
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
    			e.getPlayer().sendMessage(ConfigHandler.msg_Reject);
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
    	
        /**
		 * Log Print for Debugging
		 * @Description:  Log Print for Debugging
		 */
        
    	if (printLog) {
    		e.getPlayer().sendMessage(logger_1);
    		e.getPlayer().sendMessage(logger_2);
    		e.getPlayer().sendMessage(logger_3);
    		e.getPlayer().sendMessage(logger_4);
    		e.getPlayer().sendMessage(logger_5);
    		e.getPlayer().sendMessage(logger_6);
    		e.getPlayer().sendMessage(logger_7);
    		e.getPlayer().sendMessage(logger_8);
    		e.getPlayer().sendMessage(logger_9);
    		e.getPlayer().sendMessage(logger_10);
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
