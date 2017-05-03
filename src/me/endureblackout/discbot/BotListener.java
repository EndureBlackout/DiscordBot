package me.endureblackout.discbot;

import java.util.List;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
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
			
			
		}
		
		
	}
}
