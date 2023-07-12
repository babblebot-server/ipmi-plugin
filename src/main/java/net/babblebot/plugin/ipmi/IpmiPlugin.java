package net.babblebot.plugin.ipmi;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.babblebot.api.command.Command;
import net.babblebot.api.command.CommandParam;
import net.babblebot.api.command.ICommandContext;
import net.babblebot.api.obj.DiscordColor;
import net.babblebot.api.obj.message.discord.DiscordMessage;
import net.babblebot.api.obj.message.discord.embed.EmbedAuthor;
import net.babblebot.api.obj.message.discord.embed.EmbedField;
import net.babblebot.api.obj.message.discord.embed.EmbedMessage;
import net.babblebot.api.plugins.Plugin;
import net.babblebot.plugin.ipmi.properties.IpmiConfigurationProperties;
import net.babblebot.plugin.ipmi.service.IpmiService;


/**
 * Edit me
 *
 * @author email@aaronburt (Aaron Michael Burt)
 * @since 1.0.0
 */
@Plugin
@RequiredArgsConstructor
@Slf4j
public class IpmiPlugin {

    private final IpmiConfigurationProperties ipmiConfigProperties;
    private final IpmiService ipmiService;

    public boolean isAuthorised(DiscordMessage message){
        return ipmiConfigProperties.getAuthUsers().stream().noneMatch(au -> au == message.getAuthor().getId().toLong());
    }

    public EmbedAuthor embedAuthor(){
        return EmbedAuthor.builder()
                .name("IPMI")
                .url("https://github.com/babblebot-server/ipmi-plugin")
                .iconUrl("https://avatars.githubusercontent.com/u/138989349")
                .build();
    }


    @Command(aliases = "power")
    @CommandParam(value = "action", canBeEmpty = false, optional = false)
    public EmbedMessage powerState(DiscordMessage message, ICommandContext context){
        String actionParam = context.getParameter("action");

        if(isAuthorised(message)) {
            return EmbedMessage.builder()
                    .title("Not authorised")
                    .author(embedAuthor())
                    .description("In order to use the command you must be authorised with your discord id")
                    .color(DiscordColor.RED)
                    .build();
        }

        switch (actionParam) {
            case "on", "off" -> {
                return EmbedMessage.builder()
                        .title("Power state set")
                        .author(embedAuthor())
                        .description("Power state set to: " + (ipmiService.setPowerStatus(actionParam) ? "online" : "offline"))
                        .build();
            }
            case "status" -> {
                return EmbedMessage.builder()
                        .title("Power state got")
                        .author(embedAuthor())
                        .description("Power state is: " + (ipmiService.getPowerStatus() ? "online" : "offline" ))
                        .color(DiscordColor.BLUE)
                        .build();
            }
            default -> {
                return EmbedMessage.builder().title("on/off/status").build();
            }
        }
    }

    @Command(aliases = "fanspeed")
    @CommandParam(value = "value", canBeEmpty = false, optional = false)
    public EmbedMessage fanSpeed(DiscordMessage message, ICommandContext context){
        try {
            int fanSpeed = Integer.parseInt(context.getParameter("value"));

            if(fanSpeed >= 0 && fanSpeed <= 100){
                return EmbedMessage.builder()
                        .title("Fan speed")
                        .author(embedAuthor())
                        .description("Fan speed is set to " +  ipmiService.setFanSpeed(fanSpeed))
                        .color(DiscordColor.BLUE)
                        .build();
            }

            return null;

        } catch(Exception error){
            return null;
        }
    }

    @Command(aliases = "syntax")
    public EmbedMessage help(){

        return EmbedMessage.builder()
                .title("Ipmi Help")
                .author(embedAuthor())
                .addField(EmbedField.builder().name("Fan speed").value("value (number)").build())
                .build();

    }


}

