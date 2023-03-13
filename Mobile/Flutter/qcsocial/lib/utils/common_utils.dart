import '../localization/localize.dart';

class CommonUtils {
  static String compareCurrentTime(String time, var context) {
    if (time.isEmpty) {
      return "";
    }
    var currentTime = DateTime.now().add(const Duration(hours: 20));
    var time0 = DateTime.parse(time);
    var localize = Localize().of;
    var sDefine = localize.localeName == "en" ? "s" : "";
    var diff = currentTime.difference(time0);
    if (diff.inDays > 365) {
      return time0.year.toString();
    }
    if (diff.inDays > 30) {
      var month = localize.month;
      return "${(diff.inDays / 30).floor()} ${(diff.inDays / 30).floor() == 1 ? month : "$month$sDefine"} ${localize.ago}";
    }
    if (diff.inDays > 7) {
      var week = localize.week;
      return "${(diff.inDays / 7).floor()} ${(diff.inDays / 7).floor() == 1 ? week : "$week$sDefine"} ${localize.ago}";
    }
    if (diff.inDays > 0) {
      var day = localize.day;
      return "${diff.inDays} ${diff.inDays == 1 ? day : "$day$sDefine"} ${localize.ago}";
    }
    if (diff.inHours > 0) {
      var hour = localize.hour;
      return "${diff.inHours} ${diff.inHours == 1 ? hour : "$hour$sDefine"} ${localize.ago}";
    }
    if (diff.inMinutes > 0) {
      var minute = localize.minute;
      return "${diff.inMinutes} ${diff.inMinutes == 1 ? minute : "$minute$sDefine"} ${localize.ago}";
    }
    if (diff.inSeconds >= 0) {
      return localize.just_now;
    }
    return time0.toLocal().toString();
  }

  static int compareTime(String timeA, String timeB) {
    if (timeA.isEmpty || timeB.isEmpty) {
      return 0;
    }
    var timeA0 = DateTime.parse(timeA);
    var timeB0 = DateTime.parse(timeB);
    var diff = timeA0.difference(timeB0);
    if (diff.inDays > 0) {
      return diff.inDays;
    } else if (diff.inHours > 0) {
      return diff.inHours;
    } else if (diff.inMinutes > 0) {
      return diff.inMinutes;
    } else if (diff.inSeconds > 0) {
      return diff.inSeconds;
    } else {
      return 0;
    }
  }
}
