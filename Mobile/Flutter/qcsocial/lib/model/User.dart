import 'package:json_annotation/json_annotation.dart';

part 'User.g.dart';

@JsonSerializable()
class User {
  @JsonKey(required: true)
  String id;
  String? name;
  String? userName;
  @JsonKey(includeFromJson: false)
  late String? password;
  String? email;
  bool? isOnline;

  User(this.id, this.name, this.userName, this.email, this.isOnline,
      {this.password});

  factory User.fromJson(Map<String, dynamic> json) => _$UserFromJson(json);

  Map<String, dynamic> toJson() => _$UserToJson(this);
}
