import 'package:json_annotation/json_annotation.dart';
import 'User.dart';

part 'Room.g.dart';

@JsonSerializable()
class Room {
  @JsonKey(required: true)
  String id;
  String? name;
  String? userHost;
  List<User>? existUser;

  Room(this.id, this.name, this.userHost, this.existUser);

  factory Room.fromJson(Map<String, dynamic> json) => _$RoomFromJson(json);

  Map<String, dynamic> toJson() => _$RoomToJson(this);
}