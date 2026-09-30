// TRACKED HASH: c7da1c1b82a8d0a88d3f2a674ea41109be948d6c
package dev.sporran.injects.client.gui.screens.inventory;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.ContainerScreenEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.sporran.helpers.mixin.CreateStatic;
import dev.sporran.injections.client.gui.screens.inventory.AbstractContainerScreenInjection;
import dev.sporran.mixin.client.gui.screens.inventory.AbstractContainerScreenAccessor;
import dev.sporran.util.SporranHelper;

import java.util.List;
import java.util.Optional;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenInject extends Screen implements AbstractContainerScreenInjection {
    protected AbstractContainerScreenInject(Component component) {
        super(component);
    }

    @Shadow @Nullable protected Slot hoveredSlot;
    @Shadow protected int leftPos;
    @Shadow protected int topPos;
    @Shadow protected int imageWidth;
    @Shadow protected int imageHeight;

    // Sporran: Moved ContainerScreenEvent.Render.Background to ScreenInject

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;isHighlightable()Z"))
    private boolean sporran$checkShouldUseRenderHighlightOverride(Slot instance, Operation<Boolean> original, @Local(argsOnly = true) GuiGraphics guiGraphics, @Local(argsOnly = true, ordinal = 0) int mouseX, @Local(argsOnly = true, ordinal = 1) int mouseY, @Local(argsOnly = true) float partialTick) {
        if (SporranHelper.INSTANCE.hasMethodOverride(this.getClass(), AbstractContainerScreen.class, "renderSlotHighlight", GuiGraphics.class, Slot.class, int.class, int.class, float.class)) {
            this.renderSlotHighlight(guiGraphics, instance, mouseX, mouseY, partialTick);
            return false;
        }

        return original.call(instance);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderLabels(Lnet/minecraft/client/gui/GuiGraphics;II)V", shift = At.Shift.AFTER))
    private void sporran$callRenderForegroundEvent(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        NeoForge.EVENT_BUS.post(new ContainerScreenEvent.Render.Foreground((AbstractContainerScreen<?>) (Object) this, guiGraphics, mouseX, mouseY));
    }

    @CreateStatic
    private static void renderSlotHighlight(GuiGraphics guiGraphics, int x, int y, int blitOffset, int slotColor) {
        AbstractContainerScreenInjection.sporran$slotColor.set(slotColor);
        // this is called: workarounds.
        AbstractContainerScreenAccessor.invokeRenderSlotHighlight(guiGraphics, x, y, blitOffset);
        AbstractContainerScreenInjection.sporran$slotColor.set(defaultSlotColor);
    }

    @ModifyExpressionValue(method = "renderSlotHighlight", at = @At(value = "CONSTANT", target = "Lnet/minecraft/client/gui/GuiGraphics;fillGradient(Lnet/minecraft/client/renderer/RenderType;IIIIIII)V", args = "intValue=-2130706433"))
    private static int sporran$useCustomSlotColor(int original) {
        if (original != AbstractContainerScreenInjection.defaultSlotColor)
            return original;

        return AbstractContainerScreenInjection.sporran$slotColor.get();
    }

    protected void renderSlotHighlight(GuiGraphics guiGraphics, Slot slot, int mouseX, int mouseY, float partialTick) {
        if (slot.isHighlightable()) {
            renderSlotHighlight(guiGraphics, slot.x, slot.y, 0, this.getSlotColor(slot.index));
        }
    }

    @WrapOperation(method = "renderTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;II)V"))
    private void sporran$wrapTooltipRenderWithStack(GuiGraphics instance, Font font, List<Component> tooltipLines, Optional<TooltipComponent> visualTooltipComponent, int mouseX, int mouseY, Operation<Void> original, @Local ItemStack stack) {
        instance.sporran$setTooltipStack(stack);
        original.call(instance, font, tooltipLines, visualTooltipComponent, mouseX, mouseY);
        instance.sporran$setTooltipStack(ItemStack.EMPTY);
    }

    @Inject(method = "renderFloatingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V"))
    public void sporran$getItemFont(GuiGraphics guiGraphics, ItemStack stack, int x, int y, String text, CallbackInfo ci, @Share("font") LocalRef<Font> font) {
        font.set(IClientItemExtensions.of(stack).getFont(stack, IClientItemExtensions.FontContext.ITEM_COUNT));

        if (font.get() == null)
            font.set(this.font);
    }

    @ModifyArg(method = "renderFloatingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V"))
    public Font sporran$useForgeItemFont(Font oldFont, @Share("font") LocalRef<Font> newFont) {
        return newFont.get();
    }

    @Inject(method = "renderSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;isFake()Z"), cancellable = true)
    private void sporran$tryRenderSlotContents(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci, @Local String s, @Local(ordinal = 0) ItemStack stack) {
        if (SporranHelper.INSTANCE.hasMethodOverride(this.getClass(), AbstractContainerScreen.class, "renderSlotContents", GuiGraphics.class, ItemStack.class, Slot.class, String.class)) {
            this.renderSlotContents(guiGraphics, stack, slot, s);
            guiGraphics.pose().popPose();
            ci.cancel();
        }
    }

    protected void renderSlotContents(GuiGraphics guiGraphics, ItemStack stack, Slot slot, @Nullable String countString) {
        int k = slot.x + slot.y * this.imageWidth;
        if (slot.isFake()) {
            guiGraphics.renderFakeItem(stack, slot.x, slot.y, k);
        } else {
            guiGraphics.renderItem(stack, slot.x, slot.y, k);
        }

        guiGraphics.renderItemDecorations(this.font, stack, slot.x, slot.y, countString);
    }

    // Sporran TODO: how do we implement the isActiveAndMatches patch properly

    @ModifyVariable(method = "mouseClicked", at = @At("STORE"), ordinal = 1)
    public boolean sporran$ensureNoSlotInEmptySpaceWhenClicked(boolean hasClicked, @Local Slot slot) {
        if (slot != null)
            return false;
        else
            return hasClicked;
    }

    @Inject(method = "mouseReleased", at = @At("HEAD"))
    public void sporran$callParentRelease(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        super.mouseReleased(mouseX, mouseY, button);
    }

    @ModifyVariable(method = "mouseReleased", at = @At("STORE"))
    public boolean sporran$ensureNoSlotInEmptySpaceWhenReleased(boolean hasClicked, @Local Slot slot) {
        if (slot != null)
            return false;
        else
            return hasClicked;
    }

    @ModifyExpressionValue(method = "mouseReleased", at = @At(value = "FIELD", target = "Lnet/minecraft/world/inventory/Slot;container:Lnet/minecraft/world/Container;", ordinal = 0))
    private Container sporran$forceContainerMatch(Container original, @Local(ordinal = 0) Slot slot) {
        return slot.container;
    }

    @Definition(id = "slot2", local = @Local(type = Slot.class, ordinal = 1))
    @Definition(id = "container", field = "Lnet/minecraft/world/inventory/Slot;container:Lnet/minecraft/world/Container;")
    @Definition(id = "slot", local = @Local(type = Slot.class, ordinal = 0))
    @Expression("slot2.container == slot.container")
    @ModifyExpressionValue(method = "mouseReleased", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean sporran$useForgeInventoryCheck(boolean original, @Local(ordinal = 0) Slot slot, @Local(ordinal = 1) Slot slot2) {
        return original || slot.isSameInventory(slot2);
    }

    // Sporran TODO: do we implement MC-146650?

    @Nullable
    public Slot getSlotUnderMouse() {
        return this.hoveredSlot;
    }

    public int getGuiLeft() {
        return this.leftPos;
    }

    public int getGuiTop() {
        return this.topPos;
    }

    public int getXSize() {
        return this.imageWidth;
    }

    public int getYSize() {
        return this.imageHeight;
    }

    protected int slotColor = -2130706433;
    public int getSlotColor(int index) {
        return slotColor;
    }

    @Override
    public void sporran$setSlotColor(int slotColor) {
        this.slotColor = slotColor;
    }
}