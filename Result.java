/*
    Parkrun result Object
    Daniel Mesham
    23 January 2017
*/

import java.lang.ArrayIndexOutOfBoundsException;

class Result {

    private String name, ageCat;
    private int position, numRuns, min, sec;

    // Format for table view :: Pos Name Time AgeCat NumRuns
    public static String TABLE_FORMAT = "%4s %-40s %-6s %-8s %4s";

    public Result() {
        name = "Unknown";
        ageCat = "Unknown";
        position = -1;
        numRuns = -1;
        min = -1;
        sec = -1;
    }


    /**
        This constructor takes in the table row that defines this result.
        It assumes the data has been split on "<tr>" and so only contains "</tr>"
    */
    public Result(String dataLine) {
        this();     // Set to default values in case it is an unknown runner

        String data[] = dataLine.split("</td><td>");

        // POSITION
        data[0] = data[0].replace("<td class=\"pos\">", "");
        position = Integer.parseInt(data[0]);

        // Check: If name is 'Unknown', there is no more data
        if (dataLine.contains("Unknown")) return;

        // NAME
        data[1] = data[1].replace("</a>","");
        name = data[1].split(">")[1];

        // TIME
        String timeData[] = data[2].split(":");

        if (timeData.length == 2) {
            min = Integer.parseInt(timeData[0]);
            sec = Integer.parseInt(timeData[1]);
        }
        // If time > 1hr, assumes < 2hr
        else {
            min = 60 + Integer.parseInt(timeData[1]);
            sec = Integer.parseInt(timeData[2]);
        }

        // AGECAT
        data[3] = data[3].replace("</a>","");
        ageCat = data[3].split(">")[1];

        // RUNS
        numRuns = Integer.parseInt(data[9].split("<")[0]);
    }

    /** SAMPLE RESULT:
    <tr>
        0 POS:    <td class="pos">1</td>
        1 NAME:   <td><a href="athletehistory?athleteNumber=2094470" target="_top">Justin ROWLES</a></td>
        2 TIME:   <td>17:59</td>
        3 AGECAT: <td><a href="../agecategorytable/?ageCat=SM25-29">SM25-29</a></td>
        4 GRAD:   <td>71.73 %</td>
        5 GNDR:   <td>M</td>
        6 GPOS:   <td>1</td>
        7 CLUB:   <td><a href="../clubhistory?clubNum=1187"/></td>
        8 PB:     <td>PB stays at 00:17:59</td>
        9 RUNS:   <td>32</td>
        9 PRCLUB: <td style="min-width:80px"/>
    </tr>
    */


    /* -- Useful Methods --- */

    /**
        Formats the result for viewing in a table.
    */
    public String tableRow() {
        if (isUnknown()) {
            return String.format(TABLE_FORMAT, position, name, "", "", "");
        }
        return String.format(TABLE_FORMAT, position, name, getTime(), ageCat, numRuns);
    }

    /* --- Accessor Methods --- */
    public boolean isUnknown()  { return numRuns == -1; }

    public String getName()     { return name; };
    public String getAgeCat()   { return ageCat; };

    public int getPosition()    { return position; };
    public int getNumRuns()     { return numRuns; };
    public int getMinutes()     { return min; };
    public int getSeconds()     { return sec; };

    public String getTime() {return String.format("%02d:%02d", min, sec); }

    /** Returns the time as a double with the number of minutes e.g. 20.5 = 20:30 */
    public double getTimeDouble() {
        return min + sec/60.0;
    }

    public boolean isMale() {
        if (ageCat.charAt(1) == 'M') return true;
        return false;
    }

    public double estAge() {
        String data[] = ageCat.substring(2).split("-");
        if (data.length == 1) return Integer.parseInt(data[0]);

        return Integer.parseInt(data[0]) + (Integer.parseInt(data[1]) - Integer.parseInt(data[0]))/2.0;
    }
}
