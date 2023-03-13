import 'package:json_annotation/json_annotation.dart';

part 'SocketEvent.g.dart';

@JsonSerializable()
class SocketData {
  @JsonKey(required: true)
  SocketEvent? event;
  dynamic data;

  SocketData(this.event, {this.data});

  factory SocketData.fromJson(Map<String, dynamic> json) => _$SocketDataFromJson(json);

  Map<String, dynamic> toJson() => _$SocketDataToJson(this);
}

enum SocketEvent {
  CONNECTED,
  JOIN_ROOM,
  ERROR,
  FETCH_MESSAGE,
  DISCONNECTED,
  SEND_MESSAGE,
  DELETE_MESSAGE
}
