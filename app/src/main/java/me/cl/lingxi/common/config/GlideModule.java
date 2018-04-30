package me.cl.lingxi.common.config;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpGlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;

/**
 * Glide 缓存设置
 * 需要在AndroidManifest.xml中配置如下
 * {@code <meta-data android:name="**.GlideModule" android:value="GlideModule" />}
 *
 * 默认name为：com.bumptech.glide.integration.okhttp3.OkHttpGlideModule
 */
public class GlideModule extends OkHttpGlideModule {

    private int diskSize = 1024 * 1024 * 250;//磁盘缓存空间，如果不设置，默认是：250 * 1024 * 1024 即250MB
    private int memorySize = (int) (Runtime.getRuntime().maxMemory()) / 4;  // 取1/4最大内存作为最大缓存

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // 定义缓存大小和位置
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "image",diskSize));  //手机磁盘
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, "image", diskSize)); //sd卡磁盘

        // 默认内存和图片池大小
//        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
//        int defaultMemoryCacheSize = calculator.getMemoryCacheSize(); // 默认内存大小
//        int defaultBitmapPoolSize = calculator.getBitmapPoolSize(); // 默认图片池大小
//        builder.setMemoryCache(new LruResourceCache(defaultMemoryCacheSize)); // 该两句无需设置，是默认的
//        builder.setBitmapPool(new LruBitmapPool(defaultBitmapPoolSize));

        // 自定义内存和图片池大小
        builder.setMemoryCache(new LruResourceCache(memorySize));
        builder.setBitmapPool(new LruBitmapPool(memorySize));

        // 定义图片格式
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
