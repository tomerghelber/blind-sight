package com.example.user.blindsight;

import android.app.Activity;

/**
 * Created by michelle on 13/11/2014.
 */
public class UpdateableActivityResource extends ActivityResource {

    public UpdateableActivityResource(UpdateableActivity activity) {
        this(activity, activity);
    }

    public UpdateableActivityResource(Activity activity, Updateable updateable) {
        super(activity);
        this.updateable = updateable;
    }

    protected Updateable updateable;
}
