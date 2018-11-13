package framgia.com.ichat;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

@GlideModule
public class ApplicationGlideModule extends AppGlideModule {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 300;
    public static final int EMOJI_WITH = 300;
    public static final int EMOJI_HEIGHT = 300;

    public static void loadImage(Context context, String path, ImageView imageView) {
        GlideApp.with(context)
                .load(path)
                .override(ApplicationGlideModule.EMOJI_WITH,
                        ApplicationGlideModule.EMOJI_HEIGHT)
                .into(imageView);
    }

    public static void loadCircleImage(Context context, String path, ImageView imageView) {
        GlideApp.with(context)
                .load(path)
                .override(ApplicationGlideModule.EMOJI_WITH,
                        ApplicationGlideModule.EMOJI_HEIGHT)
                .circleCrop()
                .into(imageView);
    }
}
