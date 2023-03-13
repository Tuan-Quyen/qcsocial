import '../localization/localize.dart';

class ConstantUtils {
  static const String BASE_HOST = "192.168.70.138:8077";
  static const String BASE_URL_API = "http://$BASE_HOST/api/";
  static const String BASE_URL_SOCKET = "ws://$BASE_HOST/chat/room/";

  List<String> monthList() => [
    Localize().of.jan,
    Localize().of.fer,
    Localize().of.mar,
    Localize().of.apr,
    Localize().of.may,
    Localize().of.jun,
    Localize().of.jul,
    Localize().of.aug,
    Localize().of.sep,
    Localize().of.oct,
    Localize().of.nov,
    Localize().of.dec,
  ];
}
