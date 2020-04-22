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
		
		String intputMsg = e.getMessage().toLowerCase();

		// �������
		String regEx="[a-zA-Z\\u4e00-\\u9fa5]"; 
		Pattern p = Pattern.compile(regEx); 
		Matcher m  = p.matcher(intputMsg);
		StringBuffer finalStr = new StringBuffer();
		while(m.find()){
			finalStr.append(m.group());
		}
		
		// ���ּ�� - DFA
		SensitivewordFilter filter = new SensitivewordFilter();
		String finalfinal = finalStr.toString();
		Set<String> set = filter.getSensitiveWord(finalfinal, 1);
		
        if (Message.replaceEnable) {
    		if (!set.isEmpty()) {
//    	        e.getPlayer().sendMessage("���дʵ�������" + filter.sensitiveWordMap.size());
//    	        e.getPlayer().sendMessage("����а������дʵĸ���Ϊ�� " + set.size() + "�������� " + set);
    	        for(String x: set) {
    	        	finalfinal = finalfinal.replace(x, Message.maskSymbol);
    			}
    	        // �ǿ���Ϊ������䣬ǿ���滻�������дʣ������
    	        String regExLvl_2 = "[�������ү�̶��������������ȱ���&\\&0-9]";
    	        Pattern p2 = Pattern.compile(regExLvl_2);
    	        Matcher m2 = p2.matcher(finalfinal);
    	        finalfinal = m2.replaceAll("").trim();
    		}
        } else {
    		if (!set.isEmpty()) {
    			e.getPlayer().sendMessage(Message.msg_Reject);
    			e.setCancelled(true);
    		}
        }
        
		// ���ּ��
        if (set.isEmpty()) {
    		if (Message.replaceEnable) {
    			for(String charBan: BadWordDetector.banCharList) {
    				intputMsg = intputMsg.replace(charBan, Message.maskSymbol);
    			}
    			// �������
    			e.setMessage(intputMsg);
    		} else {
    	        for(String charBan: BadWordDetector.banCharList) {
    	        	if (intputMsg.contains(charBan)) {
    	    			e.getPlayer().sendMessage(Message.msg_Reject);
    	    			e.setCancelled(true);
    	        	}
    	        }
    		}
        } else {
    		if (Message.replaceEnable) {
    			for(String charBan: BadWordDetector.banCharList) {
    				finalfinal = finalfinal.replace(charBan, Message.maskSymbol);
    			}
    			// �������
    			e.setMessage(finalfinal);
    		} else {
    	        for(String charBan: BadWordDetector.banCharList) {
    	        	if (finalfinal.contains(charBan)) {
    	    			e.getPlayer().sendMessage(Message.msg_Reject);
    	    			e.setCancelled(true);
    	        	}
    	        }
    		}
		}

		return;
	}
}
