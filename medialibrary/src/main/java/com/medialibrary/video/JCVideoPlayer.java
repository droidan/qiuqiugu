package com.medialibrary.video;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.medialibrary.R;
import com.medialibrary.video.listener.JCMediaPlayerListener;
import com.medialibrary.video.listener.ScreenListener;
import com.medialibrary.video.utils.HandlerWeak;
import com.medialibrary.video.utils.JCUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by Nathen on 16/7/30.
 */
public abstract class JCVideoPlayer extends FrameLayout implements JCMediaPlayerListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener, View.OnTouchListener, TextureView.SurfaceTextureListener {
    public static final String TAG = "JieCaoVideoPlayer";
    public static final int FULLSCREEN_ID = 33797;
    public static final int TINY_ID = 33798;
    public static final int THRESHOLD = 80;
    public static final int FULL_SCREEN_NORMAL_DELAY = 500;
    public static boolean ACTION_BAR_EXIST = true;
    public static boolean TOOL_BAR_EXIST = true;
    public static boolean WIFI_TIP_DIALOG_SHOWED = false;
    public int FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    public int NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

    public static long CLICK_QUIT_FULLSCREEN_TIME = 0;

    public static final int SCREEN_LAYOUT_NORMAL = 0;
    public static final int SCREEN_LAYOUT_LIST = 1;
    public static final int SCREEN_WINDOW_FULLSCREEN = 2;
    public static final int SCREEN_WINDOW_TINY = 3;

    public static final int CURRENT_STATE_NORMAL = 0;
    public static final int CURRENT_STATE_PREPARING = 1;
    public static final int CURRENT_STATE_PLAYING = 2;
    public static final int CURRENT_STATE_PLAYING_BUFFERING_START = 3;
    public static final int CURRENT_STATE_PAUSE = 5;
    public static final int CURRENT_STATE_AUTO_COMPLETE = 6;
    public static final int CURRENT_STATE_ERROR = 7;

    public int currentState = -1;
    public int currentScreen = -1;
    public String url = "";
    public Object[] objects = null;
    public boolean looping = false;
    public Map<String, String> mapHeadData = new HashMap<>();
    public int seekToInAdvance = -1;
    private Bitmap pauseSwitchCoverBitmap = null;
    private boolean textureUpdated;
    private boolean textureSizeChanged;
    private boolean isLiving;

    public ImageView startButton;
    public JCResizeImageView cacheImageView;
    public SeekBar progressBar;
    public ImageView fullscreenButton;
    public TextView currentTimeTextView, totalTimeTextView;
    public ViewGroup textureViewContainer;
    public ViewGroup topContainer, bottomContainer;
    public Surface surface;

    protected static WeakReference<JCBuriedPoint> JC_BURIED_POINT;
    protected int mScreenWidth;
    protected int mScreenHeight;
    protected AudioManager mAudioManager;
    private HandlerWeak mHandler;

    protected boolean mTouchingProgressBar;
    protected float mDownX;
    protected float mDownY;
    protected boolean mChangeVolume;
    protected boolean mChangePosition;
    protected int mDownPosition;
    protected int mGestureDownVolume;
    protected int mSeekTimePosition;

    private boolean isPreLoad = true;

    public JCVideoPlayer(Context context) {
        super(context);
        init(context);
    }

    public JCVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        View.inflate(context, getLayoutId(), this);
        startButton = (ImageView) findViewById(R.id.start);
        fullscreenButton = (ImageView) findViewById(R.id.fullscreen);
        progressBar = (SeekBar) findViewById(R.id.progress);
        currentTimeTextView = (TextView) findViewById(R.id.current);
        totalTimeTextView = (TextView) findViewById(R.id.total);
        bottomContainer = (ViewGroup) findViewById(R.id.layout_bottom);
        textureViewContainer = (ViewGroup) findViewById(R.id.surface_container);
        topContainer = (ViewGroup) findViewById(R.id.layout_top);
        cacheImageView = (JCResizeImageView) findViewById(R.id.cache);

        startButton.setOnClickListener(this);
        bottomContainer.setOnClickListener(this);
        fullscreenButton.setOnClickListener(this);
        progressBar.setOnSeekBarChangeListener(this);
        textureViewContainer.setOnClickListener(this);

        textureViewContainer.setOnTouchListener(this);
        mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getContext().getResources().getDisplayMetrics().heightPixels;

        mAudioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        mHandler = new HandlerWeak();
    }

    public void setLiving(boolean living) {
        isLiving = living;
        if (living) {
            progressBar.setVisibility(View.INVISIBLE);
            totalTimeTextView.setVisibility(View.INVISIBLE);
            currentTimeTextView.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            totalTimeTextView.setVisibility(View.VISIBLE);
            currentTimeTextView.setVisibility(View.VISIBLE);
        }
    }

    public boolean setVideoInfo(String url, int screen, Object... objects) {
        if (!TextUtils.isEmpty(this.url) && TextUtils.equals(this.url, url)) {
            return false;
        }
        JCVideoPlayerManager.checkAndPutListener(this);
        if (JCVideoPlayerManager.CURRENT_SCROLL_LISTENER != null && JCVideoPlayerManager.CURRENT_SCROLL_LISTENER.get() != null) {
            if (this == JCVideoPlayerManager.CURRENT_SCROLL_LISTENER.get()) {
                if (((JCVideoPlayer) JCVideoPlayerManager.CURRENT_SCROLL_LISTENER.get()).currentState == CURRENT_STATE_PLAYING) {
                    if (url.equals(JCMediaManager.instance().mediaPlayer.getDataSource())) {
                        ((JCVideoPlayer) JCVideoPlayerManager.CURRENT_SCROLL_LISTENER.get()).startWindowTiny();//如果列表中,滑动过快,在还没来得及onScroll的时候自己已经被复用了
                    }
                }
            }
        }
        this.url = url;
        this.objects = objects;
        this.currentScreen = screen;
        setUiWitStateAndScreen(CURRENT_STATE_NORMAL);
        if (url.equals(JCMediaManager.instance().mediaPlayer.getDataSource())) {//如果初始化了一个正在tinyWindow的前身,就应该监听它的滑动,如果显示就在这个listener中播放
            JCVideoPlayerManager.putScrollListener(this);
        }
        return true;
    }

    @Override
    public int getScreenType() {
        return currentScreen;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public int getState() {
        return currentState;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.start) {
            Log.i(TAG, "onClick start [" + this.hashCode() + "] ");
            if (TextUtils.isEmpty(url)) {
                Toast.makeText(getContext(), getResources().getString(R.string.no_url), Toast.LENGTH_SHORT).show();
                return;
            }
            isPreLoad = false;
            if (currentState == CURRENT_STATE_NORMAL || currentState == CURRENT_STATE_ERROR) {
                if (!url.startsWith("file") && !JCUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
                    showWifiDialog();
                    return;
                }
                prepareVideo();
                onEvent(currentState != CURRENT_STATE_ERROR ? JCBuriedPoint.ON_CLICK_START_ICON : JCBuriedPoint.ON_CLICK_START_ERROR);
            } else if (currentState == CURRENT_STATE_PLAYING) {
                mediaPause();
            } else if (currentState == CURRENT_STATE_PAUSE) {
                mediaResume();
            } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
                onEvent(JCBuriedPoint.ON_CLICK_START_AUTO_COMPLETE);
                prepareVideo();
            }
        } else if (i == R.id.fullscreen) {
            Log.i(TAG, "onClick fullscreen [" + this.hashCode() + "] ");
            if (currentState == CURRENT_STATE_AUTO_COMPLETE) return;
            if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
                //quit fullscreen
                //backPress();
            } else {
                Log.d(TAG, "toFullscreenActivity [" + this.hashCode() + "] ");
                onEvent(JCBuriedPoint.ON_ENTER_TINYSCREEN);
                //startWindowFullscreen();
            }
            if (listener == null) {
                return;
            }
            listener.onFullscreenOnclicker();
        } else if (i == R.id.surface_container && currentState == CURRENT_STATE_ERROR) {
            Log.i(TAG, "onClick surfaceContainer State=Error [" + this.hashCode() + "] ");
            prepareVideo();
        }
    }

    /**
     * 视频暂停
     */
    public void mediaPause() {
        if (isPlayed()) {
            obtainCache();
            onEvent(JCBuriedPoint.ON_CLICK_PAUSE);
            Log.d(TAG, "pauseVideo [" + this.hashCode() + "] ");
            JCMediaManager.instance().mediaPlayer.pause();
            setUiWitStateAndScreen(CURRENT_STATE_PAUSE);
            refreshCache();
        }
    }

    /**
     * 视频继续
     */
    public void mediaResume() {
        if (isPlayed()) {
            onEvent(JCBuriedPoint.ON_CLICK_RESUME);
            JCMediaManager.instance().mediaPlayer.start();
            setUiWitStateAndScreen(CURRENT_STATE_PLAYING);
        }
    }

    public void prepareVideo() {
        JCVideoPlayerManager.completeAll();
        JCVideoPlayerManager.putListener(this);
        addTextureView();

        mAudioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        JCUtils.scanForActivity(getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        JCVideoPlayerManager.putScrollListener(this);
        JCMediaManager.instance().prepare(url, mapHeadData, looping);

        setUiWitStateAndScreen(CURRENT_STATE_PREPARING);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int id = v.getId();
        if (id == R.id.surface_container) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.i(TAG, "onTouch surfaceContainer actionDown [" + this.hashCode() + "] ");
                    mTouchingProgressBar = true;
                    mDownX = x;
                    mDownY = y;
                    mChangeVolume = false;
                    mChangePosition = false;
                    /////////////////////
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i(TAG, "onTouch surfaceContainer actionMove [" + this.hashCode() + "] ");
                    float deltaX = x - mDownX;
                    float deltaY = y - mDownY;
                    float absDeltaX = Math.abs(deltaX);
                    float absDeltaY = Math.abs(deltaY);
                    if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
                        if (!mChangePosition && !mChangeVolume) {
                            if (absDeltaX > THRESHOLD || absDeltaY > THRESHOLD) {
                                cancelProgressTimer();
                                if (absDeltaX >= THRESHOLD) {
                                    // 全屏模式下的CURRENT_STATE_ERROR状态下,不响应进度拖动事件.
                                    // 否则会因为mediaplayer的状态非法导致App Crash
                                    if (currentState != CURRENT_STATE_ERROR) {
                                        mChangePosition = true;
                                        mDownPosition = getCurrentPositionWhenPlaying();
                                    }
                                } else {
                                    mChangeVolume = true;
                                    mGestureDownVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                                }
                            }
                        }
                    }
                    if (mChangePosition) {
                        int totalTimeDuration = getDuration();
                        mSeekTimePosition = (int) (mDownPosition + deltaX * totalTimeDuration / mScreenWidth);
                        if (mSeekTimePosition > totalTimeDuration)
                            mSeekTimePosition = totalTimeDuration;
                        String seekTime = JCUtils.stringForTime(mSeekTimePosition);
                        String totalTime = JCUtils.stringForTime(totalTimeDuration);

                        showProgressDialog(deltaX, seekTime, mSeekTimePosition, totalTime, totalTimeDuration);
                    }
                    if (mChangeVolume) {
                        deltaY = -deltaY;
                        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                        int deltaV = (int) (max * deltaY * 3 / mScreenHeight);
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mGestureDownVolume + deltaV, 0);
                        int volumePercent = (int) (mGestureDownVolume * 100 / max + deltaY * 3 * 100 / mScreenHeight);

                        showVolumeDialog(-deltaY, volumePercent);
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    Log.i(TAG, "onTouch surfaceContainer actionUp [" + this.hashCode() + "] ");
                    mTouchingProgressBar = false;
                    dismissProgressDialog();
                    dismissVolumeDialog();
                    if (mChangePosition && !isLiving) {
                        onEvent(JCBuriedPoint.ON_TOUCH_SCREEN_SEEK_POSITION);
                        JCMediaManager.instance().mediaPlayer.seekTo(mSeekTimePosition);
                        int duration = getDuration();
                        int progress = mSeekTimePosition * 100 / (duration == 0 ? 1 : duration);
                        progressBar.setProgress(progress);
                    }
                    if (mChangeVolume) {
                        onEvent(JCBuriedPoint.ON_TOUCH_SCREEN_SEEK_VOLUME);
                    }
                    startProgressTimer();
                    break;
            }
        }
        return false;
    }

    public void addTextureView() {
        Log.d(TAG, "addTextureView [" + this.hashCode() + "] ");
        if (textureViewContainer.getChildCount() > 0) {
            textureViewContainer.removeAllViews();
        }
        JCResizeTextureView textureView = new JCResizeTextureView(getContext());
        textureView.setScreenOrientation(currentScreen);
        textureView.setVideoSize(JCMediaManager.instance().getVideoSize());
        textureView.setRotation(JCMediaManager.instance().videoRotation);
        textureView.setSurfaceTextureListener(this);
        JCMediaManager.instance().setTextureView(textureView);

        LayoutParams layoutParams =
                new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        Gravity.CENTER);
        textureViewContainer.addView(JCMediaManager.instance().getTextureView(), layoutParams);

        cacheImageView.setVideoSize(JCMediaManager.instance().getVideoSize());
        cacheImageView.setRotation(JCMediaManager.instance().videoRotation);

    }

    public void setUiWitStateAndScreen(int state) {
        currentState = state;
        switch (currentState) {
            case CURRENT_STATE_NORMAL:
                if (isCurrentMediaListener()) {
                    cancelProgressTimer();
                    JCMediaManager.instance().releaseMediaPlayer();
                }
                break;
            case CURRENT_STATE_PREPARING:
                resetProgressAndTime();
                break;
            case CURRENT_STATE_PLAYING:
            case CURRENT_STATE_PAUSE:
            case CURRENT_STATE_PLAYING_BUFFERING_START:
                startProgressTimer();
                break;
            case CURRENT_STATE_ERROR:
                cancelProgressTimer();
                if (isCurrentMediaListener()) {
                    JCMediaManager.instance().releaseMediaPlayer();
                }
                break;
            case CURRENT_STATE_AUTO_COMPLETE:
                cancelProgressTimer();
                progressBar.setProgress(100);
                currentTimeTextView.setText(totalTimeTextView.getText());
                break;
        }
    }


    public void startProgressTimer() {
        cancelProgressTimer();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(this, 0);
                if (currentState == CURRENT_STATE_PLAYING || currentState == CURRENT_STATE_PAUSE || currentState == CURRENT_STATE_PLAYING_BUFFERING_START) {
                    int position = getCurrentPositionWhenPlaying();
                    int duration = getDuration();
                    Log.v(TAG, "onProgressUpdate " + position + "/" + duration + " [" + this.hashCode() + "] ");
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            setTextAndProgress(JCMediaManager.instance().bufferPercent);
                        }
                    });
                }
            }
        }, 300);

    }

    public void cancelProgressTimer() {
        if (mHandler != null)
            mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onPrepared() {
        Log.e(TAG, "onPrepared " + " [" + this.hashCode() + "] ");

        if (currentState != CURRENT_STATE_PREPARING) return;

        JCMediaManager.instance().mediaPlayer.start();
        if (seekToInAdvance != -1) {
            if (precentPosition != 1.0) {
                seekToInAdvance = (int) (getDuration() * precentPosition);
                JCMediaManager.instance().mediaPlayer.seekTo(seekToInAdvance);
            }
            seekToInAdvance = -1;
        }
        startProgressTimer();
        setUiWitStateAndScreen(CURRENT_STATE_PLAYING);


        if (listener == null) {
            return;
        }
        listener.onPreparedListener();


    }

    /**
     * 进度百分比
     */
    private float precentPosition;

    public void setSeekToPosition(float precent) {
        if (precent == 0) {
            return;
        }
        seekToInAdvance = 1;
        this.precentPosition = precent;

        Log.e("===>", precent + "百分比");

    }

    public void clearFullscreenLayout() {
        ViewGroup vp = (ViewGroup) (JCUtils.scanForActivity(getContext()))//.getWindow().getDecorView();
                .findViewById(Window.ID_ANDROID_CONTENT);
        View oldF = vp.findViewById(FULLSCREEN_ID);
        View oldT = vp.findViewById(TINY_ID);
        if (oldF != null) {
            vp.removeView(oldF);
        }
        if (oldT != null) {
            vp.removeView(oldT);
        }
        showSupportActionBar(getContext());
    }

    @Override
    public void onAutoCompletion() {
        Log.i(TAG, "onAutoCompletion " + " [" + this.hashCode() + "] ");
        onEvent(JCBuriedPoint.ON_AUTO_COMPLETE);
        dismissVolumeDialog();
        dismissProgressDialog();
        setUiWitStateAndScreen(CURRENT_STATE_AUTO_COMPLETE);
        JCVideoPlayerManager.popListener();//自己进入autoComplete状态，其他的进入complete状态
        JCVideoPlayerManager.completeAll();
        if (listener == null) {
            return;
        }
        listener.onCompletionListener();
    }

    @Override
    public void onCompletion() {
        Log.i(TAG, "onCompletion " + " [" + this.hashCode() + "] ");
        setUiWitStateAndScreen(CURRENT_STATE_NORMAL);
        if (textureViewContainer.getChildCount() > 0) {
            textureViewContainer.removeAllViews();
        }

        JCMediaManager.instance().currentVideoWidth = 0;
        JCMediaManager.instance().currentVideoHeight = 0;

        // 清理缓存变量
        JCMediaManager.instance().bufferPercent = 0;
        JCMediaManager.instance().videoRotation = 0;

        mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
        JCUtils.scanForActivity(getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        clearFullscreenLayout();
        JCUtils.getAppCompActivity(getContext()).setRequestedOrientation(NORMAL_ORIENTATION);

        // 清理cover image,回收bitmap内存
        clearCacheImage();

    }

    @Override
    public boolean backToOtherListener() {//这里这个名字这么写并不对,这是在回退的时候gotoother,如果直接gotoother就不叫这个名字

        obtainCache();

        Log.i(TAG, "backToOtherListener " + " [" + this.hashCode() + "] ");
        JCUtils.getAppCompActivity(getContext()).setRequestedOrientation(NORMAL_ORIENTATION);
        if (currentScreen == JCVideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN
                || currentScreen == JCVideoPlayerStandard.SCREEN_WINDOW_TINY) {
//            if (currentScreen == JCVideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN) {
//                final Animation ra = AnimationUtils.loadAnimation(getContext(), R.anim.quit_fullscreen);
//                startAnimation(ra);
//            }
            onEvent(currentScreen == JCVideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN ?
                    JCBuriedPoint.ON_QUIT_FULLSCREEN :
                    JCBuriedPoint.ON_QUIT_TINYSCREEN);
            if (JCVideoPlayerManager.LISTENERLIST.size() == 1) {//directly fullscreen
                JCVideoPlayerManager.popListener().onCompletion();
                JCMediaManager.instance().releaseMediaPlayer();
                showSupportActionBar(getContext());
                return true;
            }
            ViewGroup vp = (ViewGroup) (JCUtils.scanForActivity(getContext()))//.getWindow().getDecorView();
                    .findViewById(Window.ID_ANDROID_CONTENT);
            vp.removeView(this);
            JCMediaManager.instance().lastState = currentState;//save state
            JCVideoPlayerManager.popListener();
            JCVideoPlayerManager.getFirst().goBackThisListener();
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();

            refreshCache();

            return true;
        }

        return false;
    }

    public static long lastAutoFullscreenTime = 0;

    @Override
    public void autoFullscreen(float x) {
        if (isCurrentMediaListener()
                && currentState == CURRENT_STATE_PLAYING
                && currentScreen != SCREEN_WINDOW_FULLSCREEN
                && currentScreen != SCREEN_WINDOW_TINY) {
            if (x > 0) {
                JCUtils.getAppCompActivity(getContext()).setRequestedOrientation(
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                JCUtils.getAppCompActivity(getContext()).setRequestedOrientation(
                        ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            }
            startWindowFullscreen();
        }
    }

    @Override
    public void autoQuitFullscreen() {
        if ((System.currentTimeMillis() - lastAutoFullscreenTime) > 2000
                && isCurrentMediaListener()
                && currentState == CURRENT_STATE_PLAYING
                && currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            lastAutoFullscreenTime = System.currentTimeMillis();
            backPress();
        }
    }

    @Override
    public void goBackThisListener() {
        Log.i(TAG, "goBackThisListener " + " [" + this.hashCode() + "] ");

        currentState = JCMediaManager.instance().lastState;
        setUiWitStateAndScreen(currentState);
        addTextureView();

        showSupportActionBar(getContext());
    }

    @Override
    public void onBufferingUpdate(int percent) {
        if (currentState != CURRENT_STATE_NORMAL && currentState != CURRENT_STATE_PREPARING) {
            Log.v(TAG, "onBufferingUpdate " + percent + " [" + this.hashCode() + "] ");
            JCMediaManager.instance().bufferPercent = percent;
            setTextAndProgress(percent);
        }
    }

    @Override
    public void onSeekComplete() {
    }

    @Override
    public void onError(int what, int extra) {
        Log.e(TAG, "onError " + what + " - " + extra + " [" + this.hashCode() + "] ");
        if (what != 38 && what != -38) {
            setUiWitStateAndScreen(CURRENT_STATE_ERROR);
        }
    }


    @Override
    public void onInfo(int what, int extra) {
        Log.d(TAG, "onInfo what - " + what + " extra - " + extra);
        if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_START) {
            JCMediaManager.instance().backUpBufferState = currentState;
            setUiWitStateAndScreen(CURRENT_STATE_PLAYING_BUFFERING_START);
            Log.e(TAG, "MEDIA_INFO_BUFFERING_START");
        } else if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
            if (JCMediaManager.instance().backUpBufferState != -1) {
                setUiWitStateAndScreen(JCMediaManager.instance().backUpBufferState);
                JCMediaManager.instance().backUpBufferState = -1;
            }
            Log.e(TAG, "MEDIA_INFO_BUFFERING_END");
        } else if (what == IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED) {
            JCMediaManager.instance().videoRotation = extra;
            JCMediaManager.instance().textureView.setRotation(extra);
            cacheImageView.setRotation(JCMediaManager.instance().videoRotation);
            Log.e(TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED");
        }
    }

    @Override
    public void onVideoSizeChanged() {
        Log.i(TAG, "onVideoSizeChanged " + " [" + this.hashCode() + "] ");
        JCMediaManager.instance().textureView.setVideoSize(JCMediaManager.instance().getVideoSize());
        cacheImageView.setVideoSize(JCMediaManager.instance().getVideoSize());
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.i(TAG, "onSurfaceTextureAvailable [" + this.hashCode() + "] ");
        this.surface = new Surface(surface);
        JCMediaManager.instance().setDisplay(this.surface);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        // 如果SurfaceTexture还没有更新Image，则记录SizeChanged事件，否则忽略
        textureSizeChanged = true;
        Log.i(TAG, "onSurfaceTextureSizeChanged [" + this.hashCode() + "] ");
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        surface.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // 如果textureSizeChanged=true，则说明此次Updated事件不是Image更新引起的   应该是TextureSizeChanged引起的 所以不需要更新 cacheImageView
        Log.i(TAG, "onSurfaceTextureUpdated [" + this.hashCode() + "] textureSizeChanged=" + textureSizeChanged);
        if (!textureSizeChanged) {
            cacheImageView.setVisibility(INVISIBLE);
            JCMediaManager.instance().textureView.setHasUpdated();
        } else {
            textureSizeChanged = false;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.i(TAG, "bottomProgress onStartTrackingTouch [" + this.hashCode() + "] ");
        cancelProgressTimer();
        ViewParent vpdown = getParent();
        while (vpdown != null) {
            vpdown.requestDisallowInterceptTouchEvent(true);
            vpdown = vpdown.getParent();
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.i(TAG, "bottomProgress onStopTrackingTouch [" + this.hashCode() + "] ");
        onEvent(JCBuriedPoint.ON_SEEK_POSITION);
        startProgressTimer();
        ViewParent vpup = getParent();
        while (vpup != null) {
            vpup.requestDisallowInterceptTouchEvent(false);
            vpup = vpup.getParent();
        }
        if (currentState != CURRENT_STATE_PLAYING &&
                currentState != CURRENT_STATE_PAUSE) return;
        int progress = seekBar.getProgress();
        if (progress > 97) {
            progress = 97;
        }
        int time = progress * getDuration() / 100;
        JCMediaManager.instance().mediaPlayer.seekTo(time);
        Log.i(TAG, "seekTo " + time + " [" + this.hashCode() + "] ");
    }

    public static boolean backPress() {
        Log.i(TAG, "backPress");
        if (JCVideoPlayerManager.getFirst() != null) {
            return JCVideoPlayerManager.getFirst().backToOtherListener();
        }
        return false;
    }

    public void startWindowFullscreen() {

        onEvent(JCBuriedPoint.ON_ENTER_FULLSCREEN);
        obtainCache();

        Log.i(TAG, "startWindowFullscreen " + " [" + this.hashCode() + "] ");
        CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
        hideSupportActionBar(getContext());
        JCUtils.getAppCompActivity(getContext()).setRequestedOrientation(FULLSCREEN_ORIENTATION);

        ViewGroup vp = (ViewGroup) (JCUtils.scanForActivity(getContext()))//.getWindow().getDecorView();
                .findViewById(Window.ID_ANDROID_CONTENT);
        View old = vp.findViewById(FULLSCREEN_ID);
        if (old != null) {
            vp.removeView(old);
        }
        if (textureViewContainer.getChildCount() > 0) {
            textureViewContainer.removeAllViews();
        }
        try {
            Constructor<JCVideoPlayer> constructor = (Constructor<JCVideoPlayer>) JCVideoPlayer.this.getClass().getConstructor(Context.class);
            JCVideoPlayer jcVideoPlayer = constructor.newInstance(getContext());
            jcVideoPlayer.setId(FULLSCREEN_ID);
            LayoutParams lp = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            vp.addView(jcVideoPlayer, lp);
            jcVideoPlayer.setVideoInfo(url, JCVideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN, objects);
            jcVideoPlayer.setUiWitStateAndScreen(currentState);
            jcVideoPlayer.addTextureView();
            if (listener != null)
                jcVideoPlayer.setScreenListener(listener);

//            final Animation ra = AnimationUtils.loadAnimation(getContext(), R.anim.start_fullscreen);
//            jcVideoPlayer.setAnimation(ra);

            JCVideoPlayerManager.putListener(jcVideoPlayer);


        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        refreshCache();

    }

    public void startWindowTiny() {
        Log.i(TAG, "startWindowTiny " + " [" + this.hashCode() + "] ");


        ViewGroup vp = (ViewGroup) (JCUtils.scanForActivity(getContext()))//.getWindow().getDecorView();
                .findViewById(Window.ID_ANDROID_CONTENT);
        View old = vp.findViewById(TINY_ID);
        if (old != null) {
            vp.removeView(old);
        }
        if (textureViewContainer.getChildCount() > 0) {
            textureViewContainer.removeAllViews();
        }
        try {
            Constructor<JCVideoPlayer> constructor = (Constructor<JCVideoPlayer>) JCVideoPlayer.this.getClass().getConstructor(Context.class);
            JCVideoPlayer mJcVideoPlayer = constructor.newInstance(getContext());
            mJcVideoPlayer.setId(TINY_ID);
            LayoutParams lp = new LayoutParams(400, 400);
            lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
            vp.addView(mJcVideoPlayer, lp);
            mJcVideoPlayer.setVideoInfo(url, JCVideoPlayerStandard.SCREEN_WINDOW_TINY, objects);
            mJcVideoPlayer.setUiWitStateAndScreen(currentState);

            mJcVideoPlayer.addTextureView();
            JCVideoPlayerManager.putListener(mJcVideoPlayer);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getCurrentPositionWhenPlaying() {
        int position = 0;
        if (currentState == CURRENT_STATE_PLAYING || currentState == CURRENT_STATE_PAUSE || currentState == CURRENT_STATE_PLAYING_BUFFERING_START) {
            try {
                position = (int) JCMediaManager.instance().mediaPlayer.getCurrentPosition();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return position;
            }
        }
        return position;
    }

    public int getDuration() {
        int duration = 0;
        try {
            duration = (int) JCMediaManager.instance().mediaPlayer.getDuration();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return duration;
        }
        return duration;
    }

    public void setTextAndProgress(int secProgress) {
        int position = getCurrentPositionWhenPlaying();
        int duration = getDuration();
        int progress = position * 100 / (duration == 0 ? 1 : duration);
        setProgressAndTime(progress, secProgress, position, duration);
    }

    public void setProgressAndTime(int progress, int secProgress, int currentTime, int totalTime) {
        if (!mTouchingProgressBar) {
            if (progress != 0) progressBar.setProgress(progress);
        }
        if (secProgress > 95) secProgress = 100;
        if (secProgress != 0) progressBar.setSecondaryProgress(secProgress);
        if (currentTime != 0) currentTimeTextView.setText(JCUtils.stringForTime(currentTime));
        totalTimeTextView.setText(JCUtils.stringForTime(totalTime));
    }

    public void resetProgressAndTime() {
        progressBar.setProgress(0);
        progressBar.setSecondaryProgress(0);
        currentTimeTextView.setText(JCUtils.stringForTime(0));
        totalTimeTextView.setText(JCUtils.stringForTime(0));
    }

    public static AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    releaseAllVideos();
                    Log.d(TAG, "AUDIOFOCUS_LOSS [" + this.hashCode() + "]");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    if (JCMediaManager.instance().mediaPlayer.isPlaying()) {
                        JCMediaManager.instance().mediaPlayer.pause();
                    }
                    Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT [" + this.hashCode() + "]");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    break;
            }
        }
    };

    public void release() {
        if (url.equals(JCMediaManager.instance().mediaPlayer.getDataSource()) &&
                (System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME) > FULL_SCREEN_NORMAL_DELAY) {
            //如果正在全屏播放就不能手动调用release
            if (JCVideoPlayerManager.getFirst() != null &&
                    JCVideoPlayerManager.getFirst().getScreenType() != SCREEN_WINDOW_FULLSCREEN) {
                Log.d(TAG, "release [" + this.hashCode() + "]");
                releaseAllVideos();
            }
        }
        cancelProgressTimer();
        if (mAudioManager != null) {
            if (onAudioFocusChangeListener == null)
                mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
    }

    public boolean isPlayed() {
        if (url.equals(JCMediaManager.instance().mediaPlayer.getDataSource()) &&
                (System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME) > FULL_SCREEN_NORMAL_DELAY) {
            //如果正在全屏播放就不能手动调用release
            if (JCVideoPlayerManager.getFirst() != null) {
                return true;
            }
        }
        return false;
    }

    public boolean isCurrentMediaListener() {
        return JCVideoPlayerManager.getFirst() != null
                && JCVideoPlayerManager.getFirst() == this;
    }

    public static void releaseAllVideos() {
        Log.d(TAG, "releaseAllVideos");
        JCVideoPlayerManager.completeAll();
        JCMediaManager.instance().releaseMediaPlayer();
    }

    public static void setJcBuriedPoint(JCBuriedPoint jcBuriedPoint) {
        JC_BURIED_POINT = new WeakReference<>(jcBuriedPoint);
    }

    public void onEvent(int type) {
        if (JC_BURIED_POINT != null && JC_BURIED_POINT.get() != null && isCurrentMediaListener()) {
            JC_BURIED_POINT.get().onEvent(type, url, currentScreen, objects);
        }
    }

    @Override
    public void onScrollChange() {//这里需要自己判断自己是 进入小窗,退出小窗,暂停还是播放
        if (url.equals(JCMediaManager.instance().mediaPlayer.getDataSource())) {
            if (JCVideoPlayerManager.getFirst() == null) return;
            if (JCVideoPlayerManager.getFirst().getScreenType() == SCREEN_WINDOW_TINY) {
                //如果正在播放的是小窗,择机退出小窗
                if (isShown()) {//已经显示,就退出小窗
                    backPress();
                }
            } else {
                //如果正在播放的不是小窗,择机进入小窗
                if (!isShown()) {//已经隐藏
                    if (currentState != CURRENT_STATE_PLAYING) {
                        releaseAllVideos();
                    } else {
                        startWindowTiny();
                    }
                }
            }
        }
    }

    public static void hideSupportActionBar(Context context) {
//        if (ACTION_BAR_EXIST) {
//            ActionBar ab = JCUtils.getAppCompActivity(context).getSupportActionBar();
//            if (ab != null) {
//                ab.setShowHideAnimationEnabled(false);
//                ab.hide();
//            }
//        }
        if (TOOL_BAR_EXIST) {
            JCUtils.getAppCompActivity(context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public static void showSupportActionBar(Context context) {
//        if (ACTION_BAR_EXIST) {
//            ActionBar ab = JCUtils.getAppCompActivity(context).getSupportActionBar();
//            if (ab != null) {
//                ab.setShowHideAnimationEnabled(false);
//                ab.show();
//            }
//        }
        if (TOOL_BAR_EXIST) {
            JCUtils.getAppCompActivity(context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private void obtainCache() {
        Point videoSize = JCMediaManager.instance().getVideoSize();
        if (videoSize != null) {
            Bitmap bitmap = JCMediaManager.instance().textureView.getBitmap(videoSize.x, videoSize.y);
            if (bitmap != null) {
                pauseSwitchCoverBitmap = bitmap;
            }
        }
    }

    private void refreshCache() {
        if (pauseSwitchCoverBitmap != null) {
            JCVideoPlayerStandard jcVideoPlayer = ((JCVideoPlayerStandard) JCVideoPlayerManager.getFirst());
            if (jcVideoPlayer != null) {
                jcVideoPlayer.cacheImageView.setImageBitmap(pauseSwitchCoverBitmap);
                jcVideoPlayer.cacheImageView.setVisibility(VISIBLE);
            }
        }
    }


    public void clearCacheImage() {
        pauseSwitchCoverBitmap = null;
        cacheImageView.setImageBitmap(null);
    }

    public void showWifiDialog() {
    }

    public void showProgressDialog(float deltaX,
                                   String seekTime, int seekTimePosition,
                                   String totalTime, int totalTimeDuration) {
    }

    public void dismissProgressDialog() {

    }

    public void showVolumeDialog(float deltaY, int volumePercent) {

    }

    public void dismissVolumeDialog() {
    }

    public abstract int getLayoutId();


    public void setFullscreen(int currentScreen) {
        this.currentScreen = currentScreen;
    }

    private ScreenListener listener;

    public void setScreenListener(ScreenListener listener) {
        this.listener = listener;
    }

}
