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
      'ws://192.168.70.138:8077/chat/room/63ecbffe7c7dda063f67ecb0/user/63e610089ac3f82b3142c12b'));
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
      body: Padding(
        padding: const EdgeInsets.all(20.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Form(
              child: TextFormField(
                controller: _controller,
                decoration: const InputDecoration(labelText: 'Send a message'),
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
        "data": _controller.text
      };
      _channel.sink.add(json.encode(data));
    }
  }

  void _reconnect() {
    _channel.sink.close();
    setState(() {
      _channel = WebSocketChannel.connect(Uri.parse(
          'ws://192.168.70.138:8077/chat/room/63ecbffe7c7dda063f67ecb0/user/63e610089ac3f82b3142c12b'));
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
