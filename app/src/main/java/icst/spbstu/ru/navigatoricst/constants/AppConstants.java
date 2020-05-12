package icst.spbstu.ru.navigatoricst.constants;

public class AppConstants {

    public static final int SPLASH_DURATION = 1500;
    public static final int PROMPT_DURATION = 2500;


    public static final int DIRECTIONS_COUNT = 8;
    public static final int INFO_DIRECTIONS_COUNT = 3; // количество направлений в результатах

    public static final int BUNDLE_KEY_ZERO_INDEX = 0;
    public static final int BUNDLE_KEY_FIRST_INDEX = 1;
    public static final int BUNDLE_KEY_SECOND_INDEX = 2;



    public static final String EMPTY_STRING = "";
    public static final String BUNDLE_KEY_TITLE = "title";
    public static final String BUNDLE_KEY_MESSAGE = "message";
    public static final String BUNDLE_KEY_URL = "url";
    public static final long SITE_CASHE_SIZE = 10 * 1024 * 1024;

    // notification constants
    public static final String APP_PREF_NAME = "nav_app_pref";
    public static final String PREF_NOTIFICATION = "pref_notification";
    public static final String PREF_FONT_SIZE = "pref_font_size";
    public static final String NEW_NOTI = "new_notification";
    public static final String BUNDLE_KEY_ITEM = "item";
    public static final String BUNDLE_KEY_INDEX = "index";
    public static final String BUNDLE_KEY_DELETE_ALL_NOT = "delete_all_not";

    // question constants
    public static final String BUNDLE_KEY_YES = "yes";
    public static final String BUNDLE_KEY_NO = "no";
    public static final String BUNDLE_KEY_VIEW_ID = "view_id_tex";
    public static final String BUNDLE_KEY_DIALOG_FRAGMENT = "dialog_fragment";
    public static final String BUNDLE_KEY_EXIT_OPTION = "exit";
    public static final String BUNDLE_KEY_CLOSE_OPTION = "close";
    public static final String BUNDLE_KEY_SKIP_OPTION = "skip";
    public static final String BUNDLE_KEY_DIRECTIONS_SCORES = "dir_scores";
    public static final String BUNDLE_KEY_ID = "id";

    // question file
    public static final String QUESTION_FILE = "json/question_set.json";
    public static final String JSON_KEY_QUESTIONNAIRY = "questionnaires";
    public static final String JSON_KEY_QUESTION = "question";
    public static final String JSON_KEY_ANSWERS = "answers";
    public static final String JSON_KEY_SCORES = "scores";
    public static final String JSON_KEY_SCORE_SET = "score_set";
    public static final String QUESTIONS_IN_TEST = "questions_count";

    // info file
    public static final String INFO_FILE = "json/it_info.json";
    public static final String JSON_KEY_AREAS = "areas";
    public static final String JSON_KEY_DIRECTIONS = "directions";
    public static final String JSON_KEY_ID = "id";
    public static final String JSON_KEY_NAME = "name";
    public static final String JSON_KEY_DIRS = "icst_dirs";
    public static final String JSON_KEY_CODES = "code";
    public static final String JSON_KEY_DESCRIPTION = "description";


    // pie chart constants
    public static final float TRANSPARENT_CIRCLE_RADIUS = 65f;
    public static final int ANIMATION_VALUE = 2000;

    public static final String COLOR_WHITE = "rectangle_white_normal";
    public static final String LAST_RES = "last_res";
}
