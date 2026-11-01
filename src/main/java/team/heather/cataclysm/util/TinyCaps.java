package team.heather.cataclysm.util;

/** Converts lowercase ASCII letters to tiny caps, preserving uppercase and other characters. */
public final class TinyCaps {

    private static final String LETTERS = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀꜱᴛᴜᴠᴡxʏᴢ";

    private TinyCaps() {}

    public static String format(String text) {
        var result = new StringBuilder(text.length());
        for (char character : text.toCharArray()) {
            result.append(character >= 'a' && character <= 'z'
                    ? LETTERS.charAt(character - 'a')
                    : character);
        }
        return result.toString();
    }
}
