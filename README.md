# BookManagementApp
書籍・読了状況管理アプリケーション（バーコードから書籍情報をAPI通信により取得）

### 個人的なこだわり
- バーコード読み取り画面をライブラリの[サンプルアプリ](https://github.com/journeyapps/zxing-android-embedded/blob/master/sample/src/main/java/example/zxing/MainActivity.java)を参考にAndroid Viewで独自実装
- Javaで書かれていたCustomScannerActivityをKotlinにリファクタリングして使用（100% Kotlinを維持）
