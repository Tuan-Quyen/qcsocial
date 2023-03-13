import 'package:flutter/material.dart';

import '../controller/room_controller.dart';
import '../model/Room.dart';

class RoomProvider extends ChangeNotifier {
  late Function(String) onError;
  List<Room> _rooms = [];

  final _roomController = RoomController();

  List<Room> get rooms => _rooms;

  fetchRooms() async {
    try {
      _rooms = await _roomController.getRooms();
    } catch (e) {
      onError(e.toString());
    }
    notifyListeners();
  }
}
