import 'package:flutter/material.dart';
import 'package:qcsocial/localization/localize.dart';

import '../model/Room.dart';

class RoomItem extends StatelessWidget {
  final Function(String, String)? onPress;
  final Room room;

  const RoomItem({required this.room, Key? key, this.onPress}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () => onPress?.call(room.id, room.name!),
      child: Padding(
        padding: const EdgeInsets.symmetric(vertical: 5),
        child: Card(
          child: Container(
            margin: const EdgeInsets.all(15),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(
                      room.name!,
                      style: const TextStyle(
                          fontSize: 16, fontWeight: FontWeight.bold),
                    ),
                    Text(
                      '${Localize().of.host}: ${room.userHost!}',
                      style: const TextStyle(fontSize: 12),
                    ),
                  ],
                ),
                Padding(
                  padding: const EdgeInsets.only(top: 10),
                  child: Text(
                    '${room.existUser!.length} users ${Localize().of.joined(Localize().of.join)}: ${room.existUser!.map((user) => user.name!).toList()}',
                    style: const TextStyle(fontSize: 12),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
