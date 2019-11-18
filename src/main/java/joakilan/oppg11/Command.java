// Copyright (c) 2019 Joakim Skogø Langvand (jlangvand@gmail.com)
//
// GNU GENERAL PUBLIC LICENSE
//    Version 3, 29 June 2007
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

package joakilan.oppg11;

import java.util.Arrays;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Command class
 */
class Command {
  protected final String cmd;
  protected List<String> aliases;
  protected final String helpText;
  protected final CLIFunctions functions;
  protected final Method function;

  /**
   * Construct a new Command.
   *
   * @param cmd      Command name.
   * @param helpText Description and usage.
   * @param function Method to be run when called.
   */
  protected Command(String cmd, String helpText, CLIFunctions functionsObject) throws NoSuchMethodException {
    this.cmd = cmd.substring(3).toLowerCase();
    this.functions = functionsObject;
    this.function = functionsObject.getClass().getDeclaredMethod(cmd, List.class, Map.class);
    this.helpText = helpText;
    aliases = new ArrayList<String>();
  }

  /**
   * Run applied method and return the result as a String.
   *
   * @param params  Parameters(s)/subcommand(s).
   * @param options Options to be passed to the method.
   * @return The result.
   */
  protected String exec(List<String> params, Map<String, String> options)
      throws IllegalAccessException, InvocationTargetException {
    return (String) function.invoke(functions, params, options);
  }

  /**
   * Add an alias the Command should respond to.
   *
   * @param alias Alternative name.
   */
  protected void addAlias(String alias) {
    aliases.add(alias);
  }

  /**
   * Chech whether the command responds to the given name.
   *
   * @param cmd Command name.
   * @return True if command responds to the name.
   */
  protected boolean respondsToCommand(String cmd) {
    return this.cmd.equals(cmd) || aliases.contains(cmd);
  }
}
