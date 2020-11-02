package org.gipilab.simulateurdeplacements;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.NumberFormat;

public class MyMarkerView extends MarkerView {
    private TextView tvContent;

    private NumberFormat formatter;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        formatter = NumberFormat.getCurrencyInstance();
        formatter.setMaximumFractionDigits(2);

        tvContent = (TextView) findViewById(R.id.myMarkerViewTextView1);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        tvContent.setText(formatter.format(e.getY()));
        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {
        //right align
        if (mOffset == null) {
            mOffset = new MPPointF(-getWidth() - 5, -getHeight());
        }
        return mOffset;
    }
}
