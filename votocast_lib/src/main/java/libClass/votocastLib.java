package libClass;

import android.app.Activity;
import android.content.Intent;

import com.votocast.votocast.SplashActivity;

/**
 * Created by Admin on 12/21/2017.
 */

public class votocastLib {

    public votocastLib(Activity activity) {
        activity.startActivity(new Intent(activity, SplashActivity.class));
    }
}
