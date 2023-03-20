/*
import 'dart:collection';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:web_socket_channel/web_socket_channel.dart';

void main() {
  runApp(const App());
}

class App extends StatelessWidget {
  const App({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const HomePage(title: 'HomePage'),
    );
  }
}

class HomePage extends StatefulWidget {
  const HomePage({super.key, required this.title});

  final String title;

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  final TextEditingController _controller = TextEditingController();
  var _channel = WebSocketChannel.connect(Uri.parse(
      'ws://192.168.70.138:8077/chat/room/63f714391ada7255a964b87f/user/63eca210a2ab7a2629d01bb8'));
  late Stream _stream;

  @override
  void initState() {
    super.initState();
    _stream = _channel.stream.asBroadcastStream();
  }

  @override
  Widget build(BuildContext context) {
    _stream.listen((event) {
      print("Stream listen:" + event);
    });
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: RefreshIndicator(
        onRefresh: () async => _refresh(),
        child: SingleChildScrollView(
          physics: const AlwaysScrollableScrollPhysics(),
          child: Padding(
            padding: const EdgeInsets.all(20.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Form(
                  child: TextFormField(
                    controller: _controller,
                    decoration:
                        const InputDecoration(labelText: 'Send a message'),
                  ),
                ),
                const SizedBox(height: 24),
                StreamBuilder(
                  stream: _stream,
                  builder: (context, snapshot) {
                    return Text(snapshot.hasData ? '${snapshot.data}' : '');
                  },
                )
              ],
            ),
          ),
        ),
      ),
      floatingActionButton: Row(
        mainAxisAlignment: MainAxisAlignment.end,
        children: [
          FloatingActionButton(
            onPressed: _sendMessage,
            tooltip: 'Send message',
            child: const Icon(Icons.send),
          ),
          const Padding(padding: EdgeInsets.only(right: 20)),
          FloatingActionButton(
            onPressed: _reconnect,
            tooltip: 'Reconnect',
            child: const Icon(Icons.connect_without_contact),
          )
        ],
      ),
    );
  }

  void _sendMessage() {
    if (_controller.text.isNotEmpty) {
      var data = {
        "event": "SEND_MESSAGE",
        "data": {"message": _controller.text}
      };
      _channel.sink.add(json.encode(data));
    }
  }

  void _refresh() {
    var data = {"event": "FETCH_MESSAGE"};
    _channel.sink.add(json.encode(data));
  }

  void _reconnect() {
    _channel.sink.close();
    setState(() {
      _channel = WebSocketChannel.connect(Uri.parse(
          'ws://192.168.70.138:8077/chat/room/63f714391ada7255a964b87f/user/63eca210a2ab7a2629d01bb8'));
      _stream = _channel.stream.asBroadcastStream();
    });
  }

  @override
  void dispose() {
    _channel.sink.close();
    _controller.dispose();
    super.dispose();
  }
}
*/

import 'package:flutter/material.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:provider/provider.dart';
import 'localization/localize.dart';
import 'provider/setting_provider.dart';
import 'utils/routes.dart';
import 'widget/custom_widget.dart';

void main() {
  runApp(const App());
}

class App extends StatelessWidget {
  const App({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    late SettingProvider _settingProvider;
    return MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (context) => SettingProvider())
      ],
      builder: (context, child) {
        _settingProvider = context.read<SettingProvider>();
        return MaterialApp(
          debugShowCheckedModeBanner: false,
          locale: _settingProvider.locale,
          localizationsDelegates: [
            Localize().delegate(),
            GlobalMaterialLocalizations.delegate,
            GlobalCupertinoLocalizations.delegate,
            GlobalWidgetsLocalizations.delegate,
          ],
          supportedLocales: Localize().supportedLocales(),
          theme: CustomWidget.themeData(),
          darkTheme: CustomWidget.themeData(isDark: true),
          themeMode: _settingProvider.themeMode,
          initialRoute: RouteUtils.login,
          routes: RouteUtils().routePages(),
        );
      },
    );
  }
}
