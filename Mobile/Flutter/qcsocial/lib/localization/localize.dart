import 'package:flutter/material.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class Localize {
  late AppLocalizations of;
  static final Localize _instance = Localize._internal();

  Localize._internal();

  factory Localize() => _instance;

  init(BuildContext context) => of = AppLocalizations.of(context)!;

  List<Locale> supportedLocales() => AppLocalizations.supportedLocales;

  LocalizationsDelegate<AppLocalizations> delegate() => AppLocalizations.delegate;
}
