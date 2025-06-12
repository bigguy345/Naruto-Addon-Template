package goatee.narutoaddon.template.network;

import goatee.narutoaddon.template.NarutoAddon;
import goatee.narutoaddon.template.client.JutsuKeys;
import goatee.narutoaddon.template.network.packets.NarutoSyncData;
import goatee.narutoaddon.template.proxy.CommonProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Hashtable;
import java.util.Map;

public final class PacketHandler {
    public static PacketHandler Instance;

    public Map<String, AbstractPacket> map = new Hashtable<>();
    public Map<String, FMLEventChannel> channels = new Hashtable<>();

    public PacketHandler() {

        map.put(NarutoSyncData.packetName, new NarutoSyncData());
        map.put(JutsuKeys.Packet.packetName, new JutsuKeys.Packet());
        this.register();
    }

    public void register() {
        FMLEventChannel eventChannel;
        for (String channel : map.keySet()) {
            eventChannel = NetworkRegistry.INSTANCE.newEventDrivenChannel(channel);
            eventChannel.register(this);
            channels.put(channel, eventChannel);
        }
    }

    @SubscribeEvent
    public void onServerPacket(FMLNetworkEvent.ServerCustomPacketEvent event) {
        try {
            map.get(event.getPacket().channel()).receiveData(event.getPacket().payload(), ((NetHandlerPlayServer) event.getHandler()).player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        try {
            map.get(event.getPacket().channel()).receiveData(event.getPacket().payload(), NarutoAddon.PROXY.getClientPlayer());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToPlayer(EntityPlayer player, AbstractPacket pr) {
        FMLProxyPacket packet = pr.generatePacket();
        if (packet != null && CommonProxy.side() == Side.SERVER) {
            channels.get(packet.channel()).sendTo(packet, (EntityPlayerMP) player);
        }
    }

    public void sendToServer(AbstractPacket pr) {
        FMLProxyPacket packet = pr.generatePacket();
        if (packet != null) {
            packet.setTarget(Side.SERVER);
            channels.get(packet.channel()).sendToServer(packet);
        }
    }

    public void sendToTrackingPlayers(Entity entity, AbstractPacket packet) {
        FMLProxyPacket pr = packet.generatePacket();
        if (pr != null && CommonProxy.side() == Side.SERVER) {
            EntityTracker tracker = ((WorldServer) entity.world).getEntityTracker();
            tracker.sendToTracking(entity, pr); // Send packet to tracking players
        }
    }

    public void sendAround(Entity entity, double range, AbstractPacket packet) {
        FMLProxyPacket pr = packet.generatePacket();
        if (pr != null && CommonProxy.side() == Side.SERVER) {
            channels.get(pr.channel()).sendToAllAround(pr, new NetworkRegistry.TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, range));
        }
    }

    public void sendToAll(AbstractPacket packet) {
        FMLProxyPacket pr = packet.generatePacket();
        if (pr != null && CommonProxy.side() == Side.SERVER) {
            channels.get(pr.channel()).sendToAll(pr);
        }
    }
}
