// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'Chat.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Chat _$ChatFromJson(Map<String, dynamic> json) {
  $checkKeys(
    json,
    requiredKeys: const ['id', 'roomId', 'user'],
  );
  return Chat(
    json['id'] as String,
    json['roomId'] as String,
    User.fromJson(json['user'] as Map<String, dynamic>),
  )..message = json['message'] as String?;
}

Map<String, dynamic> _$ChatToJson(Chat instance) => <String, dynamic>{
      'id': instance.id,
      'roomId': instance.roomId,
      'message': instance.message,
      'user': instance.user,
    };
