
package com.tiger.socol.gu.views.widgets.slideview;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.tiger.socol.gu.R;

import java.io.File;

public abstract class BaseSliderView {

    protected Context mContext;

    private final Bundle mBundle;

    /**
     * Error place holder image.
     */
    private int mErrorPlaceHolderRes;

    /**
     * Empty imageView placeholder.
     */
    private int mEmptyPlaceHolderRes;

    private String mUrl;
    private File mFile;
    private int mRes;

    protected OnSliderClickListener mOnSliderClickListener;

    private boolean mErrorDisappear;

    private ImageLoadListener mLoadListener;

    private String mDescription;

    private boolean visibleDescription;

    protected BaseSliderView(Context context) {
        mContext = context;
        this.mBundle = new Bundle();
    }

    /**
     * the placeholder image when loading image from url or file.
     *
     * @param resId Image resource id
     * @return
     */
    public BaseSliderView empty(int resId) {
        mEmptyPlaceHolderRes = resId;
        return this;
    }

    /**
     * determine whether remove the image which failed to download or load from
     * file
     *
     * @param disappear
     * @return
     */
    public BaseSliderView errorDisappear(boolean disappear) {
        mErrorDisappear = disappear;
        return this;
    }

    /**
     * if you set errorDisappear false, this will set a error placeholder image.
     *
     * @param resId image resource id
     * @return
     */
    public BaseSliderView error(int resId) {
        mErrorPlaceHolderRes = resId;
        return this;
    }

    /**
     * the description of a slider image.
     *
     * @param description
     * @return
     */
    public BaseSliderView description(String description) {
        mDescription = description;
        return this;
    }


    public BaseSliderView descriptionisVisible(boolean isVisible) {
        visibleDescription = isVisible;
        return this;
    }

    /**
     * set a url as a image that preparing to load
     *
     * @param url
     * @return
     */
    public BaseSliderView image(String url) {
        if (mFile != null || mRes != 0) {
            throw new IllegalStateException("Call multi image function," +
                    "you only have permission to call it once");
        }
        mUrl = url;
        return this;
    }

    /**
     * set a file as a image that will to load
     *
     * @param file
     * @return
     */
    public BaseSliderView image(File file) {
        if (mUrl != null || mRes != 0) {
            throw new IllegalStateException("Call multi image function," +
                    "you only have permission to call it once");
        }
        mFile = file;
        return this;
    }

    public BaseSliderView image(int res) {
        if (mUrl != null || mFile != null) {
            throw new IllegalStateException("Call multi image function," +
                    "you only have permission to call it once");
        }
        mRes = res;
        return this;
    }

    public String getUrl() {
        return mUrl;
    }

    public boolean isErrorDisappear() {
        return mErrorDisappear;
    }

    public int getEmpty() {
        return mEmptyPlaceHolderRes;
    }

    public int getError() {
        return mErrorPlaceHolderRes;
    }

    public String getDescription() {
        return mDescription;
    }

    public boolean getIsVisibleDescription() {
        return visibleDescription;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * set a slider image click listener
     *
     * @param l
     * @return
     */
    public BaseSliderView setOnSliderClickListener(OnSliderClickListener l) {
        mOnSliderClickListener = l;
        return this;
    }

    /**
     * when you want to extends this class, please use this method to load a
     * image to a imageview.
     *
     * @param targetImageView
     */
    protected void loadImage(final ImageView targetImageView) {
        final BaseSliderView me = this;
        mLoadListener.onStart(me);
//        Picasso p = Picasso.with(mContext);
//        RequestCreator rq = null;
//        if (mUrl != null) {
//            rq = p.load(mUrl);
//        } else if (mFile != null) {
//            rq = p.load(mFile);
//        } else if (mRes != 0) {
//            rq = p.load(mRes);
//        } else {
//            return;
//        }
//        if (rq == null) {
//            return;
//        }
//
//        if (getEmpty() != 0) {
//            rq.placeholder(getEmpty());
//        }
//
//        if (getError() != 0) {
//            rq.error(getError());
//        }
//        rq.fit();
//
//        rq.into(targetImageView, new Callback() {
//            @Override
//            public void onRequestExamDetailSuccess() {
////                if (progressBar != null)
////                    progressBar.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onError() {
////                if (progressBar != null)
////                    progressBar.setVisibility(View.INVISIBLE);
//                targetImageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.mipmap.bg_default_slide));
//
//                if (mLoadListener != null) {
//                    mLoadListener.onEnd(false, me);
//                }
//            }
//        });
    }

    private View progressBar = null;

    /**
     * when you want to extends this class, you must call this method to bind
     * click event to your view.
     *
     * @param v
     */
    protected void bindClickEvent(View v) {
        final BaseSliderView me = this;
        progressBar = v.findViewById(R.id.loading_bar);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSliderClickListener != null) {
                    mOnSliderClickListener.onSliderClick(me);
                }
            }
        });
    }

    /**
     * the extended class have to implement getView(), which is called by the
     * adapter, every extended class response to render their own view.
     *
     * @return
     */
    public abstract View getView();

    /**
     * set a listener to get a message , if load error.
     *
     * @param l
     */
    public void setOnImageLoadListener(ImageLoadListener l) {
        mLoadListener = l;
    }

    public interface OnSliderClickListener {
        void onSliderClick(BaseSliderView slider);
    }

    /**
     * when you have some extra information, please put it in this bundle.
     *
     * @return
     */
    public Bundle getBundle() {
        return mBundle;
    }

    public interface ImageLoadListener {
        void onStart(BaseSliderView target);

        void onEnd(boolean result, BaseSliderView target);
    }

}