package unsafedodo.fabricautomessage.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import eu.pb4.placeholders.api.TextParserUtils;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import unsafedodo.fabricautomessage.AutoMessage;

public class AutoMessageList {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment){
        dispatcher.register(CommandManager.literal("automessage")
                .then(CommandManager.literal("list")
                        .requires(Permissions.require("automessage.list", 3))
                            .executes(AutoMessageList::run)));
    }

    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();

        if(AutoMessage.messages.getSize() != 0){
            StringBuilder msg = new StringBuilder("-----------------------------------------------------\n");
            msg.append(String.format("<aqua>Timeout: <yellow>%d<reset>\n\n", AutoMessage.timeout));
            for(int i = 0; i < AutoMessage.messages.getSize(); i++){
                msg.append("<reset><aqua>Index ").append(i).append(":</aqua>").append(" ").append(AutoMessage.messages.get(i)).append("\n");
            }
            msg.append("<reset>-----------------------------------------------------");
            Text finalMsg = TextParserUtils.formatText(msg.toString());
            source.sendMessage(finalMsg);
        }
        else
            context.getSource().sendFeedback(() -> Text.literal("There are no messages saved").formatted(Formatting.RED), false);

        return 0;
    }
}
