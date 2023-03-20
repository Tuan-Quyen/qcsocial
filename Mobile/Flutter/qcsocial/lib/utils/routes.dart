import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:qcsocial/provider/login_provider.dart';
import '../page/chat_page.dart';
import '../page/login_page.dart';
import '../page/setting_page.dart';
import '../page/room_page.dart';
import '../provider/chat_provider.dart';
import '../provider/room_provider.dart';

class RouteUtils {
  static const login = 'login';
  static const room = 'room';
  static const chat = 'chat';
  static const setting = 'setting';

  Map<String, WidgetBuilder> routePages() {
    return <String, WidgetBuilder>{
      login: (context) => ChangeNotifierProvider(
          create: (_) => LoginProvider(), child: const LoginPage()),
      room: (context) => ChangeNotifierProvider(
          create: (_) => RoomProvider(), child: const RoomPage()),
      chat: (context) => ChangeNotifierProvider(
          create: (_) => ChatProvider(), child: const ChatPage()),
      setting: (context) => const SettingPage()
    };
  }
}
