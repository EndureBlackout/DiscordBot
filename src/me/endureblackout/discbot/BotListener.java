package me.endureblackout.discbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.emote.EmoteAddedEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class BotListener extends ListenerAdapter {
	@Override
	public void onMessageReceived(MessageReceivedEvent e) {
		if (e.getMessage().getRawContent().equalsIgnoreCase("ping")) {
			e.getChannel().sendMessage(e.getAuthor().getAsMention() + " pong!").queue();
		}

		if (e.getMessage().getRawContent().equalsIgnoreCase("/avatar")) {
			e.getMessage().delete().queue();
			
			String s = e.getAuthor().getAvatarUrl();
			e.getChannel().sendMessage(s).queue();
		}

		if (e.getMessage().getRawContent().equalsIgnoreCase("laugh")) {
			e.getMessage().addReaction(e.getGuild().getEmoteById("laugh")).queue();
		}

		if (e.getMessage().getRawContent().startsWith("/kick")) {
			e.getMessage().delete().queue();
			List<User> users = e.getMessage().getMentionedUsers();

			Guild guild = e.getGuild();

			for (User u : users) {
				Member memb = guild.getMember(u);

				guild.getController().kick(memb).queue(
						success -> e.getChannel().sendMessage("Kicked " + memb.getEffectiveName()).queue(), error -> {
							if (error instanceof PermissionException) {

								e.getChannel().sendMessage(
										"Permission kicking [" + memb.getEffectiveName() + "]: " + error.getMessage())
										.queue();
							} else {
								e.getChannel()
										.sendMessage("Unknow error while kicking [" + memb.getEffectiveName() + "]: "
												+ "<" + error.getClass().getSimpleName() + ">: " + error.getMessage())
										.queue();
							}
						});
			}
		}

		if (e.getMessage().getRawContent().startsWith("/ban")) {
			e.getMessage().delete().queue();

			List<User> users = e.getMessage().getMentionedUsers();
			Guild guild = e.getGuild();
			Member selfMemb = guild.getMember(e.getAuthor());

			if (selfMemb.hasPermission(Permission.BAN_MEMBERS)) {
				for (User u : users) {
					Member memb = guild.getMember(u);

					guild.getController().ban(memb, 6).queue(
							success -> e.getChannel().sendMessage("Banned " + memb.getEffectiveName()).queue(), error -> {
								if (error instanceof PermissionException) {
									e.getChannel().sendMessage("Permission banning [" + memb.getEffectiveName() + "]:"
											+ error.getMessage()).queue();
								} else {
									e.getChannel()
											.sendMessage("Unknown error while banning [" + memb.getEffectiveName()
													+ "]: " + "<" + error.getClass().getSimpleName() + ">: "
													+ error.getMessage())
											.queue();
								}
							});
				}
			} else {
				e.getChannel().sendMessage(e.getAuthor().getAsMention() + " you don't have permission to ban!").queue();
			}
		}
		
		if(e.getMessage().getRawContent().equalsIgnoreCase("/meme")) {
			e.getMessage().delete().queue();
			
			JSONObject ob = null;
			JSONArray ar = null;
			
			try {
				ob = readJsonFromUrl("http://api.giphy.com/v1/gifs/search?q=meme&api_key=dc6zaTOxFJmzC");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				ar = (JSONArray) ob.getJSONArray("data");
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
			e.getChannel().sendMessage(ob.toString()).queue();
		}
	}
	
	@Override
	public void onEmoteAdded(EmoteAddedEvent e) {
		Emote emote = e.getEmote();
		
		e.getGuild().getPublicChannel().sendMessage("New emote added! " + emote);
		List<TextChannel> channels = e.getGuild().getTextChannels();
		
		for(TextChannel announce : channels) {
			if(announce.getName().equalsIgnoreCase("announcements")) {
				announce.sendMessage("Emoji added!").queue();
				announce.sendMessage("Emoji: " + emote.getName()).queue();
			}
		}
	}
	
	public static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		
		while((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		
		return sb.toString();
	}
	
	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			
			return json;
		} finally {
			is.close();
		}
	}
	
	public static JSONArray readArray(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONArray json = new JSONArray(jsonText);
			
			return json;
		} finally {
			is.close();
		}
	}
}
