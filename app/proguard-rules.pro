# Keep WebView JS interface if needed
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
