package me.endureblackout.discbot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public class DiscBotMain {
	
	public static JDA jda;
	public static final String BOT_TOKEN = "MzA4NzQ3MzE1MjYxMzQxNjk5.C-leww.EzTju-WnXD4ba38Kw_3k_e9Xtzg";
	
	public static void main(String[] args) {
		
		try {
			jda = new JDABuilder(AccountType.BOT).addEventListener(new BotListener()).setToken(BOT_TOKEN).buildBlocking();
		} catch (LoginException | IllegalArgumentException | InterruptedException | RateLimitedException e) {
			e.printStackTrace();
		}
	}
}
