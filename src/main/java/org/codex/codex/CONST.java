package org.codex.codex;

public class CONST {

    public static final int maxParticipants = 5; // max number of participants per game
    public static final int maxNameLen = 20; // max number of chars of game name and nickname

    public static final String serverName = "Server";
    public static final String serverLocalHost = "127.0.0.1";
    public static final int serverLocalRMIPort = 20345;
    public static final int serverLocalSocketPort = 20350;


    public static final int resourceCards = 40;
    public static final int goldCards = 40;
    public static final int starterCards = 6;
    public static final int objectiveCards = 16;

    public static final String resourceAssetsDir = "src/main/resources/org/codex/codex/assets";
    public static final String resourceAssets = CONST.resourceAssetsDir + "/resource.json";
    public static final String goldAssets = CONST.resourceAssetsDir + "/gold.json";
    public static final String starterAssets = CONST.resourceAssetsDir + "/starter.json";
    public static final String objectiveAssets = CONST.resourceAssetsDir + "/objective.json";


    public static final int tableViewPadding = 5; // min 10 in way to show all the chars of coordinates like "(123,123)"

    public static final int tableWidth = 360; // min 323 + tableViewPadding   | 323 = (1(starter card) * cardWidth) + 2 * ((resourceCards + goldCards) * cardWidth - 1)
    public static final int tableHeight = 360; // min 323 + tableViewPadding  | 323 = (1(starter card) * cardHeight) + 2 * ((resourceCards + goldCards) * cardHeight - 1)

    public static final int starterCardX = 180; // (tableWidth - cardWidth) / 2     |   cardWidth = 3
    public static final int starterCardY = 180; // (tableHeight - cardHeight) / 2   |   cardHeight = 3


    public static final int guiWidth = 1200;
    public static final int guiHeight = 800;
    public static final int guiCardWidth = 90;
    public static final int guiCardHeight = 90;

    public static final int differenceGuiTuiX = 4845;
    public static final int differenceGuiTuiY = 5145;


    public static final String TEXT_TITLE = "                          $$\\                                                $$\\                                  $$\\ $$\\           \n" +
            "                          $$ |                                               $$ |                                 $$ |\\__|          \n" +
            " $$$$$$$\\  $$$$$$\\   $$$$$$$ | $$$$$$\\  $$\\   $$\\       $$$$$$$\\   $$$$$$\\ $$$$$$\\   $$\\   $$\\  $$$$$$\\  $$$$$$\\  $$ |$$\\  $$$$$$$\\ \n" +
            "$$  _____|$$  __$$\\ $$  __$$ |$$  __$$\\ \\$$\\ $$  |      $$  __$$\\  \\____$$\\\\_$$  _|  $$ |  $$ |$$  __$$\\ \\____$$\\ $$ |$$ |$$  _____|\n" +
            "$$ /      $$ /  $$ |$$ /  $$ |$$$$$$$$ | \\$$$$  /       $$ |  $$ | $$$$$$$ | $$ |    $$ |  $$ |$$ |  \\__|$$$$$$$ |$$ |$$ |\\$$$$$$\\  \n" +
            "$$ |      $$ |  $$ |$$ |  $$ |$$   ____| $$  $$<        $$ |  $$ |$$  __$$ | $$ |$$\\ $$ |  $$ |$$ |     $$  __$$ |$$ |$$ | \\____$$\\ \n" +
            "\\$$$$$$$\\ \\$$$$$$  |\\$$$$$$$ |\\$$$$$$$\\ $$  /\\$$\\       $$ |  $$ |\\$$$$$$$ | \\$$$$  |\\$$$$$$  |$$ |     \\$$$$$$$ |$$ |$$ |$$$$$$$  |\n" +
            " \\_______| \\______/  \\_______| \\_______|\\__/  \\__|      \\__|  \\__| \\_______|  \\____/  \\______/ \\__|      \\_______|\\__|\\__|\\_______/\n";

    // ---------- System.out.print(...) -------------------------------------------------------------------

    // Emoji
    public static final String TEXT_EMPTY_SPACE_TABLE = "▪\uFE0F";
    public static final String TEXT_EMPTY_SPACE = " \uFE0F";
    public static final String TEXT_EMOJI_BLANK = "◻\uFE0F";
    public static final String TEXT_EMOJI_ANIMALS = "\uD83D\uDC3A";
    public static final String TEXT_EMOJI_INSECTS = "\uD83E\uDEB0";
    public static final String TEXT_EMOJI_MUSHROOMS = "\uD83C\uDF44";
    public static final String TEXT_EMOJI_VEGETABLES = "\uD83C\uDF31";
    public static final String TEXT_EMOJI_PARCHMENT = "\uD83D\uDCDC";
    public static final String TEXT_EMOJI_INK = "\uD83D\uDD8B\uFE0F";
    public static final String TEXT_EMOJI_FEATHER = "\uD83E\uDEB6";

