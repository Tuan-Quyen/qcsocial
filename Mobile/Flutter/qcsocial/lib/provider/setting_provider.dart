import 'package:flutter/material.dart';
import 'dart:ui' as ui;

class SettingProvider extends ChangeNotifier {
  Locale _locale = ui.window.locale;
  ThemeMode _themeMode = ThemeMode.light;

  Locale get locale => _locale;

  bool get isDarkMode => _themeMode == ThemeMode.dark;

  ThemeMode get themeMode => _themeMode;

  void setLocale(Locale locale) {
    if (locale.languageCode == 'en') {
      _locale = const Locale("vi");
    } else {
      _locale = const Locale("en");
    }
    // locale;
    notifyListeners();
  }

  void switchTheme(bool isDark) {
    _themeMode = isDark ? ThemeMode.dark : ThemeMode.light;
    notifyListeners();
  }
}
