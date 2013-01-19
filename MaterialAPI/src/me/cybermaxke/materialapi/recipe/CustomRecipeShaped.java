package me.cybermaxke.materialapi.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import me.cybermaxke.materialapi.inventory.CustomItemStack;
import me.cybermaxke.materialapi.utils.InventoryUtils;

public class CustomRecipeShaped implements CustomRecipe {
	private CustomItemStack result;
	private CustomItemStack[] items;
	private Recipe bukkitrecipe;
	private int height;
	private int width;

	public CustomRecipeShaped(CustomItemStack result) {
		this.result = result;
	}
	
	public void setShape(CustomItemStack[]... cs) {
		List<CustomItemStack> items = new ArrayList<CustomItemStack>();
		this.height = cs.length;
		
		for (CustomItemStack[] it : cs) {
			if (it.length > this.width) {
				this.width = it.length;
			}
		}
		
		for (CustomItemStack[] it : cs) {
			CustomItemStack[] is = new CustomItemStack[this.width];
			
			for (int i = 0; i < it.length; i++) {
				is[i] = it[i];
			}
			
			if (it.length < this.width) {
				int i = this.width - it.length;
				
				for (int n = 0; n < i; n++) {
					is[i] = null;
				}
			}
			
			items.addAll(Arrays.asList(is));
		}

		this.items = new CustomItemStack[this.height * this.width];
		for (int i = 0; i < this.items.length; i++) {
			this.items[i] = items.get(i);
		}

		this.bukkitrecipe = this.toBukkitRecipe();
	}
	
	public int getSize() {
		return this.height * this.width;
	}

	@Override
	public CustomItemStack getResult() {
		return new CustomItemStack(this.result.clone());
	}

	@Override
	public CustomItemStack[] getItems() {
		return this.items;
	}

	private Recipe toBukkitRecipe() {
		ShapedRecipe recipe = new ShapedRecipe(this.getResult());
		
		switch (this.height) {
			case 1:
				switch (this.width) {
					case 1:
						recipe.shape(new String[] { "a" });
						break;
					case 2:
						recipe.shape(new String[] { "ab" });
						break;
					case 3:
						recipe.shape(new String[] { "abc" });
			}

			break;
			case 2:
				switch (this.width) {
					case 1:
						recipe.shape(new String[] { "a", "b" });
						break;
					case 2:
						recipe.shape(new String[] { "ab", "cd" });
						break;
					case 3:
						recipe.shape(new String[] { "abc", "def" });
			}

			break;
			case 3:
				switch (this.width) {
					case 1:
						recipe.shape(new String[] { "a", "b", "c" });
						break;
					case 2:
						recipe.shape(new String[] { "ab", "cd", "ef" });
						break;
					case 3:
						recipe.shape(new String[] { "abc", "def", "ghi" });
			}	
		}

		char c = 'a';
		for (CustomItemStack stack : this.items) {
			if (stack != null) {
				recipe.setIngredient(c, stack.getType(), stack.getDurability());
			}
				
			c = (char)(c + '\001');
		}
			
		return recipe;
	}

	public boolean checkMatch(Inventory inventory, int i, int j, boolean flag) {
		for (int k = 0; k < 3; k++) {
    		for (int l = 0; l < 3; l++) {
    			int i1 = k - i;
    			int j1 = l - j;
    			CustomItemStack is = null;

    			if ((i1 >= 0) && (j1 >= 0) && (i1 < this.width) && (j1 < this.height)) {
    				if (flag) {
            	  		is = this.items[(this.width - i1 - 1 + j1 * this.width)];
    				} else {
            	  		is = this.items[(i1 + j1 * this.width)];
              		}
            	}

            	CustomItemStack is2 = InventoryUtils.getStackInRowAndColumn(inventory, k, l);
    			if (!InventoryUtils.doItemsMatch(is, is2)) {			
    				return false;
    			}
          	}
        }

		return true;
	}

	@Override
	public boolean matches(Inventory inventory) {
		for (int i = 0; i <= 3 - this.width; i++) {
			for (int j = 0; j <= 3 - this.height; j++) {
				if (this.checkMatch(inventory, i, j, true)) {
					return true;
		       	}

				if (this.checkMatch(inventory, i, j, false)) {
					return true;
				}
			}
		}
		 
		return false;
	}

	@Override
	public Recipe getBukkitRecipe() {
		return this.bukkitrecipe;
	}
}