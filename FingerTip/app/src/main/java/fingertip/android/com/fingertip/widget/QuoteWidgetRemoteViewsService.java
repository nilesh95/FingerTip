package fingertip.android.com.fingertip.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by nilnayal on 3/16/2017.
 */

public class QuoteWidgetRemoteViewsService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetDataProvider(this, intent);
    }
}
