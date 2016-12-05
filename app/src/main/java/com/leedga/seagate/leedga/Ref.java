package com.leedga.seagate.leedga;

import com.google.gson.Gson;

/**
 * Created by Muhammad Workstation on 06/11/2016.
 */


class REF {
    public static final String TIME_FORMAT = "hh:mm a";
    public static final String SIMPLE_DATE_FORMAT = "dd/MM/yyyy";
    public static final String TRUE_FALSE_KEY = "truefalse";
    public static final String SINGLE_CHOICE_KEY = "single";
    public static final String MULTI_CHOICE_KEY = "multi";
    public static final String TEST_BUNDLE_KEY = "tests";
    public static final String QUESTIONS_POSTITION_KEY = "count";
    public static final String QUESTION_KEY = "question";
    public static final String DB_PATH = "data/data/com.sea.gate.leedga/databases/";
    public static final String[] typesNames = {"truefalse", "single", "multi"};
    public static final String SINGLE_CHOICE_LEED_TABLE = "leed_process_14";
    public static final String ID = "id";
    public static final String ANSWER = "answer";
    public static final String FIRST_CHOICE = "a";
    public static final String SECOND_CHOICE = "b";
    public static final String THIRD_CHOICE = "c";
    public static final String FOURTH_CHOICE = "d";
    public static final String NOTES_ON_ANSWER = "notes";
    public static final String FLAGGED = "flagged";
    public static final String TABLE_NAME = "LeedGA";
    public static final String FIFTH_CHOICE = "e";
    public static final String SIXITH_CHOICE = "f";
    public static final String TYPE = "type";
    public static final String CATEGORY = "category";
    public static final String KEY = "key";
    //DBHelper
    public static final String[] CATEGORY_NAMES = {"LEED Process", "Integrative Strategies", "Location and Transportation", "Sustainable Sites", "Project Surroundings", "Water Efficiency", "Energy & Atmosphere", "Materials & Resources", "Indoor Environmental Quality"};
    static final String DETAILED_DATE_FORMAT = "MMM dd, yyyy h:mm a";
    static final int TERMS_KEY = 0;
    static final int LESSONS_KEY = 1;
    static final int REFERENCES_KEY = 2;
    static final String FRAGMENT_TYPE_KEY = "type";
    static final String POSITION_KEY = "position";
    static final String GENERAL_SETTING_PREF = "general_setting_pref";
    static final String SCHEDULE_EXAM_DATE_PREF = "schedule_exam_date";
    static final String DAY_QUESTION_PREF = "question_of_day_alert";
    static final int PENDING_INTENT_ID = 3333;
    static final String TRIGGER_MILLS_KEY = "trigger_mills";
    static final String DATABASE_NAME = "leed.sqlite";
    static final String TEST_FRAGMENT_TYPE = "type";
    static final String SINGLE_QUESTION = "single_question";
    static final String FULL_QUESTIONS = "full_questions";
    static final String UNCOMPLETED_PREF = "uncompleted_pref";
    static final String UNCOMPLETED_TEST = "uncompleted_test";
    static final String TEST_FRAGMENT_TAG = "test_fragment";
    static final String LESSON_ACTIVITY_KEY = "lesson_activity_key";
    static final String LESSONS = "Lessons";
    static final String KEY_TERMS = "Key terms and definitions";
    static final String REFERENCE = "Reference materials";
    static final String KNOWLEDGE_DOMAIN = "knowledge_domain";
    static final String DEFAULT_TEST_KEY = "default_test";
    static final String DEFAULT_TEST_PREF_KEY = "default_test_pref";
    static final String DAY_QUESTION_DATE_PREF_KEY = "day_question_date_key";
    static final String DAY_QUESTION_HISTORY_PREF="day_question_history_pref";
    static final String SKU_PREMIUM ="premium_id";
    static final int RC_PREMIUM_PURCHASE=6564;
    static final String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkLHsKDzVLt/KYTAGO1Js363oEj679xh2TsdPMyM2RfFAT2iGG6tD6sfHKUXhb76TXAxeUH3wAr0iD+JM1gnc9KtG7MzEorZ+QDm0t+4akExczlOTABgWQ7R1F9MRzHxnr3PrRexp+rbXYTCBwzlCTUQ08C6Ryb/lxUCwrNC+UnirUjRZQ7xjEqIWcP5u1q5AIHaa2bze6AHZn7jE+qBpjGRhRxsRQyVM0Hnt7DQu2yUrkuUtz1yQg2d58CgcKN7TZz2H+lr3s5Oe6D9nCoM77VI33dnGJpyWpAzf/mRaEY/01K3H91pJrc0Qy4tR9XcgBB8Q2d8DUKjt/gJY5RxHiQIDAQAB";
    static final String VUNGLE_APP_ID="5845c44d27800f59690002e1";
    static final String FIREBASE_COMMIT_NUMBER_PREF_KEY="firebase_commit_number_pref_key";
    static final  String COMMIT_NUMBER_KEY="CommitNumber";
    static final  String QUESTION_BODY_KEY="questionBody";
    static final int RC_UPGRADE_PURCHASE=1005;
    static String[] chapterNames = {"Becoming a LEED Green Associate", "The Test Process", "LEED v4 Core Concepts and Themes", "Overview of USGBC and LEED", "Location and Transportation", "Sustainable Sites", "Water Efficiency", "Energy and Atmosphere", "Materials and Resources", "Indoor Environmental Quality", "Innovation and Regional Priority", "Regional Priority"};
    static String[] links = {"http://www.usgbc.org/resources/leed-v4-green-associate-candidate-handbook", "http://www.usgbc.org/resources/leed-core-concepts-guide", "http://www.usgbc.org/resources/leed-green-associate-exam-preparation-guide-leed-v4-edition", "http://www.usgbc.org/sites/all/assets/section/files/v4-guide-excerpts/Excerpt_v4_BDC.pdf", "http://www.usgbc.org/sites/default/files/LEED%20v4%20Impact%20Category%20and%20Point%20Allocation%20Process_Overview_0.pdf", "http://www.usgbc.org/resources/leed-v4-user-guide", "http://www.usgbc.org/cert-guide/commercial", "http://www.usgbc.org/cert-guide/fees", "http://www.usgbc.org/articles/rating-system-selection-guidance", "http://www.usgbc.org/leed-interpretations", "http://www.usgbc.org/resources/leed-v4-building-design-and-construction-checklist"};
    static String[] references = {"LEED V4 Green Associate Candidate Handbook ",
            "Green Building and LEED Core Concepts Guide. 3rd Edition", "LEED Green Associate Exam Preparation Guide LEED V4 Edition", " LEED Building Design + Construction Reference Guide v4 Edition - introductory and overview sections", "LEED v4 Impact Category and Point Allocation Process Overview", "LEED v4 User Guide ", "Guide to LEED Certification: Commercial", "LEED Certification Fees", "Rating System Selection Guidance", "Addenda Database", "LEED v4 for Building Design and Construction Checklist"};

    static String getGsonString(Object o){
        Gson gson=new Gson();
        return gson.toJson(o);
    }

    static Test getTestFromGson(String json){
        Gson gson=new Gson();
        return gson.fromJson(json,Test.class);
    }
}
