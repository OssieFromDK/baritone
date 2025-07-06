/*
 * This file is part of Baritone.
 *
 * Baritone is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Baritone is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Baritone.  If not, see <https://www.gnu.org/licenses/>.
 */

package burgertone.launch.mixins;

import burgertone.api.BaritoneAPI;
import burgertone.api.IBaritone;
import burgertone.api.event.events.PlayerUpdateEvent;
import burgertone.api.event.events.SprintStateEvent;
import burgertone.api.event.events.type.EventState;
import burgertone.behavior.LookBehavior;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Group;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author Brady
 * @since 8/1/2018
 */
@Mixin(LocalPlayer.class)
public class MixinClientPlayerEntity {
    @Unique
    private static final MethodHandle MAY_FLY = burgertone$resolveMayFly();

    @Unique
    private static MethodHandle burgertone$resolveMayFly() {
        try {
            var lookup = MethodHandles.publicLookup();
            return lookup.findVirtual(LocalPlayer.class, "mayFly", MethodType.methodType(boolean.class));
        } catch (NoSuchMethodException e) {
            return null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/client/player/AbstractClientPlayer.tick()V",
                    shift = At.Shift.AFTER
            )
    )
    private void onPreUpdate(CallbackInfo ci) {
        IBaritone burgertone = BaritoneAPI.getProvider().getBaritoneForPlayer((LocalPlayer) (Object) this);
        if (burgertone != null) {
            burgertone.getGameEventHandler().onPlayerUpdate(new PlayerUpdateEvent(EventState.PRE));
        }
    }

    @Redirect(
            method = "aiStep",
            at = @At(
                    value = "FIELD",
                    target = "net/minecraft/world/entity/player/Abilities.mayfly:Z"
            )
    )
    @Group(name = "mayFly", min = 1, max = 1)
    private boolean isAllowFlying(Abilities capabilities) {
        IBaritone burgertone = BaritoneAPI.getProvider().getBaritoneForPlayer((LocalPlayer) (Object) this);
        if (burgertone == null) {
            return capabilities.mayfly;
        }
        return !burgertone.getPathingBehavior().isPathing() && capabilities.mayfly;
    }

    @Redirect(
        method = "aiStep",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;mayFly()Z"
        )
    )
    @Group(name = "mayFly", min = 1, max = 1)
    private boolean onMayFlyNeoforge(LocalPlayer instance) throws Throwable {
        IBaritone burgertone = BaritoneAPI.getProvider().getBaritoneForPlayer((LocalPlayer) (Object) this);
        if (burgertone == null) {
            return (boolean) MAY_FLY.invokeExact(instance);
        }
        return !burgertone.getPathingBehavior().isPathing() && (boolean) MAY_FLY.invokeExact(instance);
    }

    @Redirect(
            method = "aiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Input;sprint()Z"
            )
    )
    private boolean redirectSprintInput(final Input instance) {
        IBaritone burgertone = BaritoneAPI.getProvider().getBaritoneForPlayer((LocalPlayer) (Object) this);
        if (burgertone == null) {
            return instance.sprint();
        }
        SprintStateEvent event = new SprintStateEvent();
        burgertone.getGameEventHandler().onPlayerSprintState(event);
        if (event.getState() != null) {
            return event.getState();
        }
        if (burgertone != BaritoneAPI.getProvider().getPrimaryBaritone()) {
            // hitting control shouldn't make all bots sprint
            return false;
        }
        return instance.sprint();
    }

    @Inject(
            method = "rideTick",
            at = @At(
                    value = "HEAD"
            )
    )
    private void updateRidden(CallbackInfo cb) {
        IBaritone burgertone = BaritoneAPI.getProvider().getBaritoneForPlayer((LocalPlayer) (Object) this);
        if (burgertone != null) {
            ((LookBehavior) burgertone.getLookBehavior()).pig();
        }
    }

    @Redirect(
            method = "aiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;tryToStartFallFlying()Z"
            )
    )
    private boolean tryToStartFallFlying(final LocalPlayer instance) {
        IBaritone burgertone = BaritoneAPI.getProvider().getBaritoneForPlayer(instance);
        if (burgertone != null && burgertone.getPathingBehavior().isPathing()) {
            return false;
        }
        return instance.tryToStartFallFlying();
    }
}
