package org.bigorange.game.assets;

import com.badlogic.gdx.Gdx;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

public class LocaleAssets {
    private static Locale[] locales;

    static {
        final String[] locStrs = Gdx.files.internal("locales").readString().split("\n");
        locales = new Locale[locStrs.length];
        for (int i = 0; i < locStrs.length; i++) {
            final String code = locStrs[i];
            if(code.contains("_")){
                locales[i] = new Locale(code.split("_")[0], code.split("_")[1]);
            } else {
                locales[i] = new Locale(code);
            }
        }

        Arrays.sort(locales, new LocaleComparator());
    }

    public static Locale[] getLocales() {
        return locales;
    }

    static class LocaleComparator implements Comparator<Locale>{

        @Override
        public int compare(Locale o1, Locale o2) {
            return o1.getDisplayName().compareToIgnoreCase(o2.getDisplayName());
        }
    }
}
