package me.cybermaxke.materialfactory.v18r3.enbt;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;

@SuppressWarnings("rawtypes")
public class SerialMap implements EnbtSerializer<Map, NBTTagList> {

    @Override
    public EnbtSerializerData<NBTTagList> serialize(EnbtSerializerContext ctx, Map object) {
        NBTTagList list = new NBTTagList();
        for (Object obj : object.entrySet()) {
            Entry en = (Entry) obj;
            NBTTagCompound entry = new NBTTagCompound();
            entry.set("k", ctx.serialize(en.getKey()));
            entry.set("v", ctx.serialize(en.getValue()));
            list.add(entry);
        }
        return new EnbtSerializerData<>(list);
    }

    @Override
    public Map deserialize(EnbtSerializerContext ctx, EnbtSerializerData<NBTTagList> tag) {
        NBTTagList list = tag.getTag();
        Map<Object, Object> map = Maps.newHashMapWithExpectedSize(list.size());
        for (int i = 0; i < list.size(); i++) {
            NBTTagCompound entry = list.get(i);
            map.put(ctx.deserialize(entry.get("k")), ctx.deserialize(entry.get("v")));
        }
        return map;
    }

}
