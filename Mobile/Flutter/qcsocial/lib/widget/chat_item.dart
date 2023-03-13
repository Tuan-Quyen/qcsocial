import 'package:flutter/material.dart';

import '../model/Chat.dart';
import '../model/SocketEvent.dart';

class ChatItem extends StatelessWidget {
  final Chat chat;

  const ChatItem({
    super.key,
    required this.chat,
  });

  @override
  Widget build(BuildContext context) {
    switch (chat.type) {
      case SocketEvent.JOIN_ROOM:
        return Text(
          '${chat.user.name} has joined',
          style: const TextStyle(fontStyle: FontStyle.italic),
        );
      case SocketEvent.DISCONNECTED:
        return Text(
          '${chat.user.name} has disconnected.',
          style: const TextStyle(fontStyle: FontStyle.italic),
        );
      default:
        bool _isUserChat = chat.user.id == "63eca143a2ab7a2629d01bb6";
        return Column(
          crossAxisAlignment:
              _isUserChat ? CrossAxisAlignment.end : CrossAxisAlignment.start,
          mainAxisAlignment: MainAxisAlignment.start,
          children: [
            Text(
              chat.user.name ?? '',
              style: const TextStyle(
                  fontSize: 10,
                  fontStyle: FontStyle.italic,
                  color: Colors.grey),
            ),
            Stack(
              alignment: _isUserChat
                  ? AlignmentDirectional.bottomEnd
                  : AlignmentDirectional.bottomStart,
              children: [
                Padding(
                  padding: const EdgeInsets.only(
                    left: 8,
                    bottom: 20,
                    right: 8,
                  ),
                  child: Card(
                    color: _isUserChat ? Colors.blue : Colors.grey.shade200,
                    shape: const RoundedRectangleBorder(
                        borderRadius: BorderRadius.all(Radius.circular(10))),
                    child: Padding(
                      padding: const EdgeInsets.all(10),
                      child: Text(
                        chat.message ?? '',
                        style: TextStyle(
                          color: _isUserChat ? Colors.white : Colors.black,
                        ),
                      ),
                    ),
                  ),
                ),
                Positioned(
                  bottom: 10,
                  //can be replace by avatar
                  child: Container(
                    width: 25,
                    height: 25,
                    decoration: ShapeDecoration(
                        shape: const CircleBorder(),
                        color: Colors.grey.shade400),
                  ),
                )
              ],
            ),
          ],
        );
    }
  }
}
