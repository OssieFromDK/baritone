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

package burgertone.command.defaults;

import burgertone.api.IBaritone;
import burgertone.api.command.ICommand;

import java.util.*;

public final class DefaultCommands {

    private DefaultCommands() {
    }

    public static List<ICommand> createAll(IBaritone burgertone) {
        Objects.requireNonNull(burgertone);
        List<ICommand> commands = new ArrayList<>(Arrays.asList(
                new HelpCommand(burgertone),
                new SetCommand(burgertone),
                new CommandAlias(burgertone, Arrays.asList("modified", "mod", "burgertone", "modifiedsettings"), "List modified settings", "set modified"),
                new CommandAlias(burgertone, "reset", "Reset all settings or just one", "set reset"),
                new GoalCommand(burgertone),
                new GotoCommand(burgertone),
                new PathCommand(burgertone),
                new ProcCommand(burgertone),
                new ETACommand(burgertone),
                new VersionCommand(burgertone),
                new RepackCommand(burgertone),
                new BuildCommand(burgertone),
                //new SchematicaCommand(burgertone),
                new LitematicaCommand(burgertone),
                new ComeCommand(burgertone),
                new AxisCommand(burgertone),
                new ForceCancelCommand(burgertone),
                new GcCommand(burgertone),
                new InvertCommand(burgertone),
                new TunnelCommand(burgertone),
                new RenderCommand(burgertone),
                new FarmCommand(burgertone),
                new FollowCommand(burgertone),
                new PickupCommand(burgertone),
                new ExploreFilterCommand(burgertone),
                new ReloadAllCommand(burgertone),
                new SaveAllCommand(burgertone),
                new ExploreCommand(burgertone),
                new BlacklistCommand(burgertone),
                new FindCommand(burgertone),
                new MineCommand(burgertone),
                new ClickCommand(burgertone),
                new SurfaceCommand(burgertone),
                new ThisWayCommand(burgertone),
                new WaypointsCommand(burgertone),
                new CommandAlias(burgertone, "sethome", "Sets your home waypoint", "waypoints save home"),
                new CommandAlias(burgertone, "home", "Path to your home waypoint", "waypoints goto home"),
                new SelCommand(burgertone),
                new ElytraCommand(burgertone)
        ));
        ExecutionControlCommands prc = new ExecutionControlCommands(burgertone);
        commands.add(prc.pauseCommand);
        commands.add(prc.resumeCommand);
        commands.add(prc.pausedCommand);
        commands.add(prc.cancelCommand);
        return Collections.unmodifiableList(commands);
    }
}
