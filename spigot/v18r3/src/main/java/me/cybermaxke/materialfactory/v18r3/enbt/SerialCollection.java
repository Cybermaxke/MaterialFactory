package me.cybermaxke.materialfactory.v18r3.enbt;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import me.cybermaxke.materialfactory.api.util.Coerce;
import net.minecraft.server.v1_8_R3.NBTTagList;

@SuppressWarnings({"unchecked", "rawtypes"})
public class SerialCollection implements EnbtSerializer<Collection, NBTTagList> {

    @Override
    public EnbtSerializerData<NBTTagList> serialize(EnbtSerializerContext ctx, Collection object) {
        NBTTagList tag = new NBTTagList();
        List<?> list = Lists.newArrayList(object);
        int[] extraData = new int[0];
        if (!list.isEmpty()) {
            if (Coerce.toListOf(list, list.get(0).getClass()).size() == list.size()) {
                EnbtEntry entry = ctx.serializeAsEntry(list.get(0));
                Integer type = entry.getExtendedType();
                int[] extraData0 = entry.getExtraData();
                if (type != null || extraData0.length > 0) {
                    extraData = new int[extraData.length + 1];
                    extraData[0] = type != null ? type : 0;
                    for (int i = 0; i < extraData0.length; i++) {
                        extraData[i + 1] = extraData0[i];
                    }
                }
                for (Object obj : list) {
                    tag.add(ctx.serializeAsEntry(obj).getTag());
                }
            } else {
                // 0 is not used
                extraData = new int[] { -1 };
                for (Object obj : list) {
                    tag.add(ctx.serialize(obj));
                }
            }
        }
        return new EnbtSerializerData<>(tag, extraData);
    }

    @Override
    public Collection deserialize(EnbtSerializerContext ctx, EnbtSerializerData<NBTTagList> tag) {
        NBTTagList tag0 = tag.getTag();
        List list = Lists.newArrayListWithCapacity(tag0.size());
        int[] extraData = tag.getExtraData();
        if (extraData.length > 0 && extraData[0] == -1) {
            for (int i = 0; i < tag0.size(); i++) {
                list.add(ctx.deserialize(tag0.get(i)));
            }
        } else {
            int type = extraData.length > 0 ? extraData[0] : tag0.f();
            if (extraData.length > 1) {
                extraData = Arrays.copyOfRange(extraData, 1, extraData.length);
            }
            for (int i = 0; i < tag0.size(); i++) {
                list.add(ctx.deserializeFromEntry(new EnbtEntry(tag0.g(i), type, extraData)));
            }
        }
        return list;
    }

}
