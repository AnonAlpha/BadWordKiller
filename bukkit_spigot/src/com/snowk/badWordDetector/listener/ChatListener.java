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
		doDFA(e, "[a-zA-Z\\u4e00-\\u9fa5]", false); // ��������������ֺͷ��ţ��ų��ݡ�@ #��123��
		doDFA(e, "[\\u4e00-\\u9fa5]", true); // �����������Ӣ�ģ������������һ�μ�飬�ų�  ��as��as��
		return;
	}
	
	private void doDFA(AsyncPlayerChatEvent e, String regEx, boolean doCharCheck) {

		String intputMsg = e.getMessage().toLowerCase();
		
		// ������� ��������
		Pattern p = Pattern.compile(regEx); 
		Matcher m  = p.matcher(intputMsg);
		StringBuffer finalStr = new StringBuffer();
		while(m.find()){
			finalStr.append(m.group());
		}
		
		// ���ж����Դ��ڲ�ͬ���ϵ����ã�������һ�����Դ����������
		if (finalStr.toString().length()==1) {
			String regExLvl_0 = "[�ٸ���]";
	        Pattern p0 = Pattern.compile(regExLvl_0);
	        Matcher m0 = p0.matcher(intputMsg);
	        intputMsg = m0.replaceAll(Message.maskSymbol).trim();
		}
		
		// ȫ����ּ�� - DFA
		String finalfinal = finalStr.toString();
		SensitivewordFilter filter = new SensitivewordFilter();
		Set<String> set = filter.getSensitiveWord(finalfinal, 1);
		
        if (Message.replaceEnable) {
    		if (!set.isEmpty()) {
//    	        e.getPlayer().sendMessage("���дʵ�������" + filter.sensitiveWordMap.size());
//    	        e.getPlayer().sendMessage("����а������дʵĸ���Ϊ�� " + set.size() + "�������� " + set);
    	        for(String x: set) {
    	        	finalfinal = finalfinal.replace(x, Message.maskSymbol);
    			}
    	        // �ǿ���ȷ����Ϊ���о䣬ǿ���滻�������дʣ������
    	        String regExLvl_2 = "[�������ү�̶��������������ȱ���&\\&0-9]";
    	        Pattern p2 = Pattern.compile(regExLvl_2);
    	        Matcher m2 = p2.matcher(finalfinal);
    	        finalfinal = m2.replaceAll("").trim();
    	        if (!doCharCheck) {
           			// �������
           			e.setMessage(finalfinal);
    	        }
    		}
        } else {
    		if (!set.isEmpty()) {  // ֱ��ɾ��
    			e.getPlayer().sendMessage(Message.msg_Reject);
    			e.setCancelled(true);
    		}
        }
        
		// ȫ�䵥�ּ��
        if (doCharCheck) {
        	if (set.isEmpty()) { 				// DFA�ж�Ϊ�����о�  ��δ���������ԭʼ���������
           		if (Message.replaceEnable) {
           			for(String charBan: BadWordDetector.banCharList) {
           				intputMsg = intputMsg.replace(charBan, Message.maskSymbol);
           			}
           			// �������
           			e.setMessage(intputMsg);
           		} else {  // ֱ��ɾ��
           	        for(String charBan: BadWordDetector.banCharList) {
           	        	if (intputMsg.contains(charBan)) {
           	    			e.getPlayer().sendMessage(Message.msg_Reject);
           	    			e.setCancelled(true);
           	        	}
           	        }
           		}
            } else { 							// DFA�ж�Ϊ���о�  ��ǿ��ɾ�����ŷ�֮��ľ��������
           		if (Message.replaceEnable) {
           			for(String charBan: BadWordDetector.banCharList) {
           				finalfinal = finalfinal.replace(charBan, Message.maskSymbol);
           			}
           			// �������
           			e.setMessage(finalfinal);
           		} else {  // ֱ��ɾ��
           	        for(String charBan: BadWordDetector.banCharList) {
           	        	if (finalfinal.contains(charBan)) {
           	    			e.getPlayer().sendMessage(Message.msg_Reject);
           	    			e.setCancelled(true);
           	        	}
           	        }
           		}
       		}
        }
	}
}
