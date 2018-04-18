package com.focusmedica.aqrshell;

import java.util.ArrayList;

/**
 * Created by in_sane on 7/4/17.
 */

public interface LanguageListner  {
    public void addLanguage(DataModel city);

    public ArrayList<DataModel> getAllLanguage();

    public int getLanguageCount();
}
