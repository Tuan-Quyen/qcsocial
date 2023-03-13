import 'package:flutter/material.dart';

import '../localization/localize.dart';

class SettingPage extends StatefulWidget {

  const SettingPage({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _SettingPage();
}

class _SettingPage extends State<SettingPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(Localize().of.room),
      ),
      body: const Center(),
    );
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    Localize().init(context);
  }
}
