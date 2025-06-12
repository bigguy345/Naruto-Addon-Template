package goatee.narutoaddon.template.client;

import goatee.narutoaddon.template.NarutoAddon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class Sounds {

    // public static final SoundEvent RINNEGAN = createSoundEvent("rinnegan");

    private static SoundEvent createSoundEvent(String name) {
        ResourceLocation location = new ResourceLocation(NarutoAddon.MODID, name);
        return new SoundEvent(location).setRegistryName(location);
    }

    @SubscribeEvent
    public static void onRegisterSounds(RegistryEvent.Register<SoundEvent> event) {
        //  event.getRegistry().registerAll(RINNEGAN);
    }
}
