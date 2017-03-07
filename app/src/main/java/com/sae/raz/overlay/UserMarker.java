package com.sae.raz.overlay;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.widget.Toast;

import com.jiahuan.svgmapview.SVGMapView;
import com.jiahuan.svgmapview.overlay.SVGMapBaseOverlay;
import com.sae.raz.R;


/**
 * Created by jiahuan on 2015/7/12.
 */
public class UserMarker extends SVGMapBaseOverlay
{
    private SVGMapView mMapView;
    private int x = 200;
    private int y = 300;
    private Bitmap mBitmap;

    public UserMarker(SVGMapView svgMapView, int x, int y)
    {
        initLayer(svgMapView);
        this.x = x;
        this.y = y;
    }

    private void initLayer(SVGMapView svgMapView)
    {
        mMapView = svgMapView;
        mBitmap = BitmapFactory.decodeResource(svgMapView.getResources(), R.mipmap.icon_user_marker);
    }


    @Override
    public void onDestroy()
    {

    }

    @Override
    public void onPause()
    {

    }

    @Override
    public void onResume()
    {

    }

    @Override
    public void onTap(MotionEvent event)
    {
        float[] mapCoordinate = mMapView.getMapCoordinateWithScreenCoordinate(event.getX(), event.getY());
        if (mapCoordinate[0] >= x && mapCoordinate[0] <= x + mBitmap.getWidth() && mapCoordinate[1] >= y && mapCoordinate[1] <= y + mBitmap.getHeight())
        {
            String alertStr = String.format("(x, y) = (%d, %d)", x, y);
            Toast.makeText(mMapView.getContext(), alertStr, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void draw(Canvas canvas, Matrix matrix, float currentZoom, float currentRotateDegrees)
    {
        canvas.save();
        canvas.setMatrix(matrix);

        canvas.drawBitmap(mBitmap, x, y, new Paint(Paint.ANTI_ALIAS_FLAG));

        canvas.restore();
    }
}
