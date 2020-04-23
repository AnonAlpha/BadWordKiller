package com.snowk.badWordDetector.listener;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.snowk.badWordDetector.BadWordDetector;
import com.snowk.badWordDetector.Message;
import com.snowk.badWordDetector.util.SensitivewordFilter;

public class ChatListener implements Listener {
		
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {

		String inputMsg = e.getMessage();
		
		// init and stats
		boolean isSensitive = false; 
		boolean printLog = true;
		SensitivewordFilter filter = new SensitivewordFilter();
		String logger_1 = "��7[��c!��7] ��d��lBadWordKiller ��ʼ��� ";
		String logger_2 = "��7[��a!��7] ��3���дʿ�������" + filter.sensitiveWordMap.size();
		
		//=============================================================================================================
		// ������� ��������   -  �����������֡����ţ����ң�    ��fu��ck��123123#@���� ��������inputMsg��ֻ���滻�� Ŀ�ģ��ҳ� fuck ������
		String regEx0 = "[a-zA-Z\\u4e00-\\u9fa5]";
		Pattern p = Pattern.compile(regEx0); 
		Matcher m  = p.matcher(inputMsg);
		StringBuffer finalStr = new StringBuffer();
		while(m.find()){
			finalStr.append(m.group());
		}
		// ���ڴ�����ַ��� �����ڼ�⵽���дʺ�ʹ�á�
		String buffString_1 = finalStr.toString(); 
		
		// ���ж����Դ��ڲ�ͬ���ϵ����ã�������һ�����Դ���������� �����ں��Ը��ź���ڵĵ����ַ��� �ų�����     �����ų����ٳ�
		if (buffString_1.length()==1) {
			String regExLvl_0 = "[�ٸ���]";
	        Pattern p0 = Pattern.compile(regExLvl_0);
	        Matcher m0 = p0.matcher(inputMsg);
	        buffString_1 = m0.replaceAll(Message.maskSymbol).trim();
		}
		String logger_3 = "��7[��e!��7] ��6���ڴ����ַ�������һ������󣩣�" + buffString_1;
		
		// ȫ����ּ�� - DFA Cyc-1
		Set<String> set = filter.getSensitiveWord(buffString_1.toLowerCase(), 1);
        if (Message.replaceEnable) {
    		if (!set.isEmpty()) { // ���о䴦���滻
    			isSensitive = true;
    		}
        } else {
    		if (!set.isEmpty()) {  // ���о䴦��ɾ��
    			e.getPlayer().sendMessage(Message.msg_Reject);
    			e.setCancelled(true);
    			return;
    		}
        }
		String logger_4 = "��7[��c!��7] ��a��l��һ�����дʣ� ��3" + set;
        String logger_5 = "��7[��e!��7] ��6���ڴ����ַ�����DFA-1�����ĵ�ɸ��" + buffString_1;
        
        if (!isSensitive) { // ��DFA-1�ֽ������������У��ָ�ԭ�䡿
        	buffString_1 = inputMsg;
        } else { // �����о�ǿ���滻�������дʣ�����ȡ�
	        String regExLvl_3 = "[�ȱұƱʵ������ү�̶��������������ȱ��˹���Ѽ��a-zA-Z&\\&0-9]";
	        Pattern p3 = Pattern.compile(regExLvl_3);
	        Matcher m3 = p3.matcher(buffString_1);
	        buffString_1 = m3.replaceAll("").trim();
        }
        //==========================================================================================================
		// ������� �ڶ������   -  ��������Ӣ�ġ����֡����ţ����ң�    �ݡ�asd@ #��1-2sadefvse3�� ��������inputMsg��ֻ���滻�� Ŀ�ģ��ҳ�  ������
		String regEx1 = "[\\u4e00-\\u9fa5]";
		Pattern p1 = Pattern.compile(regEx1); 
		Matcher m1  = p1.matcher(inputMsg);
		StringBuffer finalStr2 = new StringBuffer();
		while(m1.find()){
			finalStr2.append(m1.group());
		}
		// ���ڴ�����ַ��� �����ڼ�⵽���дʺ�ʹ�á�
		buffString_1 = finalStr2.toString(); 
		String logger_6 = "��7[��e!��7] ��6���ڴ����ַ������ڶ�������󣩣�" + buffString_1;
		
		// ȫ����ּ�� - DFA Cyc-2
		SensitivewordFilter filter2 = new SensitivewordFilter();
		Set<String> set2 = filter2.getSensitiveWord(buffString_1.toLowerCase(), 1);
		
        if (Message.replaceEnable) {
    		if (!set2.isEmpty()) { // ���о䴦���滻
    	        isSensitive = true;
    			// ������һ���滻�������滻ȫ�У����滻��Ӣ���
    	        for(String x: set2) {
    	        	buffString_1 = ignoreCaseReplace(buffString_1,x, Message.maskSymbol);
    	        }
    	        for(String y: set) {
    	        	buffString_1 = ignoreCaseReplace(buffString_1,y, Message.maskSymbol);
    			}
    		} 
        } else {
    		if (!set2.isEmpty()) {  // ���о䴦��ɾ��
    			e.getPlayer().sendMessage(Message.msg_Reject);
    			e.setCancelled(true);
    			return;
    		}
    		
        }  
        String logger_7 = "��7[��c!��7] ��a��l�ڶ������дʣ� ��3" + set2;
        String logger_8 = "��7[��e!��7] ��6���ڴ����ַ�����DFA-2����Ӣ��ɸ��" + buffString_1;
        
        //===========================================================================================
		// ��� ȫ�䵥�ּ��
    	if (!isSensitive) { 				// �����о�  ��ԭʼ���������
    		isSensitive = doCharCheck(isSensitive,e,inputMsg);
        } else { 							// ���о�  ��ǿ��ɾ�����ŷ����������д�֮��ľ��������
    	    String regExLvl_3 = "[�Ʊȱұʵ������ү�̶��������������ȱ���a-zA-Z&\\&0-9]";
    	    Pattern p3 = Pattern.compile(regExLvl_3);
    	    Matcher m3 = p3.matcher(buffString_1);
    	    buffString_1 = m3.replaceAll("").trim();
        	isSensitive = doCharCheck(isSensitive,e,buffString_1);
   		}
    	String logger_9 = "��7[��e!��7] ��6���ڴ����ַ�����DFA-�������ֵ�ɸ��" + buffString_1;
    	
    	if (buffString_1.length()==0) { //��ǿ���滻����ⷢ������Ϣ
    		buffString_1 = Message.maskSymbol;
    	}
    	
    	String logger_10 = "��7[��4!��7] ��c��l�������о��жϽ������a��l" + isSensitive;
    	
    	// output for debugging
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
	
	private boolean doCharCheck(boolean isSensitive, AsyncPlayerChatEvent e, String pMessage) {
		if (Message.replaceEnable) {
   			for(String charBan: BadWordDetector.banCharList) {
   				if (pMessage.contains(charBan)) {
   					isSensitive = true;
   					pMessage = ignoreCaseReplace(pMessage,charBan, Message.maskSymbol);	
   				}
   				
   			}
   			e.setMessage(pMessage);
   		} else {
   	        for(String charBan: BadWordDetector.banCharList) {
   	        	if (pMessage.contains(charBan)) {
   	        		isSensitive = true;
   	        		e.setCancelled(true);
   	        	}
   	        }
   	        e.getPlayer().sendMessage(Message.msg_Reject);
   		}
   		return isSensitive;
	}
	
	public static String ignoreCaseReplace(String source, String oldstring,String newstring){
		Pattern p = Pattern.compile(oldstring, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(source);
		String ret = m.replaceAll(newstring);
		return ret;
	}
}
