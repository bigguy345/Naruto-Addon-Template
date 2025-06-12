package goatee.narutoaddon.template.client;

import goatee.narutoaddon.template.network.AbstractPacket;
import goatee.narutoaddon.template.network.PacketHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(value = Side.CLIENT)
public class JutsuKeys {
	public static List<Key> jutsuKeys = new ArrayList<>();
	
	/*
	i.e:
	
	private static Key key4 = new Key(4, "key.jutsu.4", Keyboard.KEY_NUMPAD4, "key.mcreator.category");
	private static Key key5 = new Key(5, "key.jutsu.5", Keyboard.KEY_NUMPAD5, "key.mcreator.category");
	private static Key key6 = new Key(6, "key.jutsu.6", Keyboard.KEY_NUMPAD6, "key.mcreator.category");
	private static Key key7 = new Key(7, "key.jutsu.7", Keyboard.KEY_NUMPAD7, "key.mcreator.category");
	
	*/

	@SideOnly(Side.CLIENT)
	public static class Key extends KeyBinding {
		public byte keyId;
		public boolean wasDown;

		public Key(int keyId, String description, int keyCode, String category) {
			super(description, keyCode, category);
			this.keyId = (byte) keyId;
			jutsuKeys.add(this);
		}

		/**
		 * @return Pressed = 0, Held = 1, Released = 2
		 */
		public byte getPressType() {
			boolean isDown = isKeyDown();
			return (byte) (!wasDown && isDown ? 0 : wasDown && !isDown ? 2 : 1);
		}

		public boolean onPress(byte pressType) {
			return true; //send packet
		}
	}

	public static void register() {
		jutsuKeys.forEach(key -> ClientRegistry.registerKeyBinding(key));
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void tick(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.START || Minecraft.getMinecraft().currentScreen != null || Minecraft.getMinecraft().player == null)
			return;

		for (Key key : jutsuKeys) {
			boolean isDown = key.isKeyDown();
			if (isDown || key.wasDown) {
				byte pressType = key.getPressType();
				if (key.onPress(pressType))
					PacketHandler.Instance.sendToServer(new Packet(key.keyId, pressType));
				key.wasDown = isDown;
			}
		}
	}

	public static class Packet extends AbstractPacket {
		public static final String packetName = "JutsuKey";

		public String getChannel() {
			return packetName;
		}

		byte keyId;
		byte pressType;

		public Packet() {
		}

		public Packet(byte keyId, byte is_pressed) {
			this.pressType = is_pressed;
			this.keyId = keyId;
		}

		public void sendData(ByteBuf out) throws IOException {
			out.writeByte(this.keyId);
			out.writeByte(this.pressType);
		}

		public void receiveData(ByteBuf in, EntityPlayer player) throws IOException {
			this.keyId = in.readByte();
			this.pressType = in.readByte();
			
			/*
			HANDLE HERE
			
			i.e:
				ItemStack helmet = player.inventory.armorInventory.get(3);

				if ((helmet.getItem() instanceof ItemDojutsu.Base)) {
					ItemDojutsu.Base eye = (ItemDojutsu.Base) helmet.getItem();

					if (keyId == 4)
						eye.onJutsuKey4(pressType, helmet, player);
					else if (keyId == 5)
						eye.onJutsuKey5(pressType, helmet, player);
					else if (keyId == 6)
						eye.onJutsuKey6(pressType, helmet, player);
					else if (keyId == 7)
						eye.onJutsuKey7(pressType, helmet, player);
				}
						
			 */
		}
	}
}
