package goatee.narutoaddon.template;

import goatee.narutoaddon.template.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = NarutoAddon.MODID, name = NarutoAddon.NAME, version = NarutoAddon.VERSION)
public class NarutoAddon {

    public static final String MODID = "narutoaddontemplate";
    public static final String NAME = "Naruto Addon Template";
    public static final String VERSION = "1.0.0";
    public static final String DEPEND = "";
    public static final Logger LOGGER = LogManager.getLogger(NAME);
    public static final SimpleNetworkWrapper NETWORK = new SimpleNetworkWrapper(MODID);

    @SidedProxy(clientSide = "goatee.narutoaddon.template.proxy.ClientProxy", serverSide = "goatee.narutoaddon.template.proxy.CommonProxy")
    public static CommonProxy PROXY;

    @Mod.Instance(MODID)
    public static NarutoAddon INSTANCE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        PROXY.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        PROXY.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        PROXY.postInit(event);
    }

    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event) {

    }
}
