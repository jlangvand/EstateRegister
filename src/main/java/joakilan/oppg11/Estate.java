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

/**
 * Klasse for eiendommer.
 *
 * Jeg ville egentlig løst dette med et bibliotek som kan lete opp kommune
 * basert på navn eller nummer. Da kunne en hatt en klasse Municipality, og
 * Estate holder et objekt av denne klassen.
 *
 * Det ligger inne mutatorer for navn, eiernavn, areal og kommunenavn. Alt dette
 * er verdier som kan komme til å forandre seg.
 */
class Estate {
  private String municipalityName;
  private int municipalityNumber;
  private int lotNumber;
  private int sectionNumber;
  private String estateName;
  private double area;
  private String ownerName;

  Estate(String municipalityName, int municipalityNumber, int lotNumber, int sectionNumber, double area,
      String ownerName, String estateName) {
    this.municipalityName = municipalityName;
    this.municipalityNumber = municipalityNumber;
    this.lotNumber = lotNumber;
    this.sectionNumber = sectionNumber;
    this.estateName = estateName;
    this.area = area;
    this.ownerName = ownerName;
  }

  Estate(String municipalityName, String municipalityNumber, String lotNumber, String sectionNumber, String area,
      String ownerName, String estateName) {
    this.municipalityName = municipalityName;
    this.municipalityNumber = Integer.parseInt(municipalityNumber);
    this.lotNumber = Integer.parseInt(lotNumber);
    this.sectionNumber = Integer.parseInt(sectionNumber);
    this.estateName = estateName;
    this.area = Double.parseDouble(area);
    this.ownerName = ownerName;
  }

  Estate(String municipalityName, int municipalityNumber, int lotNumber, int sectionNumber, double area,
      String ownerName) {
    this(municipalityName, municipalityNumber, lotNumber, sectionNumber, area, ownerName, "");
  }

  Estate(String municipalityName, String municipalityNumber, String lotNumber, String sectionNumber, String area,
      String ownerName) {
    this(municipalityName, municipalityNumber, lotNumber, sectionNumber, area, ownerName, "");
  }

  /**
   * @return the municipalityName
   */
  public String getMunicipalityName() {
    return municipalityName;
  }

  /**
   * @return the municipalityNumber
   */
  public int getMunicipalityNumber() {
    return municipalityNumber;
  }

  /**
   * @return the lotNumber
   */
  public int getLotNumber() {
    return lotNumber;
  }

  /**
   * @return the sectionNumber
   */
  public int getSectionNumber() {
    return sectionNumber;
  }

  /**
   * @return the area
   */
  public double getArea() {
    return area;
  }

  /**
   * @return the ownerName
   */
  public String getOwnerName() {
    return ownerName;
  }

  /**
   * @return the estateName
   */
  public String getEstateName() {
    return estateName;
  }

  /**
   * @param area the area to set
   */
  public void setArea(double area) {
    this.area = area;
  }

  /**
   * @param ownerName the ownerName to set
   */
  public void setOwnerName(String ownerName) {
    this.ownerName = ownerName;
  }

  /**
   * @param municipalityName the municipalityName to set
   */
  public void setMunicipalityName(String municipalityName) {
    this.municipalityName = municipalityName;
  }

  /**
   * @param estateName the estateName to set
   */
  public void setEstateName(String estateName) {
    this.estateName = estateName;
  }

  /**
   * Get standardised, unique ID for the property.
   *
   * @return
   */
  public String getUniqueID() {
    return String.format("%d-%d/%d", municipalityNumber, lotNumber, sectionNumber);
  }

  public String getParamByName(String param) {
    switch (param) {
    case "gnr":
      return String.format("%d", lotNumber);
    case "bnr":
      return String.format("%d", sectionNumber);
    case "knr":
      return String.format("%d", municipalityNumber);
    default:
      return null;
    }
  }

  static String capitalise(String in) {
    if (in.isEmpty()) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    Arrays.asList(in.split(" ")).forEach(str -> sb.append(str.substring(0, 1).toUpperCase() + str.substring(1) + " "));
    return sb.toString().stripTrailing();
  }

  @Override
  public String toString() {
    return String.format("%s, %12s, Area: %.1fm², Owner name: %s, Estate name: %s", getUniqueID(),
        capitalise(municipalityName), area, capitalise(ownerName), capitalise(estateName));
  }

  public String toString(String format) {
    return String.format(format, getUniqueID(), capitalise(municipalityName), area, capitalise(ownerName),
        capitalise(estateName));
  }
}
