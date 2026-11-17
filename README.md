<h1 align="center">💬 QC Social</h1>

<p align="center">
  Real-time chat rooms with a reactive <b>Java Quarkus</b> backend on <b>MongoDB</b> and a bilingual <b>Flutter</b> client.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/Quarkus-2.16-4695EB?style=flat-square&logo=quarkus&logoColor=white" alt="Quarkus">
  <img src="https://img.shields.io/badge/MongoDB-Panache-47A248?style=flat-square&logo=mongodb&logoColor=white" alt="MongoDB">
  <img src="https://img.shields.io/badge/Flutter-Provider-02569B?style=flat-square&logo=flutter&logoColor=white" alt="Flutter">
  <img src="https://img.shields.io/badge/WebSocket-realtime-19C3A6?style=flat-square" alt="WebSocket">
</p>

---

## Overview

QC Social is a personal full-stack project for exploring **reactive Java on Quarkus** together with a **Flutter** client. Users register, join chat rooms and exchange messages in real time over WebSocket. The REST API handles users and rooms; the socket layer handles presence and messaging.

## Features

- **Accounts:** register, log in, search, update and delete users
- **Rooms:** create, search, rename and delete chat rooms; rooms track their members
- **Real-time chat:** join a room over WebSocket, load history, send and delete messages, broadcast to everyone in the room
- **Presence:** join and leave events are pushed to the other members
- **Bilingual client:** English and Vietnamese (`flutter gen-l10n`), plus light and dark themes
- **API docs:** Swagger UI generated from the OpenAPI spec

## Architecture

```text
┌──────────────────────┐   REST /api/users, /api/rooms   ┌──────────────────────────────┐
│  Flutter client      │ ──────────────────────────────▶ │  Quarkus (RESTEasy Reactive)  │
│  Provider · Dio      │                                  │  Controllers → Services       │
│  web_socket_channel  │ ◀──── WebSocket /chat/room/{room}/user/{user} ─── ChatSocket   │
└──────────────────────┘                                  └──────────────┬───────────────┘
                                                                         │ Panache
                                                                         ▼
                                                                ┌─────────────────┐
                                                                │    MongoDB      │
                                                                │ users·rooms·chat│
                                                                └─────────────────┘
```

**Backend** (`BE/`)
- REST controllers share a generic `BaseController<T>` that returns `Uni<Response>` (Mutiny) and maps domain errors (`NotFoundException`, `ConflictException`, `UnauthorizedException`) to HTTP status codes in one place
- Entities are persisted with **MongoDB Panache**; VO/helper classes keep the API shape separate from the documents
- `ChatSocket` keeps a per-room session map and dispatches a small event protocol:

| Event | Direction | What happens |
|---|---|---|
| `CONNECTED` | server → client | user added to the room, room data returned; `JOIN_ROOM` broadcast to others |
| `FETCH_MESSAGE` | client → server | room history returned to the caller |
| `SEND_MESSAGE` | client → server | message saved, then broadcast to the room |
| `DELETE_MESSAGE` | client → server | message removed, deletion broadcast |
| `DISCONNECTED` | server → room | leave event broadcast |

**Mobile** (`Mobile/Flutter/qcsocial/`)
- Pages for rooms, chat and settings; state in `ChangeNotifier` providers
- Dio API client with request logging; models generated with `json_serializable`

## Getting started

### Prerequisites

- JDK 17+, Maven (or the bundled `./mvnw`)
- MongoDB running on `localhost:27017`
- Flutter SDK (Dart ≥ 2.19)

### 1. Backend

```bash
cd BE
./mvnw compile quarkus:dev
```

- API: `http://localhost:8077/api`
- Swagger UI: `http://localhost:8077/swagger`
- Database name: `qcsocial` (see `src/main/resources/application.properties`)

### 2. Mobile client

Point the client at your backend in `lib/utils/constant_utils.dart`:

```dart
static const String BASE_HOST = "<your-ip>:8077";
```

Then:

```bash
cd Mobile/Flutter/qcsocial
flutter pub get
flutter pub run build_runner build --delete-conflicting-outputs
flutter run
```

## API at a glance

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/users` | List users |
| `GET` | `/api/users/search?username=&id=` | Find a user |
| `POST` | `/api/users` | Register |
| `POST` | `/api/users/login` | Log in |
| `PUT` / `DELETE` | `/api/users` | Update / delete |
| `GET` | `/api/rooms` | List rooms |
| `GET` | `/api/rooms/search?name=&id=` | Find a room (with members) |
| `POST` / `PUT` / `DELETE` | `/api/rooms` | Create / update / delete |

## Project status and next steps

This is a learning project, not production code. Known gaps I'd close next:

- [ ] Hash passwords (bcrypt/Argon2) and issue JWTs instead of plain credential checks
- [ ] Authenticate the WebSocket handshake instead of trusting the user id in the URL
- [ ] Move the backend host into build-time config (`--dart-define`) instead of a constant
- [ ] Integration tests for the socket protocol with `@QuarkusTest`

## Author

**Quyen Chau** · Full-stack developer · [Portfolio](https://quyenchau.pages.dev) · [LinkedIn](https://www.linkedin.com/in/quy%E1%BB%81n-ch%C3%A2u-7b1331246/)
