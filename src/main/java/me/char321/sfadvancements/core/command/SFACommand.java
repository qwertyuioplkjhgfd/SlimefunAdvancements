package me.char321.sfadvancements.core.command;

import me.char321.sfadvancements.SFAdvancements;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SFACommand implements CommandExecutor {
    private final List<SubCommand> subcommands = new LinkedList<>();

    public SFACommand(SFAdvancements plugin) {
        subcommands.add(new SaveCommand());
        subcommands.add(new RevokeCommand());
        subcommands.add(new GrantCommand());
        subcommands.add(new GuiCommand());
        subcommands.add(new DumpItemCommand());
        subcommands.add(new ReloadCommand());
        subcommands.add(new ImportCommand());

        plugin.getCommand("sfadvancements").setTabCompleter(new SFATabCompleter(this));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            for (SubCommand subcmd : subcommands) {
                if (args[0].equalsIgnoreCase(subcmd.getCommandName())) {
                    if (sender.hasPermission("sfa.command."+subcmd.getCommandName())) {
                        return subcmd.onExecute(sender, command, label, args);
                    } else {
                        sender.sendMessage("You do not have permission to use this command.");
                        return false;
                    }
                }
            }
            sender.sendMessage("Unknown subcommand! Available subcommands are: " + subcommands.stream().map(SubCommand::getCommandName).collect(Collectors.joining(", ")));
            return false;
        }
        sender.sendMessage("SFAdvancements version " + SFAdvancements.instance().getDescription().getVersion());
        return true;
    }

    public List<SubCommand> getSubCommands() {
        return subcommands;
    }
}
