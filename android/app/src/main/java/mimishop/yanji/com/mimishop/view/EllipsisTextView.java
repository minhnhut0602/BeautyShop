package mimishop.yanji.com.mimishop.view;

/**
 * Created by KimCholJu on 7/29/2015.
 */
import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EllipsisTextView extends TextView
{
    public interface EllipsisListener
    {
        void ellipsisStateChanged(boolean ellipses);
    }

    private final List<EllipsisListener> ellipsesListeners = new ArrayList<EllipsisListener>();

    private boolean ellipses;

    public EllipsisTextView(Context context)
    {
        super(context);
    }

    public EllipsisTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public EllipsisTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public void addEllipsesListener(EllipsisListener listener)
    {
        if (listener == null) {
            throw new NullPointerException();
        }
        ellipsesListeners.add(listener);
    }

    public void removeEllipsesListener(EllipsisListener listener)
    {
        ellipsesListeners.remove(listener);
    }

    public boolean hadEllipses() {
        return ellipses;
    }

    @Override
    public void layout(int l, int t, int r, int b)
    {
        super.layout(l, t, r, b);

        ellipses = false;
        Layout layout = getLayout();
        if ( layout != null){
            int lines = layout.getLineCount();
            if ( lines > 0)  {
                if ( layout.getEllipsisCount(lines-1) > 0) {
                    ellipses = true;
                }
            }
        }

        for (EllipsisListener listener : ellipsesListeners) {
            listener.ellipsisStateChanged(ellipses);
        }
    }
}