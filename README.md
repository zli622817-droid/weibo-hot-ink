# 微博热搜榜看板

EEGO P4 Pro 墨水屏专用 Android WebView 应用，黑白高对比，实时显示微博热搜榜。

## 特性

- 黑白高对比 UI，适配 E-ink 墨水屏（无彩色、无渐变、无动画）
- 纯 ES5 JavaScript 兼容 WebView 83（Chromium 83）
- 原生 HTTP 拦截请求，自带 UA/Referer，无需 PC 代理
- 显示前 30 条热搜，前 3 条加粗，带 [热]/[新] 标签
- 热度值自动转"万"单位
- 每 5 分钟自动刷新，底部手动刷新按钮
- 552×768 竖屏布局

## 设备要求

- EEGO P4 Pro（4 英寸 E-ink，552×768，Android 8+）
- WiFi 连接

## 构建

```bash
gradle assembleDebug
# APK 输出在 app/build/outputs/apk/debug/app-debug.apk
```

## 安装

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.eego.weibohot/.MainActivity
```

## 技术要点

- `WebViewClient.shouldInterceptRequest` 拦截 `http://localhost/hotsearch`
- 原生 `HttpURLConnection` 请求 `https://weibo.com/ajax/side/hotSearch`
- 设置移动浏览器 UA + Referer 避免 403
- `setAllowUniversalAccessFromFileURLs(true)` 允许 file:// 跨域
- 所有点击用 `addEventListener` 绑定，不绑 touchstart 避免双触发