    // Reset
    public static final String TEXT_RESET = "\033[0m";  // Text Reset

    // Regular Colors
    public static final String TEXT_BLACK = "\033[0;30m";   // BLACK
    public static final String TEXT_RED = "\033[0;31m";     // RED
    public static final String TEXT_GREEN = "\033[0;32m";   // GREEN
    public static final String TEXT_YELLOW = "\033[0;33m";  // YELLOW
    public static final String TEXT_BLUE = "\033[0;34m";    // BLUE
    public static final String TEXT_PURPLE = "\033[0;35m";  // PURPLE
    public static final String TEXT_CYAN = "\033[0;36m";    // CYAN
    public static final String TEXT_WHITE = "\033[0;37m";   // WHITE

    // Bold
    public static final String TEXT_BLACK_BOLD = "\033[1;30m";  // BLACK
    public static final String TEXT_RED_BOLD = "\033[1;31m";    // RED
    public static final String TEXT_GREEN_BOLD = "\033[1;32m";  // GREEN
    public static final String TEXT_YELLOW_BOLD = "\033[1;33m"; // YELLOW
    public static final String TEXT_BLUE_BOLD = "\033[1;34m";   // BLUE
    public static final String TEXT_PURPLE_BOLD = "\033[1;35m"; // PURPLE
    public static final String TEXT_CYAN_BOLD = "\033[1;36m";   // CYAN
    public static final String TEXT_WHITE_BOLD = "\033[1;37m";  // WHITE

    // Underline
    public static final String TEXT_BLACK_UNDERLINED = "\033[4;30m";  // BLACK
    public static final String TEXT_RED_UNDERLINED = "\033[4;31m";    // RED
    public static final String TEXT_GREEN_UNDERLINED = "\033[4;32m";  // GREEN
    public static final String TEXT_YELLOW_UNDERLINED = "\033[4;33m"; // YELLOW
    public static final String TEXT_BLUE_UNDERLINED = "\033[4;34m";   // BLUE
    public static final String TEXT_PURPLE_UNDERLINED = "\033[4;35m"; // PURPLE
    public static final String TEXT_CYAN_UNDERLINED = "\033[4;36m";   // CYAN
    public static final String TEXT_WHITE_UNDERLINED = "\033[4;37m";  // WHITE

    // Background
    public static final String TEXT_BLACK_BACKGROUND = "\033[40m";  // BLACK
    public static final String TEXT_RED_BACKGROUND = "\033[41m";    // RED
    public static final String TEXT_GREEN_BACKGROUND = "\033[42m";  // GREEN
    public static final String TEXT_YELLOW_BACKGROUND = "\033[43m"; // YELLOW
    public static final String TEXT_BLUE_BACKGROUND = "\033[44m";   // BLUE
    public static final String TEXT_PURPLE_BACKGROUND = "\033[45m"; // PURPLE
    public static final String TEXT_CYAN_BACKGROUND = "\033[46m";   // CYAN
    public static final String TEXT_WHITE_BACKGROUND = "\033[47m";  // WHITE

    // High Intensity
    public static final String TEXT_BLACK_BRIGHT = "\033[0;90m";  // BLACK
    public static final String TEXT_RED_BRIGHT = "\033[0;91m";    // RED
    public static final String TEXT_GREEN_BRIGHT = "\033[0;92m";  // GREEN
    public static final String TEXT_YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
    public static final String TEXT_BLUE_BRIGHT = "\033[0;94m";   // BLUE
    public static final String TEXT_PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
    public static final String TEXT_CYAN_BRIGHT = "\033[0;96m";   // CYAN
    public static final String TEXT_WHITE_BRIGHT = "\033[0;97m";  // WHITE

    // Bold High Intensity
    public static final String TEXT_BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
    public static final String TEXT_RED_BOLD_BRIGHT = "\033[1;91m";   // RED
    public static final String TEXT_GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
    public static final String TEXT_YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
    public static final String TEXT_BLUE_BOLD_BRIGHT = "\033[1;94m";  // BLUE
    public static final String TEXT_PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
    public static final String TEXT_CYAN_BOLD_BRIGHT = "\033[1;96m";  // CYAN
    public static final String TEXT_WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE

    // High Intensity backgrounds
    public static final String TEXT_BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
    public static final String TEXT_RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
    public static final String TEXT_GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
    public static final String TEXT_YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
    public static final String TEXT_BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE
    public static final String TEXT_PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
    public static final String TEXT_CYAN_BACKGROUND_BRIGHT = "\033[0;106m";  // CYAN
    public static final String TEXT_WHITE_BACKGROUND_BRIGHT = "\033[0;107m";   // WHITE
}
