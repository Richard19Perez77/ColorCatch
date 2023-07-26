package radical.appwards.colorcatch.level;

import android.content.Context;

public class LevelFactory {

    public Level createLevel(Context context, int levelNumber) {
        switch (levelNumber) {
            case 0:
                return new LevelEndingImpl(context);
            case 1:
                return new Level01Impl();
            case 2:
                return new Level02Impl();
            case 3:
                return new Level03Impl();
            case 4:
                return new Level04Impl();
            case 5:
                return new Level05Impl();
            case 6:
                return new Level06Impl();
            case 7:
                return new Level07Impl();
            case 8:
                return new Level08Impl();
            case 9:
                return new Level09Impl();
            case 10:
                return new Level10Impl();
        }
        return null;
    }
}
