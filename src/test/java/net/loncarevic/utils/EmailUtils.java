package net.loncarevic.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Utility methods for processing email content. */
public class EmailUtils {
  /**
   * Extracts the first URL found in the given text.
   *
   * @param text The input text
   * @return Extracted URL or null if not found
   */
  public static String extractUrlFromText(String text) {
    Pattern pattern = Pattern.compile("https?://\\S+");
    Matcher matcher = pattern.matcher(text);
    return matcher.find() ? matcher.group() : null;
  }
}
