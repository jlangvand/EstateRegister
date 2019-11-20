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

import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Declare all commands the CLI should recognise.
 *
 * Every method representing a command must be prefixed with 'cmd', and accept
 * the parameters List params, Map options. The rest of the method name will be
 * used as 'name' for the command. That means the method 'cmdList(List params,
 * Map options' will represent a command 'list'. The body of the method will be
 * invoked by the interpreter when appropriate.
 */
public class EstateFunctions extends CLIFunctions {
  /* Declare or initialise fields as needed */
  Estates registry;
  String format = "%-15s %-12s Areal: %6.1fm²  Eier: %-20s Bruksnavn: %-10s\n";

  /* The constructor can be modified as needed */
  EstateFunctions(Estates registry) {
    this.registry = registry;
  }

  public String cmdNy(List<String> params, Map<String, String> options) {
    try {
      return registry.addEstate(options) ? "Eiendom lagt til" : "Ugyldig format";
    } catch (DuplicateEntryException e) {
      return "En eiendom med denne ID er allerede registrert";
    } catch (Exception e) {
      return "En feil oppstod";
    }
  }

  public String cmdListe(List<String> params, Map<String, String> options) {
    StringBuilder sb = new StringBuilder();
    registry.getAllEstates().forEach(estate -> sb.append(estate.toString(format)));
    return registry.getNumberOfEstates() == 0 ? "Ingen eiendommer registrert" : sb.toString().stripTrailing();
  }

  public String cmdAntall(List<String> params, Map<String, String> options) {
    return String.format("Totalt %d eiendommer er registrert", registry.getNumberOfEstates());
  }

  public String cmdHent(List<String> params, Map<String, String> options) {
    String idParamName = "id";
    String idParamValue = options.get(idParamName);
    StringBuilder sb = new StringBuilder();
    List<Estate> result = new ArrayList<Estate>();
    if (idParamValue != null) {
      if (idParamValue.matches("^\\d{4}-\\d{2,3}/\\d{2,3}")) {
        Estate estate = registry.getEstateByID(idParamValue).orElse(null);
        sb.append(estate == null ? "" : estate.toString(format));
      } else {
        sb.append("Ugyldig format. ID oppgis som 'knr-bnr/gnr'");
      }
    } else if (options.get("bnr") != null || options.get("gnr") != null || options.get("knr") != null) {
      result = registry.getAllEstates();
      for (String filterBy : new ArrayList<String>(options.keySet())) {
        result = result.stream().filter(estate -> estate.getParamByName(filterBy).equals(options.get(filterBy)))
            .collect(Collectors.toList());
      }
      result.forEach(estate -> {
        sb.append(estate.toString(format));
      });
    } else if (params.contains("alle")) {
      sb.append(cmdListe(null, null));
      result = registry.getAllEstates();
    }

    if (params.contains("gjennomsnitt") && !result.isEmpty()) {
      double totalArea = 0;
      for (Estate estate : result) {
        totalArea += estate.getArea();
      }
      sb.append(String.format("\n\nGjennomsnittlig areal: %.2fm²", totalArea / result.size()));
    }

    return sb.toString().isEmpty() ? "Ingen eiendom funnet" : sb.toString();
  }

  public String cmdGjennomsnitt(List<String> params, Map<String, String> options) {
    double totalArea = 0;
    for (Estate estate : registry.getAllEstates()) {
      totalArea += estate.getArea();
    }
    return String.format("Gjennomsnittlig areal: %.2fm²", totalArea / registry.getNumberOfEstates());
  }
}
