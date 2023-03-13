import 'dart:convert';

import 'package:dio/dio.dart';
import 'base_api.dart';
import '../model/Room.dart';

class RoomController {
  final _controller = ApiProvider().baseApi;

  Future<List<Room>> getRooms() async {
    try {
      final response = await _controller.of().get("rooms");
      Map responseBody = json.decode(response.data);
      List data = responseBody['data'];
      return data.map((element) => Room.fromJson(element)).toList();
    } on DioError catch (e) {
      throw ApiProvider().handleError(e);
    }
  }
}
