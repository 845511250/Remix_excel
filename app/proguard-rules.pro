# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\AndroidSDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-optimizationpasses 4

-keep public class * extends android.support.**
-dontwarn android.support.**

-keep class com.example.zuoyun.remix_excel.bean.ResponseData {*;}
-keep class com.example.zuoyun.remix_excel.tools.** {*;}
-keep class com.example.zuoyun.remix_excel.bean.** {*;}
-keep class com.example.zuoyun.remix_excel.activity.start.bean.** {*;}

-keep class jxl.** { *; }

-keep class butterknife.** { *; }

-keep class com.yolanda.nohttp.** { *; }
-dontwarn com.yolanda.nohttp.**

-keep class com.alibaba.fastjson.** { *; }
-dontwarn com.alibaba.fastjson.**

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {**[] $VALUES;public *;}

-ignorewarnings

-keep class aavax.xml.** { *; }
-keep class com.bea.xml.stream.** { *; }
-keep class com.wutka.dtd.** { *; }
-keep class org.apache.** { *; }
-keep class org.dom4j.** { *; }
-keep class org.repackage.** { *; }
-keep class schemaorg_apache_xmlbeans.** { *; }

-keep class com.microsoft.schemas.office.x2006.** { *; }
-keep class org.etsi.uri.x01903.v13.** { *; }
-keep class org.openxmlformats.** { *; }
-keep class org.w3.x2000.x09.xmldsig.** { *; }
-keep class schemaorg_apache_xmlbeans.** { *; }
-keep class schemasMicrosoftComOfficeExcel.** { *; }
-keep class schemasMicrosoftComOfficeOffice.** { *; }
-keep class schemasMicrosoftComVml.** { *; }
