package com.tiger.socol.gu.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.medialibrary.video.JCVideoPlayer;
import com.medialibrary.video.JCVideoPlayerStandard;
import com.medialibrary.video.listener.ScreenListener;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.api.good.VideoEntity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoPlayerView extends RelativeLayout {
    @BindView(R.id.custom_videoplayer_standard)
    JCVideoPlayerStandard vspVideoplayer;

    private Context context;

    public VideoPlayerView(Context context) {
        this(context, null);
    }

    public VideoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public void init() {
        View rootView = inflate(context, R.layout.view_lesson_type, this);
        ButterKnife.bind(this, rootView);
    }

    public void setLiving(boolean isLiving) {
        vspVideoplayer.setLiving(isLiving);
    }

    private String videoUrl;
    private String thumb;

    /**
     * 设置课程信息
     */
    public void setVideoInfo(VideoEntity videoInfo) {
        VideoEntity.AddressBean adress = videoInfo.getAddress();
//        if (adress == null) {
            videoUrl = "http://dianbo.vodjk.com/vod/xinma/jkzx/yaoshitong/2016/08/04/DA8D7BCA21B746b9A584CACFC448CDF4.mp4";
            thumb = "http://file31.mafengwo.net/M00/01/8A/wKgBs1aP5umAcminAAcTsgrNpQw97.groupinfo.w600.jpeg";
//        } else {
//            videoUrl = adress.getHd();
//            thumb = "";
//        }
        vspVideoplayer = (JCVideoPlayerStandard) findViewById(R.id.custom_videoplayer_standard);
        vspVideoplayer.setVideoInfo(videoUrl, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
        Glide.with(context)
                .load(thumb)
                .placeholder(R.mipmap.bg_default)
                .into(vspVideoplayer.thumbImageView);
    }

    private boolean isPause;

    /**
     * 视频暂停
     */
    public void setMideapalyerPause() {
        if (vspVideoplayer != null) {
            vspVideoplayer.mediaPause();
            isPause = true;
        }
    }

    /**
     * 视频继续
     */
    public void setMideapalyerResume() {
        if (vspVideoplayer != null) {
            if (isPause) {
                vspVideoplayer.mediaResume();
                isPause = false;
            }
        }
    }

    public boolean isMideaPlayed() {
        return vspVideoplayer.isPlayed();
    }

    public int getMediaPlayerPosition() {
        return vspVideoplayer.getCurrentPositionWhenPlaying() / 1000;
    }

    public void releaseMediaPlayer() {
        if (vspVideoplayer != null) {
            vspVideoplayer.release();
        }
    }

    public void setLandscape(boolean isLandscape) {
        if (isLandscape) {
            // 切换成横屏
            vspVideoplayer.startWindowFullscreen();
        } else {
            // 切换成竖屏
            JCVideoPlayer.backPress();
        }
    }

    public void setScreenListener(ScreenListener listener) {
        vspVideoplayer.setScreenListener(listener);
    }

}
