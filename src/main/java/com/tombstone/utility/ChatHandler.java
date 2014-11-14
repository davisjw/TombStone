package com.tombstone.utility;

import java.util.Arrays;
import java.util.List;

import com.tombstone.TombStone;
import com.tombstone.block.TileEntityTombStone;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class ChatHandler extends CommandBase {

	@Override
	public String getCommandName() {
		return "tombstone";
	}

	@Override
    public List getCommandAliases()
    {
        return Arrays.asList(new String[] {"tombs"});
    }
	
	@Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

	@Override
	public void processCommand(ICommandSender var1, String[] var2) {
        int tombCounter = 0;
        String chatMessage = "Tombstones:\n";
        for(int i=0; i< TombStone.instance.tombList.size(); i++)
        {
        	TileEntityTombStone item = TombStone.instance.tombList.get(i);
        	if(item.getOwner().equals(var1.getCommandSenderName()))
        	{
        		//TODO - Output coordinates to chat
        		chatMessage += "Tomb " + (tombCounter+1) + ": " + item.xCoord + "," + item.yCoord + "," + item.zCoord + "\n";
        		tombCounter++;
        	}

        	//DEBUG//
            LogHelper.debug("[TombStone] processCommand(): tombList[" + i + "] owner='" + item.getOwner() + "' senderName='" + var1.getCommandSenderName() + "'");
        }
        if(tombCounter == 0)
        	chatMessage += "None\n";

        //Send the chat message to the client
//        ChatMessageComponent msg = new ChatMessageComponent();
      //  msg.func_111072_b(chatMessage); // this line here is going to cause problems when mcp de-obfuscates this method
      //  var1.sendChatToPlayer(chatMessage);
        this.sendMsg(var1, chatMessage);
//        var1.sendChatToPlayer(ChatMessageComponent.createFromText(chatMessage));
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "tombstone";
	}

    // added, jwd
    public void sendMsg(ICommandSender target, String msg) {
       target.addChatMessage(new ChatComponentText(msg));
    }
}
