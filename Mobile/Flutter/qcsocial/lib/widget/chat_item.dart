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
        return Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisAlignment: MainAxisAlignment.start,
          children: [
            Text(
              chat.user.name ?? '',
              style: const TextStyle(
                  fontSize: 10,
                  fontStyle: FontStyle.italic,
                  color: Colors.grey),
            ),
            Padding(
              padding: const EdgeInsets.only(left: 10),
              child: Card(
                color: Colors.grey,
                shape: const RoundedRectangleBorder(borderRadius: BorderRadius.all(Radius.circular(10))),
                child: Padding(
                  padding: const EdgeInsets.all(10),
                  child: Text(chat.message ?? ''),
                ),
              ),
            ),
            Container(
              width: 20,
              height: 20,
              decoration: const ShapeDecoration(
                  shape: CircleBorder(), color: Colors.yellow),
            )
          ],
        );
    }
  }
}
