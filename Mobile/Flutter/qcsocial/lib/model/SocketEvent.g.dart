// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'SocketEvent.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

SocketData _$SocketDataFromJson(Map<String, dynamic> json) {
  $checkKeys(
    json,
    requiredKeys: const ['event'],
  );
  return SocketData(
    $enumDecodeNullable(_$SocketEventEnumMap, json['event']),
    data: json['data'],
  );
}

Map<String, dynamic> _$SocketDataToJson(SocketData instance) =>
    <String, dynamic>{
      'event': _$SocketEventEnumMap[instance.event],
      'data': instance.data,
    };

const _$SocketEventEnumMap = {
  SocketEvent.CONNECTED: 'CONNECTED',
  SocketEvent.JOIN_ROOM: 'JOIN_ROOM',
  SocketEvent.ERROR: 'ERROR',
  SocketEvent.FETCH_MESSAGE: 'FETCH_MESSAGE',
  SocketEvent.DISCONNECTED: 'DISCONNECTED',
  SocketEvent.SEND_MESSAGE: 'SEND_MESSAGE',
  SocketEvent.DELETE_MESSAGE: 'DELETE_MESSAGE',
};
