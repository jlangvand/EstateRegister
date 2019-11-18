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
 * Hovedklasse
 */
class App {
  public static void main(String[] args) {
    /* Informasjon og hjelpetekst */
    String helpformat = "%-30s %-30s%n";
    System.out.println("Dette programmet er lisensiert under GNU GPLv3");
    System.out.println("Copyright © 2019, Joakim Skogø Lagvand");
    System.out.println();
    System.out.printf(helpformat, "hent alle", "Se alle eiendommer");
    System.out.printf(helpformat, "hent --bnr=123", "Se alle eiendommer med bruksnummer 123");
    System.out.printf(helpformat, "     --gnr=123", "...med gårdsnummer 123");
    System.out.printf(helpformat, "     --gnr=1234", "...i kommune 1234");
    System.out.printf(helpformat, "", "(disse tre parametrene kan kombineres)");
    System.out.printf(helpformat, "hent --id=1234-56/789", "Hent en bestemt eiendom");
    System.out.printf(helpformat, "hent [parametre] gjennomsnitt", "Se gjennomsnittlig areal på eiendommene");
    System.out.printf(helpformat, "ny --parameter=verdi", "Legg til ny eiendom");
    System.out.println();
    System.out.println("Eksempel:");
    System.out.println(
        "ny --gnr=123 --bnr=45 --knr=6401 --knavn=Trondheim --eiernavn=\"Jan Johansen\" --areal=123.45 [--bruksavn=Kvilstua]");
    System.out.println();

    /* Initialiserer CLI */
    CLI cli = new CLI(new EstateFunctions(new Estates()));

    /* Norske oversettelser */
    cli.setNoSuchCommandMessage("Ugyldig kommando");
    cli.setNoSuchMethodMessage("Metode ikke definert");

    /* Legg inn testdata */
    cli.parse("ny --knavn=Gloppen --knr=1445 --gnr=77 --bnr=631" + " --areal=1017,6 --eiernavn=\"Jens Olsen\"");
    cli.parse("ny --knavn=Gloppen --knr=1445 --gnr=77 --bnr=131"
        + " --areal=661,3 --eiernavn=\"Nicolay Madsen\" --bruksnavn=\"Syningom\"");
    cli.parse("ny --knavn=Gloppen --knr=1445 --gnr=75 --bnr=19"
        + " --areal=650,6 --eiernavn=\"Evilyn Jensen\" --bruksnavn=\"Fugletun\"");
    cli.parse("ny --knavn=Gloppen --knr=1445 --gnr=74 --bnr=188" + " --areal=1457,2 --eiernavn=\"Karl Ove Bråten\"");
    cli.parse("ny --knavn=Gloppen --knr=1445 --gnr=69 --bnr=47"
        + " --areal=1339,4 --eiernavn=\"Elsa Indregård\" --bruksnavn=\"Høiberg\"");
    /* CLI tar kontroll over programflyten */
    cli.cli();

    // cli.parse("liste");
  }
}
