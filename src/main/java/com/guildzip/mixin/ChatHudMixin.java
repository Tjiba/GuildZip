package com.guildzip.mixin;

import com.guildzip.ChatFormatter;
import com.guildzip.Config;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(ChatComponent.class)
public class ChatHudMixin {

    @ModifyVariable(
        method = "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/multiplayer/chat/GuiMessageSource;Lnet/minecraft/client/multiplayer/chat/GuiMessageTag;)V",
        at = @At("HEAD"),
        argsOnly = true
    )
    private Component onAddMessage(Component original) {
        if (original == null) return null;
        List<ChatFormatter.Seg> segs = ChatFormatter.INSTANCE.format(original.getString(), Config.get());
        if (segs == null) return original;

        MutableComponent out = Component.empty();
        for (ChatFormatter.Seg s : segs) {
            MutableComponent part = Component.literal(s.text);
            if (s.color != null) {
                int color = s.color;
                part = part.withStyle(style -> style.withColor(color));
            }
            out.append(part);
        }
        return out;
    }
}
