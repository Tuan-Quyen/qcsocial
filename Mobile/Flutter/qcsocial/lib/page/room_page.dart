import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../utils/routes.dart';
import '../widget/custom_widget.dart';
import '../widget/room_item.dart';
import '../localization/localize.dart';
import '../provider/room_provider.dart';

class RoomPage extends StatefulWidget {
  const RoomPage({Key? key}) : super(key: key);

  @override
  State<RoomPage> createState() => _RoomPageState();
}

class _RoomPageState extends State<RoomPage> {
  late RoomProvider _roomProvider;

  @override
  void initState() {
    super.initState();
    Future.microtask(() => _roomProvider
      ..onError = CustomWidget.showError(context)
      ..fetchRooms());
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    Localize().init(context);
    _roomProvider = Provider.of<RoomProvider>(context);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      endDrawerEnableOpenDragGesture: true,
      resizeToAvoidBottomInset: true,
      endDrawer: Drawer(
        child: ListView(
          children: [
            ListTile(
              leading: const Icon(Icons.settings),
              title: Text(
                Localize().of.setting,
                style: const TextStyle(fontSize: 18),
              ),
              onTap: () => Navigator.of(context).pushNamed(RouteUtils.setting),
            )
          ],
        ),
      ),
      appBar: AppBar(
        title: Text(Localize().of.room),
      ),
      body: RefreshIndicator(
        onRefresh: () => _roomProvider.fetchRooms(),
        child: ListView.builder(
          physics: const AlwaysScrollableScrollPhysics(),
          itemCount: _roomProvider.rooms.length,
          itemBuilder: (context, index) => RoomItem(
            room: _roomProvider.rooms[index],
            onPress: (id, name) => Navigator.of(context).pushNamed(
              RouteUtils.chat,
              arguments: {
                'id': id,
                'name': name,
              },
            ),
          ),
        ),
      ),
    );
  }
}
