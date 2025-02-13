package de.ellpeck.actuallyadditions.data;

import de.ellpeck.actuallyadditions.api.ActuallyTags;
import de.ellpeck.actuallyadditions.mod.blocks.ActuallyBlocks;
import de.ellpeck.actuallyadditions.mod.items.ActuallyItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AdvancementGenerator extends ForgeAdvancementProvider {
	public AdvancementGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
		super(packOutput, registries, existingFileHelper, List.of(new ActuallyAdvancementGenerator()));
	}

	public static class ActuallyAdvancementGenerator implements AdvancementGenerator {
		@Override
		public void generate(HolderLookup.Provider registries, Consumer<Advancement> consumer, ExistingFileHelper existingFileHelper) {
			Advancement root = Advancement.Builder.advancement()
					.display(new DisplayInfo(ActuallyItems.ITEM_BOOKLET.get().getDefaultInstance(),
							Component.translatable("achievement.actuallyadditions.openBooklet"),
							Component.translatable("achievement.actuallyadditions.openBooklet.desc"),
							new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"), FrameType.TASK, true, true, false))
					.addCriterion("right_click", new ImpossibleTrigger.TriggerInstance())
					.save(consumer, "actuallyadditions:root");

			//TODO: Underwater Treasure Chest Advancement?

			Advancement phantomFace = Advancement.Builder.advancement()
					.parent(root)
					.display(new DisplayInfo(ActuallyBlocks.PHANTOM_ITEMFACE.get().asItem().getDefaultInstance(),
							Component.translatable("achievement.actuallyadditions.craftPhantomface"),
							Component.translatable("achievement.actuallyadditions.craftPhantomface.desc"),
							new ResourceLocation("textures/blocks/stone.png"), FrameType.TASK, true, true, false))
					.addCriterion("phantom_face", InventoryChangeTrigger.TriggerInstance.hasItems(ActuallyBlocks.PHANTOM_ITEMFACE.get()))
					.save(consumer, "actuallyadditions:craft_phantom_face");


			Advancement.Builder.advancement()
					.parent(phantomFace)
					.display(new DisplayInfo(ActuallyBlocks.PHANTOM_LIQUIFACE.get().asItem().getDefaultInstance(),
							Component.translatable("achievement.actuallyadditions.craftLiquiface"),
							Component.translatable("achievement.actuallyadditions.craftLiquiface.desc"),
							null, FrameType.TASK, true, true, false))
					.addCriterion("liquiface", InventoryChangeTrigger.TriggerInstance.hasItems(ActuallyBlocks.PHANTOM_LIQUIFACE.get()))
					.save(consumer, "actuallyadditions:craft_liquiface");

			Advancement.Builder.advancement()
					.parent(phantomFace)
					.display(new DisplayInfo(ActuallyBlocks.PHANTOM_ENERGYFACE.get().asItem().getDefaultInstance(),
							Component.translatable("achievement.actuallyadditions.craftEnergyface"),
							Component.translatable("achievement.actuallyadditions.craftEnergyface.desc"),
							null, FrameType.TASK, true, true, false))
					.addCriterion("energyface", InventoryChangeTrigger.TriggerInstance.hasItems(ActuallyBlocks.PHANTOM_ENERGYFACE.get()))
					.save(consumer, "actuallyadditions:craft_energyface");


			Advancement coalGenerator = Advancement.Builder.advancement()
					.parent(root)
					.display(new DisplayInfo(ActuallyBlocks.COAL_GENERATOR.get().asItem().getDefaultInstance(),
							Component.translatable("achievement.actuallyadditions.craftCoalGen"),
							Component.translatable("achievement.actuallyadditions.craftCoalGen.desc"),
							null, FrameType.TASK, true, true, false))
					.addCriterion("coal_generator", InventoryChangeTrigger.TriggerInstance.hasItems(ActuallyBlocks.COAL_GENERATOR.get()))
					.save(consumer, "actuallyadditions:craft_coal_generator");

			Advancement.Builder.advancement()
					.parent(coalGenerator)
					.display(new DisplayInfo(ActuallyBlocks.LEAF_GENERATOR.get().asItem().getDefaultInstance(),
							Component.translatable("achievement.actuallyadditions.craftLeafGen"),
							Component.translatable("achievement.actuallyadditions.craftLeafGen.desc"),
							null, FrameType.TASK, true, true, false))
					.addCriterion("leaf_generator", InventoryChangeTrigger.TriggerInstance.hasItems(ActuallyBlocks.LEAF_GENERATOR.get()))
					.save(consumer, "actuallyadditions:craft_leaf_generator");

			Advancement coffeeBeans = Advancement.Builder.advancement()
					.parent(coalGenerator)
					.display(new DisplayInfo(ActuallyItems.COFFEE_BEANS.get().getDefaultInstance(),
							Component.translatable("achievement.actuallyadditions.pickUpCoffee"),
							Component.translatable("achievement.actuallyadditions.pickUpCoffee.desc"),
							null, FrameType.TASK, true, true, false))
					.addCriterion("coffee_beans", InventoryChangeTrigger.TriggerInstance.hasItems(ActuallyItems.COFFEE_BEANS.get()))
					.save(consumer, "actuallyadditions:pickup_coffee");

			Advancement.Builder.advancement()
					.parent(coffeeBeans)
					.display(new DisplayInfo(ActuallyBlocks.COFFEE_MACHINE.get().asItem().getDefaultInstance(),
							Component.translatable("achievement.actuallyadditions.craftCoffeeMachine"),
							Component.translatable("achievement.actuallyadditions.craftCoffeeMachine.desc"),
							null, FrameType.TASK, true, true, false))
					.addCriterion("coffee_machine", InventoryChangeTrigger.TriggerInstance.hasItems(ActuallyBlocks.COFFEE_MACHINE.get()))
					.save(consumer, "actuallyadditions:craft_coffee_machine");

			Advancement reconstructor = Advancement.Builder.advancement()
					.parent(coalGenerator)
					.display(new DisplayInfo(ActuallyBlocks.ATOMIC_RECONSTRUCTOR.get().asItem().getDefaultInstance(),
							Component.translatable("achievement.actuallyadditions.craftReconstructor"),
							Component.translatable("achievement.actuallyadditions.craftReconstructor.desc"),
							null, FrameType.TASK, true, true, false))
					.addCriterion("atomic_reconstructor", InventoryChangeTrigger.TriggerInstance.hasItems(ActuallyBlocks.ATOMIC_RECONSTRUCTOR.get()))
					.save(consumer, "actuallyadditions:craft_reconstructor");

			Advancement makeFirstCrystal = Advancement.Builder.advancement()
					.parent(reconstructor)
					.display(new DisplayInfo(ActuallyItems.EMERADIC_CRYSTAL.get().getDefaultInstance(),
							Component.translatable("achievement.actuallyadditions.makeCrystal"),
							Component.translatable("achievement.actuallyadditions.makeCrystal.desc"),
							null, FrameType.TASK, true, true, false))
					.addCriterion("crystal", InventoryChangeTrigger.TriggerInstance.hasItems(
							ItemPredicate.Builder.item().of(ActuallyTags.Items.CRYSTALS).build())
					)
					.save(consumer, "actuallyadditions:make_first_crystal");

			Advancement.Builder.advancement()
					.parent(makeFirstCrystal)
					.display(new DisplayInfo(ActuallyBlocks.EMPOWERER.get().asItem().getDefaultInstance(),
							Component.translatable("achievement.actuallyadditions.craftEmpowerer"),
							Component.translatable("achievement.actuallyadditions.craftEmpowerer.desc"),
							null, FrameType.TASK, true, true, false))
					.addCriterion("empowerer", InventoryChangeTrigger.TriggerInstance.hasItems(ActuallyBlocks.EMPOWERER.get()))
					.save(consumer, "actuallyadditions:craft_empowerer");

			Advancement craftCrusher = Advancement.Builder.advancement()
					.parent(reconstructor)
					.display(new DisplayInfo(ActuallyBlocks.CRUSHER.get().asItem().getDefaultInstance(),
							Component.translatable("achievement.actuallyadditions.craftCrusher"),
							Component.translatable("achievement.actuallyadditions.craftCrusher.desc"),
							null, FrameType.TASK, true, true, false))
					.addCriterion("crystal", InventoryChangeTrigger.TriggerInstance.hasItems(
							ItemPredicate.Builder.item().of(ActuallyBlocks.CRUSHER.get()).build())
					)
					.save(consumer, "actuallyadditions:craft_crusher");

			Advancement craftDoubleCrusher = Advancement.Builder.advancement()
					.parent(craftCrusher)
					.display(new DisplayInfo(ActuallyBlocks.CRUSHER_DOUBLE.get().asItem().getDefaultInstance(),
							Component.translatable("achievement.actuallyadditions.craftDoubleCrusher"),
							Component.translatable("achievement.actuallyadditions.craftDoubleCrusher.desc"),
							null, FrameType.TASK, true, true, false))
					.addCriterion("crystal", InventoryChangeTrigger.TriggerInstance.hasItems(
							ItemPredicate.Builder.item().of(ActuallyBlocks.CRUSHER_DOUBLE.get()).build())
					)
					.save(consumer, "actuallyadditions:craft_double_crusher");

			Advancement craftLaserRelay = Advancement.Builder.advancement()
					.parent(reconstructor)
					.display(new DisplayInfo(ActuallyBlocks.LASER_RELAY.get().asItem().getDefaultInstance(),
							Component.translatable("achievement.actuallyadditions.craftLaserRelay"),
							Component.translatable("achievement.actuallyadditions.craftLaserRelay.desc"),
							null, FrameType.TASK, true, true, false))
					.addCriterion("laser_relay", InventoryChangeTrigger.TriggerInstance.hasItems(
							ItemPredicate.Builder.item().of(ActuallyBlocks.LASER_RELAY.get()).build())
					)
					.save(consumer, "actuallyadditions:craft_laser_relay");

			Advancement craftLaserRelayAdvanced = Advancement.Builder.advancement()
					.parent(craftLaserRelay)
					.display(new DisplayInfo(ActuallyBlocks.LASER_RELAY_ADVANCED.get().asItem().getDefaultInstance(),
							Component.translatable("achievement.actuallyadditions.craftLaserRelayAdvanced"),
							Component.translatable("achievement.actuallyadditions.craftLaserRelayAdvanced.desc"),
							null, FrameType.TASK, true, true, false))
					.addCriterion("laser_relay_advanced", InventoryChangeTrigger.TriggerInstance.hasItems(
							ItemPredicate.Builder.item().of(ActuallyBlocks.LASER_RELAY_ADVANCED.get()).build())
					)
					.save(consumer, "actuallyadditions:craft_laser_relay_advanced");

			Advancement craftLaserRelayExtreme = Advancement.Builder.advancement()
					.parent(craftLaserRelayAdvanced)
					.display(new DisplayInfo(ActuallyBlocks.LASER_RELAY_EXTREME.get().asItem().getDefaultInstance(),
							Component.translatable("achievement.actuallyadditions.craftLaserRelayExtreme"),
							Component.translatable("achievement.actuallyadditions.craftLaserRelayExtreme.desc"),
							null, FrameType.TASK, true, true, false))
					.addCriterion("laser_relay_extreme", InventoryChangeTrigger.TriggerInstance.hasItems(
							ItemPredicate.Builder.item().of(ActuallyBlocks.LASER_RELAY_EXTREME.get()).build())
					)
					.save(consumer, "actuallyadditions:craft_laser_relay_extreme");

			Advancement craftLaserRelayItem = Advancement.Builder.advancement()
					.parent(craftLaserRelay)
					.display(new DisplayInfo(ActuallyBlocks.LASER_RELAY_ITEM.get().asItem().getDefaultInstance(),
							Component.translatable("achievement.actuallyadditions.craftLaserRelayItem"),
							Component.translatable("achievement.actuallyadditions.craftLaserRelayItem.desc"),
							null, FrameType.TASK, true, true, false))
					.addCriterion("laser_relay_item", InventoryChangeTrigger.TriggerInstance.hasItems(
							ItemPredicate.Builder.item().of(ActuallyBlocks.LASER_RELAY_ITEM.get()).build())
					)
					.save(consumer, "actuallyadditions:craft_laser_relay_item");

			Advancement craftItemInterface = Advancement.Builder.advancement()
					.parent(craftLaserRelay)
					.display(new DisplayInfo(ActuallyBlocks.ITEM_INTERFACE.get().asItem().getDefaultInstance(),
							Component.translatable("achievement.actuallyadditions.craftItemInterface"),
							Component.translatable("achievement.actuallyadditions.craftItemInterface.desc"),
							null, FrameType.TASK, true, true, false))
					.addCriterion("item_interface", InventoryChangeTrigger.TriggerInstance.hasItems(
							ItemPredicate.Builder.item().of(ActuallyBlocks.ITEM_INTERFACE.get()).build())
					)
					.save(consumer, "actuallyadditions:craft_item_interface");
		}
	}
}
