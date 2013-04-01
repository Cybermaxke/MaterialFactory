package me.cybermaxke.chunkdata;

import me.cybermaxke.tagutils.TagCompound;

public class ChunkData {
	private TagCompound tag = new TagCompound();

	public ChunkData setTag(TagCompound tag) {
		this.tag = tag;
		return this;
	}

	public TagCompound getTag() {
		return this.tag;
	}

	public ChunkData clear() {
		this.tag = new TagCompound();
		return this;
	}

	public ChunkData clone() {
		return new ChunkData().setTag(this.tag.clone());
	}
}