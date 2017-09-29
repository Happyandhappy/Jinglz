#Base Package
-keep class com.jinglz.app.** { *; }

#Base
-keepattributes Signature
-keepattributes Exceptions
-keepattributes EnclosingMethod
-keepattributes *Annotation*
-keep class **.R$* {
  <fields>;
}

#Retrolambda
-dontwarn java.lang.invoke.*

#Mixpanel
-dontwarn com.mixpanel.**

#AWS
-keepnames class com.amazonaws.**
-keep class com.amazonaws.services.**.*Handler
-dontwarn com.fasterxml.jackson.**
-dontwarn org.apache.commons.logging.**
-dontwarn org.apache.http.**
-dontwarn com.amazonaws.http.**
-dontwarn com.amazonaws.metrics.**

#ReactiveNetwork
-dontwarn com.github.pwittchen.reactivenetwork.library.ReactiveNetwork
-dontwarn io.reactivex.functions.Function
-dontwarn rx.internal.util.**

#Retrofit
-dontwarn retrofit2.adapter.rxjava.CompletableHelper$**
-dontnote retrofit2.Platform
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn retrofit2.Platform$Java8
-dontwarn okio.**
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

#RxJava
-keep class rx.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.android.schedulers.Schedulers {
    public static <methods>;
}

#GSON
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-dontwarn sun.misc.Unsafe

#OkHttp
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

#RxBus
-dontwarn com.hwangjr.rxbus.**
-keep class com.hwangjr.rxbus.** { *;}
-keepclassmembers class ** {
    @com.hwangjr.rxbus.annotation.Subscribe public *;
    @com.hwangjr.rxbus.annotation.Produce public *;
}

#Branch
-keep class com.google.android.gms.ads.identifier.** { *; }

#Gms
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**
