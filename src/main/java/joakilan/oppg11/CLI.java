
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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Main class for the command line interface.
 *
 * TODO: Short command name is being ignored for now.
 */
class CLI {
  List<Command> commands;
  String noSuchCommandMessage = "No such command";
  String noSuchMethodMessage = "Internal exception: no such method";

  /**
   * Initialise new CLI.
   */
  CLI(CLIFunctions functions) {
    commands = new ArrayList<Command>();
    functions.getCommands().stream().filter(m -> m.getName().startsWith("cmd")).forEach(method -> {
      try {
        addCommand(method.getName(), "", functions);
      } catch (NoSuchMethodException e) {
        System.out.println(noSuchMethodMessage);
      }
    });
  }

  /**
   * Starts a basic command line interface.
   */
  public void cli() {
    try (Scanner in = new Scanner(System.in)) {
      while (true) {
        try {
          System.out.printf("%n$ ");
          System.out.println("\n" + parse(in.nextLine()).stripTrailing());
        } catch (UserInterruptException e) {
          /* Clean exit */
          break;
        } catch (Exception e) {
          /* Should ideally be unreachable; uncaught exception */
          e.printStackTrace();

          /* Exit the CLI */
          break;
        }
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Set "no such command" message
   *
   * @param noSuchCommandMessage message
   */
  public void setNoSuchCommandMessage(String noSuchCommandMessage) {
    this.noSuchCommandMessage = noSuchCommandMessage;
  }

  /**
   * Set "no such method" message
   *
   * @param noSuchMethodMessage message
   */
  public void setNoSuchMethodMessage(String noSuchMethodMessage) {
    this.noSuchMethodMessage = noSuchMethodMessage;
  }

  private void addCommand(String cmd, String helpText, CLIFunctions functions) throws NoSuchMethodException {
    commands.add(new Command(cmd, helpText, functions));
  }

  /* Returns the first word of the string. */
  private String getNthWord(String in, int n) {
    assert in.length() > n - 1 : "Asked for word " + n + " from a string of " + in.length() + " words.";
    return in.split(" ")[n - 1];
  }

  private Map<String, String> getArgumentsFromString(String in) {
    Map<String, String> out = new HashMap<String, String>();
    in = in.replaceAll("(=\"[A-Åa-å0-9]+)(?:[A-Åa-å0-9]*(\\s+))+([A-Åa-å0-9]*[A-Åa-å0-9]\")", "$1_$3");
    in = in.replace(",", ".");
    Arrays.asList(in.split(" ")).forEach(s -> {
      if (s.matches("^--\\w+=\\S+$") || s.matches("^--\\w*=\"\\S[\\S\\s]*\\S\"$")) {
        String arg = s.split("=")[0].replace("-", "");
        out.put(arg, s.split("=")[1]);
      }
    });
    return out;
  }

  /*  */
  private List<String> getParamsFromString(String in) {
    List<String> out = new ArrayList<String>();
    Arrays.asList(in.split(" ")).forEach(s -> {
      if (s.matches("^[^-]\\w*$")) {
        out.add(s);
      }
    });
    return out;
  }

  /**
   * Parse string from user input.
   *
   * @param in String to parse.
   */
  protected String parse(String in) throws UserInterruptException {
    in = in.toLowerCase();
    String cmd = getNthWord(in, 1);
    Map<String, String> args = getArgumentsFromString(in);
    List<String> params = getParamsFromString(in);
    params.remove(cmd);

    /* Special cases */
    switch (cmd) {
    case "help":
      // TODO: Return helptext
      break;
    case "exit":
      throw new UserInterruptException("User exit");
    default:
    }

    StringBuilder out = new StringBuilder();

    commands.stream().filter(c -> c.respondsToCommand(cmd)).findFirst().ifPresent(command -> {
      try {
        out.append(command.exec(params, args));
      } catch (IllegalAccessException e) {
        System.out.println("Illegal access exception");
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        System.out.println("Invocation target exception");
        e.printStackTrace();
      }
    });

    return out.toString().equals("") ? noSuchCommandMessage : out.toString();
  }
}
