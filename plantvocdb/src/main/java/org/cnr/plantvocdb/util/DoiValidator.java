package org.cnr.plantvocdb.util;

import java.util.regex.Pattern;

public class DoiValidator {

    public static boolean isValidDoi(String doi) {
        /**
         * Checks if a DOI is in the correct format.
         *
         * A valid DOI follows the format: 10.<registrant>/<suffix>
         * where:
         * - "10." is a fixed prefix
         * - <registrant> is a number (can have multiple digits)
         * - <suffix> can contain letters, numbers, dots, dashes, and slashes
         *
         * @param doi The DOI to validate
         * @return True if the DOI is valid, False otherwise
         */
        if (doi.startsWith("https://doi.org/")) {
            // Rimuove "https://doi.org/"
            doi = doi.replaceFirst("https://doi.org/", "");
        }
        String doiPattern = "^10\\.\\d{4,9}/[\\w.\\-_]+$";
        return Pattern.matches(doiPattern, doi);
    }

}
