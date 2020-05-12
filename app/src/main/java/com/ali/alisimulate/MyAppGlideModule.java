package com.ali.alisimulate;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Author:关震
 * Date:2020/5/12 17:03
 * Description:MyAppGlideModule
 **/
@GlideModule
public class MyAppGlideModule extends AppGlideModule {
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
