package org.opencv.samples.blindsight.navigation;

import android.app.Activity;

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
