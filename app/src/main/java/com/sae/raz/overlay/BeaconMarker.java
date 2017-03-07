package com.sae.raz.overlay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.jiahuan.svgmapview.SVGMapView;
import com.jiahuan.svgmapview.overlay.SVGMapBaseOverlay;
import com.sae.raz.AppConstant;
import com.sae.raz.R;
import com.sae.raz.model.BeaconModel;
import com.sae.raz.ui.activity.BrowserActivity;


/**
 * Created by jiahuan on 2015/7/12.
 */
public class BeaconMarker extends SVGMapBaseOverlay
{
    private SVGMapView mMapView;
    private int type = BeaconModel.TYPE_MULTI;
    private int x = 200;
    private int y = 300;
    private boolean isDetected = false;
    private String webUrl = "";
    private String title = "";
    private Bitmap mBitmap;

    Activity activity;

    public BeaconMarker(SVGMapView svgMapView, BeaconModel model, boolean isDetected, Activity activity)
    {
        this.type = model.equType;
        this.x = model.xPosition;
        this.y = model.yPosition;
        this.isDetected = isDetected;
        this.webUrl = model.webUrl;
        this.title = model.beaconTitle;
        this.activity = activity;

        initLayer(svgMapView);
    }

    private void initLayer(SVGMapView svgMapView)
    {
        mMapView = svgMapView;

        if (isDetected) {
            switch (type) {
                case BeaconModel.TYPE_MULTI:
                    mBitmap = BitmapFactory.decodeResource(svgMapView.getResources(), R.mipmap.icon_multi_marker);
                    break;
                case BeaconModel.TYPE_UPPER:
                    mBitmap = BitmapFactory.decodeResource(svgMapView.getResources(), R.mipmap.icon_upperbody_marker);
                    break;
                case BeaconModel.TYPE_LOWER:
                    mBitmap = BitmapFactory.decodeResource(svgMapView.getResources(), R.mipmap.icon_lowerbody_marker);
                    break;
                case BeaconModel.TYPE_ARMS:
                    mBitmap = BitmapFactory.decodeResource(svgMapView.getResources(), R.mipmap.icon_arms_marker);
                    break;
                default:
                    break;
            }

        } else {
            mBitmap = BitmapFactory.decodeResource(svgMapView.getResources(), R.mipmap.icon_grey_marker);
        }
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
            Intent intent = new Intent(activity, BrowserActivity.class);
            intent.putExtra(AppConstant.EK_URL, webUrl);
            intent.putExtra(AppConstant.EK_TITLE, title);
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.in_left, R.anim.out_left);
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
