package de.ellpeck.actuallyadditions.mod.crafting;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import de.ellpeck.actuallyadditions.mod.items.ItemTag;
import de.ellpeck.actuallyadditions.mod.util.GsonUtil;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Optional;

public class MiningLensRecipe implements Recipe<Container>, WeightedEntry {
	public static final String NAME = "mining_lens";

	private final ResourceLocation id;
	private final int weight;
	private final Ingredient input;
	//private final int weight;
	private ItemStack output;
	private TagKey<Item> outputTag;
	private OutputType outputType;

	private MiningLensRecipe(ResourceLocation id, Ingredient input, int weight) {
		super();
		this.weight = weight;
		this.input = input;
		this.id = id;
	}

	public MiningLensRecipe(ResourceLocation id, Ingredient input, int weight, ItemStack output) {
		this(id, input, weight);
		this.output = output;
		this.outputType = OutputType.ITEM;
	}

	public MiningLensRecipe(ResourceLocation id, Ingredient input, int weight, TagKey<Item> outputTag) {
		this(id, input, weight);
		this.outputTag = outputTag;
		this.outputType = OutputType.TAG;
	}

	public Weight getWeight() {
		return Weight.of(weight);
	}

	public Ingredient getInput() {
		return input;
	}

	public boolean matches(ItemStack test) {
		return input.test(test);
	}

	@Override
	public boolean matches(Container pInv, Level pLevel) {
		return false;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Nonnull
	@Override
	public ItemStack assemble(Container pInv, RegistryAccess pRegistryAccess) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight) {
		return false;
	}

	@Override
	public @NotNull ItemStack getResultItem(RegistryAccess pRegistryAccess) {
		if (outputType == OutputType.ITEM) return output;
		else return getOutputTag().map(x -> new ItemStack(x.get(0).get())).orElse(ItemStack.EMPTY);
	}

	private Optional<HolderSet.Named<Item>> getOutputTag() {
		if (outputType == OutputType.ITEM) return Optional.empty();
		return BuiltInRegistries.ITEM.getTag(this.outputTag);
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ActuallyRecipes.MINING_LENS_RECIPE.get();
	}

	@Override
	public RecipeType<?> getType() {
		return ActuallyRecipes.Types.MINING_LENS.get();
	}

	public static class Serializer implements RecipeSerializer<MiningLensRecipe> {
		@Override
		public MiningLensRecipe fromJson(@Nonnull ResourceLocation pRecipeId, @Nonnull JsonObject pJson) {
			Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(pJson, "ingredient"));
			int weight = GsonHelper.getAsInt(pJson, "weight");
			String type = GsonHelper.getAsString(pJson, "output_type", "item");
			if (type.equals("item")) {
				ItemStack result = GsonUtil.getItemWithCount(pJson, "result");
				return new MiningLensRecipe(pRecipeId, ingredient, weight, result);
			} else if (type.equals("tag")) {
				var tag = TagKey.codec(Registries.ITEM).decode(JsonOps.INSTANCE, pJson.get("result")).getOrThrow(false, e -> {
					throw new IllegalStateException(e);
				}).getFirst();
				return new MiningLensRecipe(pRecipeId, ingredient, weight, tag);
			}
			throw new IllegalStateException("Unknown recipe type: " + type);
		}

		@Nullable
		@Override
		public MiningLensRecipe fromNetwork(@Nonnull ResourceLocation pRecipeId, @Nonnull FriendlyByteBuf pBuffer) {
			Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
			int weight = pBuffer.readInt();
			OutputType outputType = pBuffer.readEnum(OutputType.class);
			if (outputType == OutputType.ITEM) {
				ItemStack result = pBuffer.readItem();
				return new MiningLensRecipe(pRecipeId, ingredient, weight, result);
			} else if (outputType == OutputType.TAG) {
				TagKey<Item> tagKey = pBuffer.readJsonWithCodec(TagKey.codec(Registries.ITEM));
				return new MiningLensRecipe(pRecipeId, ingredient, weight, tagKey);
			}
			throw new IllegalStateException("Invalid mining lens output type " + outputType);
		}

		@Override
		public void toNetwork(@Nonnull FriendlyByteBuf pBuffer, MiningLensRecipe pRecipe) {
			pRecipe.input.toNetwork(pBuffer);
			pBuffer.writeInt(pRecipe.weight);
			pBuffer.writeEnum(pRecipe.outputType);
			if (pRecipe.outputType == OutputType.ITEM) {
				pBuffer.writeItem(pRecipe.output);
			} else if (pRecipe.outputType == OutputType.TAG) {
				pBuffer.writeJsonWithCodec(TagKey.codec(Registries.ITEM), pRecipe.outputTag);
			}
		}
	}

	public static class Result implements FinishedRecipe {
		private final ResourceLocation id;
		private final Ingredient itemIngredient;
		private final int weight;
		private final ItemLike output;
		private final TagKey<Item> outputTag;
		private final OutputType outputType;

		public Result(ResourceLocation id, Ingredient itemIngredient, int weight, ItemLike output, TagKey<Item> outputTag, OutputType outputType) {
			this.id = id;
			this.itemIngredient = itemIngredient;
			this.weight = weight;
			this.output = output;
			this.outputTag = outputTag;
			this.outputType = outputType;
		}

		@Override
		public void serializeRecipeData(JsonObject pJson) {
			pJson.add("ingredient", itemIngredient.toJson());
			pJson.addProperty("weight", weight);
			pJson.addProperty("output_type", outputType.toString().toLowerCase(Locale.ROOT));

			if (outputType == OutputType.ITEM) {
				JsonObject resultObject = new JsonObject();
				resultObject.addProperty("item", ForgeRegistries.ITEMS.getKey(output.asItem()).toString());
				pJson.add("result", resultObject);
			} else if (outputType == OutputType.TAG) {
				pJson.add("result", TagKey.codec(Registries.ITEM).encodeStart(JsonOps.INSTANCE, outputTag).getOrThrow(false, e -> {
					throw new IllegalStateException(e);
				}));
			}

		}

		@Override
		public ResourceLocation getId() {
			return id;
		}

		@Override
		public RecipeSerializer<?> getType() {
			return ActuallyRecipes.MINING_LENS_RECIPE.get();
		}

		@Nullable
		@Override
		public JsonObject serializeAdvancement() {
			return null;
		}

		@Nullable
		@Override
		public ResourceLocation getAdvancementId() {
			return null;
		}
	}

	public enum OutputType {
		ITEM,
		TAG
	}
}
