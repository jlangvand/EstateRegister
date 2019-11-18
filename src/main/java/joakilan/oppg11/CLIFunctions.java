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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Declare all commands the CLI should recognise.
 *
 * Every method representing a command must be prefixed with 'cmd', and accept
 * the parameters 'List<String> params, Map<String, String> options'. The rest
 * of the method name will be used as 'name' for the command. That means the
 * method 'cmdList(List<String> params, Map<String, String> options)' will
 * represent a command 'list'. The body of the method will be invoked by the
 * interpreter when appropriate.
 */
abstract class CLIFunctions {
  /* Declare or initialise fields as needed */

  protected List<Method> getCommands() {
    return Arrays.asList(this.getClass().getDeclaredMethods()).stream().filter(m -> m.getName().startsWith("cmd"))
        .collect(Collectors.toList());
  }
}
