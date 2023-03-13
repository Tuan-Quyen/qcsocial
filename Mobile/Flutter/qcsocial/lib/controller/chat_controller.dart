import 'dart:convert';

import 'package:web_socket_channel/web_socket_channel.dart';

import '../model/SocketEvent.dart';
import '../utils/constant_utils.dart';

class ChatController {
  late Function(String) onError;
  late WebSocketChannel _socket;
  late String _url;

  ChatController(String roomId, String userId) {
    _url = "${ConstantUtils.BASE_URL_SOCKET}$roomId/user/$userId";
  }

  connect(Function(SocketData event) onData, Function(dynamic error) onError) {
    print(">>> CONNECT: $_url");
    _socket = WebSocketChannel.connect(Uri.parse(_url));
    _socket.stream.listen(
        (data) => onData(SocketData.fromJson(jsonDecode(data))),
        onError: onError,
        onDone: () => print(">>> SOCKET CLOSE."));
  }

  disconnect() {
    print(">>> DISCONNECT.");
    _socket.sink.close();
  }

  getChats() {
    var data = jsonEncode(SocketData(SocketEvent.FETCH_MESSAGE).toJson());
    print(">>> SEND: $data");
    _socket.sink.add(data);
  }

  sendMessage(message) {
    var socket = SocketData(SocketEvent.SEND_MESSAGE);
    socket.data = {"message": message};
    var data = jsonEncode(socket.toJson());
    print(">>> SEND: $data");
    _socket.sink.add(data);
  }
}
