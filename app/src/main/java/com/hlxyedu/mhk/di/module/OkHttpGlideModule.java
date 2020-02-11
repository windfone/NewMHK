package com.hlxyedu.mhk.di.module;

//public class OkHttpGlideModule implements GlideModule {
//    @Override
//    public void applyOptions(Context context, GlideBuilder builder) {
//
//    }
//
//    @Override
//    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
//        OkHttpClient client = HttpsUtils.getUnsafeOkHttpClient();
//        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));
////        HttpModule httpModule = new HttpModule();
////        OkHttpClient okHttpClient = httpModule.provideClient(new OkHttpClient.Builder());
////        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));
//    }
//}
