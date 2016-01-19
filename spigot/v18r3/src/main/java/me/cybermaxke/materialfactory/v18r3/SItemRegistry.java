package me.cybermaxke.materialfactory.v18r3;

import me.cybermaxke.materialfactory.api.item.VanillaItemType;
import me.cybermaxke.materialfactory.common.SimpleItemRegistry;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;

import java.util.Optional;

public class SItemRegistry extends SimpleItemRegistry {

    @Override
    public Optional<VanillaItemType> get(Material material) {
        return Optional.ofNullable((VanillaItemType) CraftMagicNumbers.getItem(material));
    }
}
