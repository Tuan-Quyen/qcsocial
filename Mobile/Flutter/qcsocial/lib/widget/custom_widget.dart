import 'package:flutter/material.dart';

class CustomWidget {
  static ThemeData themeData({isDark = false}) {
    if (isDark) {
      return ThemeData.dark();
    }
    return ThemeData.light();
  }

  static Function(String) showError(context) => (error) => showDialog(
        context: context,
        builder: (BuildContext context) => AlertDialog(
          title: const Text('Lỗi'),
          content: Text(error),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text('Đóng'),
            ),
          ],
        ),
      );

  static Function(String) showRetry(context, VoidCallback action) => (error) => showDialog(
    context: context,
    builder: (BuildContext context) => AlertDialog(
      title: const Text('Lỗi'),
      content: Text(error),
      actions: [
        TextButton(
          onPressed: () {
            action.call();
            Navigator.pop(context);
          },
          child: const Text('Thử lại'),
        ),
        TextButton(
          onPressed: () => Navigator.pop(context),
          child: const Text('Đóng'),
        ),
      ],
    ),
  );
}
