import 'package:flutter/material.dart';
import 'package:qcsocial/model/SocketEvent.dart';

import '../controller/chat_controller.dart';
import '../model/Chat.dart';
import '../model/User.dart';

class ChatProvider extends ChangeNotifier {
  late Function(String) _onError;
  late final ChatController _chatController;

  List<Chat> _chats = [];

  List<Chat> get chats => _chats;

  set onError(dynamic value) {
    _onError = value;
    notifyListeners();
  }



  init(String roomId) {
    _chatController = ChatController(roomId, "63eca143a2ab7a2629d01bb6");
  }

  connect() => _chatController.connect(
        (message) {
          switch (message.event) {
            case SocketEvent.CONNECTED:
              fetchChats();
              break;
            case SocketEvent.JOIN_ROOM:
            case SocketEvent.DISCONNECTED:
              var userData = User.fromJson(message.data);
              var chat = Chat.actionMessage(userData, message.event);
              _chats.add(chat);
              notifyListeners();
              break;
            case SocketEvent.FETCH_MESSAGE:
              List data = message.data;
              _chats = data.map((element) => Chat.fromJson(element)).toList();
              notifyListeners();
              break;
            case SocketEvent.SEND_MESSAGE:
              var data = Chat.fromJson(message.data);
              _chats.add(data);
              notifyListeners();
              break;
            case SocketEvent.DELETE_MESSAGE:
              print(">>> EVENT: ${message.event} | DATA: ${message.data}");
              break;
            default:
              break;
          }
        },
        (error) async {
          print(">>> ERROR: $error");
          await Future.delayed(
            const Duration(seconds: 1),
            () => _onError(error.toString()),
          );
        },
      );

  fetchChats() => _chatController.getChats();

  sendMessage(message) => _chatController.sendMessage(message);

  disconnect() => _chatController.disconnect();
}
