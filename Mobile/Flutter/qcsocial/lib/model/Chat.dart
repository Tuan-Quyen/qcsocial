import 'package:json_annotation/json_annotation.dart';
import 'SocketEvent.dart';
import 'User.dart';

part 'Chat.g.dart';

@JsonSerializable()
class Chat {
  @JsonKey(required: true)
  String id = "";
  @JsonKey(required: true)
  String roomId = "";
  String? message;
  @JsonKey(required: true)
  User user;
  @JsonKey(includeFromJson: false)
  SocketEvent? type = SocketEvent.SEND_MESSAGE;

  Chat(this.id, this.roomId, this.user, {this.type});

  factory Chat.actionMessage(user, type) => Chat("", "", user, type: type);

  factory Chat.fromJson(Map<String, dynamic> json) => _$ChatFromJson(json);

  Map<String, dynamic> toJson() => _$ChatToJson(this);
}
