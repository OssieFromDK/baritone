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
import burgertone.api.command.Command;
import burgertone.api.command.argument.IArgConsumer;
import burgertone.api.command.exception.CommandException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class SchematicaCommand extends Command {

    public SchematicaCommand(IBaritone burgertone) {
        super(burgertone, "schematica");
    }

    @Override
    public void execute(String label, IArgConsumer args) throws CommandException {
        args.requireMax(0);
        burgertone.getBuilderProcess().buildOpenSchematic();
    }

    @Override
    public Stream<String> tabComplete(String label, IArgConsumer args) {
        return Stream.empty();
    }

    @Override
    public String getShortDesc() {
        return "Builds the loaded schematic";
    }

    @Override
    public List<String> getLongDesc() {
        return Arrays.asList(
                "Builds the schematic currently open in Schematica.",
                "",
                "Usage:",
                "> schematica"
        );
    }
}
