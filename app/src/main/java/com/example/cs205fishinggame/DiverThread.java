package com.example.cs205fishinggame;
import android.content.Context;
import android.os.Handler;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieListener;

public class DiverThread implements Runnable {
    private final Context context;
    private final LottieDrawable lottieDrawable;
    private final Handler mainHandler;

    public DiverThread(Context context, LottieDrawable lottieDrawable, Handler mainHandler) {
        this.context = context;
        this.lottieDrawable = lottieDrawable;
        this.mainHandler = mainHandler;
    }

    @Override
    public void run() {
        LottieCompositionFactory.fromRawRes(context, R.raw.diver).addListener(new LottieListener<LottieComposition>() {
            @Override
            public void onResult(LottieComposition composition) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        lottieDrawable.setComposition(composition);
                        lottieDrawable.setRepeatCount(LottieDrawable.INFINITE);
                        lottieDrawable.playAnimation();
                    }
                });
            }
        });
    }
}