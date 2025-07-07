package th.co.wacoal.atech.pcms2.utilities;

public class CapitalizeFirstLetter {
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str; // Handle null or empty strings
        }
        // Capitalize the first character and concatenate with the rest of the string
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
