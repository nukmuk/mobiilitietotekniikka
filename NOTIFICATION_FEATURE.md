# Spinning Motion Detection Notification System

## Overview
This app includes a background motion detection system that monitors for spinning motion using the device's gyroscope sensor. When spinning is detected (even when the app is not in the foreground), a notification is shown that navigates to the Conversation view when tapped.

## Features Implemented

### 1. **NotificationHelper.kt**
- Creates and manages notification channels
- Displays spinning motion notifications
- Configures notification to navigate to Conversation view when tapped

### 2. **MotionDetectionService.kt**
- Foreground service that runs in the background
- Monitors gyroscope sensor for spinning motion
- Threshold: 2.0 rad/s rotation magnitude
- Cooldown: 10 seconds between notifications (prevents spam)
- Automatically started when the app launches

### 3. **NotificationPermissionHandler.kt**
- Requests POST_NOTIFICATIONS permission for Android 13+
- Shows permission request UI if not granted
- Wraps main app content

### 4. **Updated Sensors Screen**
- Displays real-time gyroscope data
- Shows visual indicator when spinning is detected
- Test button to manually trigger notification
- Monitors both light sensor and gyroscope

### 5. **Updated MainActivity**
- Starts MotionDetectionService on app launch
- Handles notification intent to navigate to Conversation view
- Creates notification channels
- Manages service lifecycle

## How It Works

1. **Service Startup**: When the app launches, `MotionDetectionService` starts as a foreground service
2. **Background Monitoring**: The service continuously monitors the gyroscope sensor
3. **Spin Detection**: When rotation magnitude exceeds 2.0 rad/s, spinning is detected
4. **Notification**: A notification is shown with title "Spinning Detected!" and text "Tap to view conversation"
5. **Navigation**: Tapping the notification opens the app and navigates to the Conversation view
6. **Cooldown**: To prevent spam, notifications are only shown once every 10 seconds

## Permissions Required

- `POST_NOTIFICATIONS` - To show notifications (Android 13+)
- `FOREGROUND_SERVICE` - To run background service
- `FOREGROUND_SERVICE_SPECIAL_USE` - For motion detection use case

## Testing

### Manual Test:
1. Navigate to Sensors screen
2. Click "Test Spinning Notification" button
3. Tap the notification to navigate to Conversation view

### Real Test:
1. Put the app in the background or lock the screen
2. Spin/rotate your device rapidly
3. A notification should appear
4. Tap the notification to return to the app and see the Conversation view

## Configuration

### Adjusting Sensitivity
In `MotionDetectionService.kt`, modify the threshold:
```kotlin
if (rotationMagnitude > 2.0f) { // Change this value
```
- Lower values = more sensitive (detects gentle rotation)
- Higher values = less sensitive (requires faster spinning)

### Adjusting Cooldown
In `MotionDetectionService.kt`, modify the cooldown period:
```kotlin
private val spinNotificationCooldown = 10000L // milliseconds
```

## Code Structure

```
app/src/main/java/com/example/composetutorial/
├── MainActivity.kt                    # App entry point, starts service
├── NotificationHelper.kt              # Notification creation and management
├── MotionDetectionService.kt          # Background motion detection service
├── NotificationPermissionHandler.kt   # Permission request UI
└── ui/theme/screens/
    ├── Conversation.kt                # Target screen for notification
    └── Sensors.kt                     # Sensor monitoring UI with test button
```

## Notes

- The service runs continuously in the background to detect motion even when the app is closed
- The foreground service shows a persistent notification indicating it's active
- The spinning detection uses the gyroscope's angular velocity (rad/s)
- Rotation magnitude is calculated as: √(x² + y² + z²)

## Future Improvements

- Add settings to enable/disable motion detection
- Allow user to customize sensitivity threshold
- Add different notification sounds or vibration patterns
- Track and display statistics of detected spins
- Battery optimization modes

