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

import java.util.Optional;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * Her bruker jeg ArrayList, siden den har god ytelse på søk. Vi regner ikke med
 * at det ofte blir lagt til eller fjernet ellementer (da hadde jeg brukt
 * LinkedList).
 */
public class Estates {
  private List<Estate> estates;

  Estates() {
    estates = new ArrayList<Estate>();
  }

  /**
   * Add a new estate (with name).
   *
   * @param municipalityName   Municipality name.
   * @param municipalityNumber Municipality number.
   * @param lotNumber          Lot number.
   * @param sectionNumber      Section number.
   * @param area               Net area of building.
   * @param ownerName          Name of owner.
   * @param estateName         Name of estate.
   */
  public void addEstate(String municipalityName, int municipalityNumber, int lotNumber, int sectionNumber, double area,
      String ownerName, String estateName) {
    estates
        .add(new Estate(municipalityName, municipalityNumber, lotNumber, sectionNumber, area, ownerName, estateName));
  }

  /**
   *
   * Add a new estate.
   *
   * @param data Key, value map containing parameters for new estate.
   * @return true if operation succeeded.
   * @throws DuplicateEntryException if estate is already in registry.
   */
  public boolean addEstate(Map<String, String> data) throws DuplicateEntryException {
    String municipalityName = data.get("knavn").replace("_", " ").replace("\"", "");
    String municipalityNumber = data.get("knr");
    String lotNumber = data.get("bnr");
    String sectionNumber = data.get("gnr");
    String area = data.get("areal");
    String ownerName = data.get("eiernavn").replace("_", " ").replace("\"", "");
    String estateName = data.get("bruksnavn") == null ? "" : data.get("bruksnavn").replace("_", " ").replace("\"", "");
    String id = String.format("%s-%s/%s", municipalityNumber, lotNumber, sectionNumber);
    if (this.getEstateByID(id).isEmpty()) {
      return estateName == null
          ? estates.add(new Estate(municipalityName, municipalityNumber, lotNumber, sectionNumber, area, ownerName))
          : estates.add(
              new Estate(municipalityName, municipalityNumber, lotNumber, sectionNumber, area, ownerName, estateName));
    } else {
      throw new DuplicateEntryException("Estate already exists");
    }

  }

  /**
   * Removes an estate given by ID string (####-##/##)
   *
   * @param id ID string.
   * @throws IllegalArgumentException if ID is malformed.
   */
  public void removeEstate(String id) throws IllegalArgumentException {
    if (id.matches("^\\d{4}-\\d{2,4}/\\d{2,4}$")) {
      estates.stream().filter(e -> e.getUniqueID().equals(id)).findFirst().ifPresent(e -> {
        estates.remove(e);
      });
    } else {
      throw new IllegalArgumentException("Malformed ID string");
    }
  }

  /**
   * Get all estates.
   *
   * @return List of all estate objects.
   */
  public List<Estate> getAllEstates() {
    return new ArrayList<Estate>(estates);
  }

  /**
   * Get an estate by ID string (####-##/##).
   *
   * @param id Unique ID string.
   * @return Optional representing an estate.
   * @throws IllegalArgumentException if ID is malformed.
   */
  public Optional<Estate> getEstateByID(String id) throws IllegalArgumentException {
    if (id.matches("^\\d{4}-\\d{2,4}/\\d{2,4}$")) {
      return estates.stream().filter(e -> e.getUniqueID().equals(id)).findFirst();
    } else {
      throw new IllegalArgumentException("Malformed ID string");
    }
  }

  /**
   * Get number of estates in the registry.
   *
   * @return number of estates.
   */
  public int getNumberOfEstates() {
    return estates.size();
  }

  /**
   * Get the average area of all estates.
   *
   * @return The average area.
   */
  public double getAverageArea() {
    double out = 0;
    for (Estate e : estates) {
      out += e.getArea();
    }
    return out / estates.size();
  }
}
