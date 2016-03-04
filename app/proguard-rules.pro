-optimizationpasses 5  
-dontusemixedcaseclassnames  
-dontskipnonpubliclibraryclasses  
-dontpreverify  
-verbose  
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  
  
-keepattributes *Annotation*  
-keepattributes Signature

-ignorewarnings
-dontwarn
-dontskipnonpubliclibraryclassmembers

#-libraryjars D:/git-repository/beautifulproscenium/EmptyLayout_2.x/libs/android-support-v4.jar
#-libraryjars D:/git-repository/beautifulproscenium/EmptyLayout_2.x/libs/androidInject_1.3.jar
#-libraryjars D:/git-repository/beautifulproscenium/EmptyLayout_2.x/libs/gson-2.2.4.jar
#-libraryjars D:/git-repository/beautifulproscenium/EmptyLayout_2.x/libs/universal-image-loader-1.9.1-with-sources.jar
#-libraryjars D:/Exploit/Libs/OneKeyShare/libs/mframework.jar
#-libraryjars D:/Exploit/AndroidBucket-2r1_refactor/libs/android-support-v4.jar
#-libraryjars libs/jpush-sdk-release1.7.3.jar
#-libraryjars libs/locSDK_4.2.jar
#-libraryjars libs/xcl-charts.jar
#-libraryjars libs/qrcode.jar
#-libraryjars libs/volley.jar


-keep public class * extends android.app.Fragment  
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.**
-keep public class com.android.vending.licensing.ILicensingService

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keep public class com.shboka.beautyorder.R$*{
	public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-dontshrink
-dontoptimize




