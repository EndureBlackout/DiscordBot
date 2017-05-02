package me.endureblackout.discbot;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class BotListener extends ListenerAdapter {
	@Override
	public void onMessageReceived(MessageReceivedEvent e) {
		if (e.getMessage().getRawContent().equalsIgnoreCase("ping")) {
			e.getChannel().sendMessage(e.getAuthor().getAsMention() + " pong!").queue();
		}

		if (e.getMessage().getRawContent().equalsIgnoreCase("/avatar")) {
			String s = e.getAuthor().getAvatarUrl();
			e.getChannel().sendMessage(e.getAuthor().getAvatarUrl()).queue();
		}

		if (e.getMessage().getRawContent().equalsIgnoreCase("laugh")) {
			e.getMessage().addReaction(e.getGuild().getEmoteById("308795166607671306")).queue();
		}
	}
}
