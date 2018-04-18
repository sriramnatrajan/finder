package com.focusmedica.aqrshell;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by windev on 8/21/2017.
 */

public class MySuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.focusmedica.aqrshell";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public MySuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
