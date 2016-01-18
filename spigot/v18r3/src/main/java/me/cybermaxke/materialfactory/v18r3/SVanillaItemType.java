package me.cybermaxke.materialfactory.v18r3;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;

import com.google.common.collect.Maps;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.minecraft.server.v1_8_R3.Item;
import me.cybermaxke.materialfactory.api.VanillaItemType;

public class SVanillaItemType implements VanillaItemType {

    private static final Map<Material, VanillaItemType> byMaterial = Maps.newHashMap();

    private final Item item;
    private final Material material;

    public SVanillaItemType(Item item) {
        this.material = CraftMagicNumbers.getMaterial(item);
        this.item = item;

        byMaterial.put(this.material, this);
    }

    @Override
    public BaseComponent getName() {
        return new TranslatableComponent(this.item.getName());
    }

    public Item getItem() {
        return this.item;
    }

    public Material getMaterial() {
        return this.material;
    }

    public static VanillaItemType getByMaterial(Material material) {
        return byMaterial.get(material);
    }

}
